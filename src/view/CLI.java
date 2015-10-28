package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import presenter.Command;

/**
 * <h2>CLI SERVER</h2>
 * this class will perform as the server command line to open/close the server with the proper commands</br>
 * @param 	private BufferedReader in;
 * @param private PrintWriter out;
 * @param HashMap<String, Command> hashCommand;
 * 
 * @author Alon Tal & Omry Dabush
 *
 */

public class CLI extends CommonView {

	private BufferedReader in;
	private PrintWriter out;
	HashMap<String, Command> hashCommand;
	
	public CLI(BufferedReader in,PrintWriter out) {
		this.in = in;
		this.out = out;
	}
	
	
	/**
	 * <h2>Srtat Method</h2>
	 * this method will start the main server thread that will wait until</br>
	 * until the "close the server is entered"
	 */
	@Override
	public void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String line;
				out.println("----- Server - Command Line -----");
				out.println("Welcome to the server side. \ntype \"open the server\" to open it\ntype \"close the server\" to stop it");
				out.flush();
				  try {
					while(!(line = in.readLine()).equals("close the server")){
							String theCommand = commandRecognizer(line);
							String[] stringCommand = line.split(" ");
							if (!(theCommand == null)){
								Command command = hashCommand.get(theCommand);
								command.setStringCommand(stringCommand);
								setChanged();
								notifyObservers(command);
								out.flush();
							}
							else
								System.out.println("Worng Command");
					}
					setChanged();
					notifyObservers(hashCommand.get("close the server"));
					out.println("--- EXIT ---");
				} catch (IOException e) {
					setChanged();
					notifyObservers("Invalid command");
				}
				  out.close();
			}	
		}).start();
	}
/**
 * <h2>Printing a string</h2>
 * 
 */
	@Override
	public void display(String s) {
		out.println(s);
		out.flush();
	}
	
	/**
	 * <h2>command Recognizer</h2>
	 * This method will recognize the command that was entered by the user<br>
	 * from a continuous String
	 * 
	 * @param s
	 * @return
	 */
	public String commandRecognizer (String s){
		String[] stringCommand = s.split(" ");
		switch (stringCommand[0]) {
		case "open":
			if (s.contains("open the server"))
				return "open the server";
		default:
			break;
		}
		return null;		
	}

	@Override
	public void setCommands(HashMap<String, Command> newHashCommand) {
		this.hashCommand = new HashMap<>(newHashCommand);		
	}

	
}