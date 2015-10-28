package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import presenter.Presenter;
import presenter.Properties;
/**
 * <h2>MyModel</h2>
 * this class implements the model interface which will manage all the server commands
 * @param int port
 * @param ServerSocket server
 * @param MyClientHandler clinetHandler
 * @param int numOfClients
 * @param ExecutorService threadpool
 * @param volatile boolean stop
 * @param Thread ServerThread
 * @param int clientsHandled
 * @param Properties properties
 * @param Presenter presenter
 * @param boolean serverWasOpen
 * 
 * 
 * @author Alon Tal & Omry Dabush
 *
 */
public class Mymodel extends Observable implements Model{

	int port;
	ServerSocket server;
	MyClientHandler clinetHandler;
	int numOfClients;
	ExecutorService threadpool;
	volatile boolean stop;
	Thread ServerThread;
	int clientsHandled=0;
	Properties properties;
	Presenter presenter;
	boolean serverWasOpen = false;
	
	
/**
 * At start the class will load the properties and will sets the right data members
 * 
 */
	public Mymodel() {
		loadProperties();
		threadpool=Executors.newFixedThreadPool(Integer.parseInt(properties.getNumOfClients()));
		clinetHandler = new MyClientHandler();
	}
	
/**
 * When the server starts it will wait until a client will connect and add a request for him to handle
 * 
 */
	@Override
	public void openserver() throws IOException{
		serverWasOpen = true;
		server=new ServerSocket(Integer.parseInt(properties.getPort()));
		server.setSoTimeout(10*1000);
		
		ServerThread=new Thread(new Runnable() {			
			@Override
			public void run() {
				while(!stop){
					try {
						final Socket someClient=server.accept();
						if(someClient!=null){
							threadpool.execute(new Runnable() {									
								@Override
								public void run() {
									try{										
										clientsHandled++;
										System.out.println("\thandling client "+clientsHandled);
										clinetHandler.handleClient(someClient.getInputStream(), someClient.getOutputStream());
										System.out.println("\tdone handling client "+clientsHandled);
										someClient.close();
									}catch(IOException e){
										setChanged();
										notifyObservers(e);
									}									
								}
							});								
						}
					}
					catch (SocketTimeoutException e){
						setChanged();
						notifyObservers("no clinet connected...");
					} 
					catch (IOException e) {
						setChanged();
						notifyObservers(e);
					}
				}
				setChanged();
				notifyObservers("done accepting new clients.");
			} // end of the mainServerThread task
		});
		
		ServerThread.start();
		
	}
	
	
	
	/**
	 * This method is responsible to close all the open threads and close the server app properly
	 * 
	 */
	@Override
	public void closeserver(){
		this.stop=true;	
		if (clinetHandler != null)
			clinetHandler.saveMazes();
		// do not execute jobs in queue, continue to execute running threads
		setChanged();
		notifyObservers("shutting down");
		clinetHandler.exitClient();
		threadpool.shutdown();
		// wait 10 seconds over and over again until all running jobs have finished
		@SuppressWarnings("unused")
		boolean allTasksCompleted=false;
		try {
			while(!(allTasksCompleted=threadpool.awaitTermination(10, TimeUnit.SECONDS)));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (serverWasOpen){
			setChanged();
			notifyObservers("all the tasks have finished");
			try {
				ServerThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			setChanged();
			notifyObservers("main server thread is done");
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		setChanged();
		notifyObservers("server is safely closed");
	}
	
	
	
	private void loadProperties() {
		this.properties=new Properties("lib/properties.xml");
	}


	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter=presenter;
	}


	
	
}
