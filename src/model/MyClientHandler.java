package model;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import algorithms.demo.Maze3dAdapter;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;
import algorithms.search.AirDistance;
import algorithms.search.Astar;
import algorithms.search.Bfs;
import algorithms.search.ManhattanDistance;
import algorithms.search.Searcher;
import algorithms.search.Solution;

/**
 * <h2>MyClientHandler</h2>
 * this class will handle the request from the client side <br>
 * will perform the calculation and will send it back to the server
 * @param ExecutorService
 * @param ObjectOutputStream 
 * @param ObjectInputStream
 * @param HashMap<Maze3d, Solution<Position>>
 * 
 * @author Omryno1
 *
 */

public class MyClientHandler implements ClinetHandler {

//	private HashMap<Maze3d, Solution<Position>> mazeSolution;
	private ExecutorService threadpool;
	private ObjectOutputStream messageToClient;
	private ObjectInputStream messageFromClient;
	private HashMap<Maze3d, Solution<Position>> MazeToSolution;
	
	/**
	 * C'tor
	 */
	public MyClientHandler() {
			loadMazeToSolution();
		if(this.MazeToSolution==null)
			this.MazeToSolution = new HashMap<Maze3d, Solution<Position>>();
		this.threadpool = Executors.newFixedThreadPool(3);
	}

	/**
	 * this method will recognize exactly what the client has requested and will perform the wanted task
	 * 
	 * 
	 */
	@Override
	public void handleClient(InputStream inFromClient, OutputStream outToClient) {
			// Get client's Inputstream from client which contain maze and algorithm
			try {
				messageFromClient = new ObjectInputStream(inFromClient);
				@SuppressWarnings("unchecked")
				ArrayList<Object> problem = (ArrayList<Object>)messageFromClient.readObject();
				
				messageToClient = new ObjectOutputStream(outToClient);
				
				if (problem.get(0).equals("solve")) {
					handleSolution(problem);
				}
				else if (problem.get(0).equals("get solution")){
					Maze3d m = (Maze3d) problem.get(1);
					if(MazeToSolution.containsKey(m)){
						Solution<Position> solution = MazeToSolution.get(m);
						messageToClient.writeObject(solution);
					}
				}
				else
				{
					System.out.println("Invalid problem");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}	
	}
	/**
	 * this method will get a maze and will solve it, afterwards the solution will be send to the server
	 * 
	 * 
	 * @param list
	 */
	private void handleSolution(ArrayList<Object> list) {
		
		String algorithm = (String) list.get(1);
		Maze3d m = (Maze3d) list.get(2);
		// Create ObjectOutputStream to answer the client throw
		// Create solution for the maze in different thread
		
		Future<Solution<Position>> future = threadpool.submit(new Callable<Solution<Position>>(){	
			@Override
			public Solution<Position> call() throws Exception {
				Solution<Position> sol=new Solution<Position>();
				Maze3dAdapter MA=new Maze3dAdapter(m, 10);//cost 10
				
					try {
							if(list.size()==3 && MazeToSolution.containsKey(m)){	
									return MazeToSolution.get(m);
								}
							 if(list.size()==3||list.size()==4){
								if(m!=null)//get the array list for specific name
									switch (algorithm) {
										case "Astar-manhattan":
											Searcher<Position> AstarsearcherManhattan=new Astar<Position>(new ManhattanDistance());
											if (list.size() == 3){
												sol = AstarsearcherManhattan.search(MA);
												MazeToSolution.put(m,sol);
												return sol;
											}
											else if (list.size() == 4){
												Position newEnter=(Position)list.get(3);
												sol = AstarsearcherManhattan.search(MA,newEnter);
												MazeToSolution.put(m,sol);
												return sol;
											}
										case "Astar-air":											
											Searcher<Position> AstarsearcherAir=new Astar<Position>(new AirDistance());
											if (list.size() == 3){
												sol = AstarsearcherAir.search(MA);
												MazeToSolution.put(m,sol);
												return sol;
											}
											else if (list.size() == 4){
												Position newEnter=(Position)list.get(3);
												sol = AstarsearcherAir.search(MA,newEnter);
												MazeToSolution.put(m,sol);
												return sol;
											}
										case "Bfs":
											Searcher<Position> searcher=new Bfs<>();
											if (list.size() == 3){
												sol= searcher.search(MA);
												MazeToSolution.put(m,sol);
												return sol;
											}
											else if (list.size() == 4){
												Position newEnter=(Position)list.get(3);
												sol= searcher.search(MA,newEnter);
												MazeToSolution.put(m,sol);
												return sol;
											}
										default:
											throw new IOException("not a valid algorithm");
									}
								else throw new IOException("maze not found");
							}	
						else throw new IOException("Not a Valid command");
		
					} catch (Exception e) {
						throw new IOException(e.getMessage());
					}
				}
		});
		
		
		Solution<Position> solution;
		try {
			solution = future.get();  // Add the solution to the HashMaps
			MazeToSolution.put(m, solution);
			
			
			messageToClient.writeObject(solution);  // Send solution to client
			messageToClient.flush();
			
			
			messageFromClient.close();  // Close streams
			messageToClient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Load the saved solutions from the zip file
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void loadMazeToSolution (){
		File file = new File("lib/mazes.zip");
		if (file.exists()){
			try {
				ObjectInputStream in = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file)));
				try {
					MazeToSolution = (HashMap<Maze3d, Solution<Position>>)in.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				in.close();
			} catch (IOException e) {
					System.err.println("File Does Not Exist");
			
			}
		}
		else 
			MazeToSolution =  new HashMap<Maze3d,Solution<Position>>();
	} 
	
	/**
	 * Save out solutions in zip file
	 * @throws IOException
	 */
	public void saveMazes()  {
		try{
			// Write all the data from the HashMap to the specified file
		   	if (!MazeToSolution.isEmpty()){
						ObjectOutputStream out = new ObjectOutputStream(
								   new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(
								   new File("lib/mazes.zip")))));
			    		out.writeObject(MazeToSolution);
			    		out.close();
		    	}
		}catch (IOException e){
			System.out.println("Error Saving Mazes");
		}
	}
	
	public void exitClient(){
		threadpool.shutdown();
		@SuppressWarnings("unused")
		Boolean allTasksCompleted=true;
		try {
			while(!(allTasksCompleted=threadpool.awaitTermination(5, TimeUnit.SECONDS)));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
