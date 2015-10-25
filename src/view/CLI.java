package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import presenter.Command;

public class CLI extends CommonView {

	private BufferedReader in;
	private PrintWriter out;
	HashMap<String, Command> hashCommand;
	
	public CLI(BufferedReader in,PrintWriter out) {
		this.in = in;
		this.out = out;
	}
	
	@Override
	public void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String line;
				out.println("----- Server - Command Line -----");
				out.println("Hello and welcome to the server side. \ntype \"open the server\" to open it\ntype \"stop the server\" to stop it");
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
								line = in.readLine();
							}
							else
								System.out.println("Worng Command");
					}
					setChanged();
					notifyObservers(hashCommand.get("exit"));
					out.println("--- EXIT ---");
				} catch (IOException e) {
					setChanged();
					notifyObservers("Invalid command");
				}
				  out.close();
			}	
		}).start();
	}

	@Override
	public void display(String s) {
		out.println(s);
		out.flush();
	}
	
	
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