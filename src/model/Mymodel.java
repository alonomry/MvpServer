package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import presenter.Presenter;
import presenter.Properties;

public class Mymodel extends Observable implements Model{

	int port;
	ServerSocket server;
	ClinetHandler clinetHandler;
	int numOfClients;
	ExecutorService threadpool;
	volatile boolean stop;
	Thread mainServerThread;
	int clientsHandled=0;
	Properties properties;
	Presenter presenter;
	
	public Mymodel() {
		loadProperties();
		threadpool=Executors.newFixedThreadPool(Integer.parseInt(properties.getNumOfClients()));
	}
	
	
	@Override
	public void openserver() throws IOException{
		server=new ServerSocket(Integer.parseInt(properties.getPort()));
		server.setSoTimeout(10*1000);
		
		mainServerThread=new Thread(new Runnable() {			
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
				notifyObservers("done accepting new clients.");
			} // end of the mainServerThread task
		});
		
		mainServerThread.start();
		
	}
	
	
	
	
	@Override
	public void closeserver(){
		/*this.stop=true;	
		clinetHandler.saveToZip();
		// do not execute jobs in queue, continue to execute running threads
		setChanged();
		notifyObservers("shutting down");
		threadpool.shutdown();
		// wait 10 seconds over and over again until all running jobs have finished
		boolean allTasksCompleted=false;
		while(!(allTasksCompleted=threadpool.awaitTermination(10, TimeUnit.SECONDS)));
		setChanged();
		notifyObservers("all the tasks have finished");
		mainServerThread.join();		
		setChanged();
		notifyObservers("main server thread is done");
		server.close();
		setChanged();
		notifyObservers("server is safely closed");*/
	}
	
	
	
	
	private void loadProperties() {
		this.properties=new Properties("lib/properties.xml");
	}


	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter=presenter;
	}


	
	
}
