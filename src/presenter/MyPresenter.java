package presenter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import model.Model;
import view.*;

/**
 * The Class MyPresenter.
 */
public class MyPresenter implements Presenter, Observer{

	/** The model. */
	Model model;
	
	/** The view. */
	View view;
	
	/**  hashmap between string to command. */
	HashMap<String, Command> hashCommand;
	
	/**
	 * Instantiates a new MyController.
	 *
	 * @param m the model
	 * @param v the View
	 */
	public MyPresenter(Model m,View v) {
		this.model = m;
		this.view = v;
		this.hashCommand = new HashMap<>();
		InitCommands();
	}
	
	/**
	 * Inits the hashmap commands.
	 */
	private void InitCommands() {
		hashCommand.put("open the server",new Command() {
			
			@Override
			public void setStringCommand(String[] s) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void doCommand() {
				try {
					model.openserver();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		
		hashCommand.put("close the server",new Command() {
			
			@Override
			public void setStringCommand(String[] s) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void doCommand() {
				model.closeserver();
				
				
			}
		});
		
	}
	

	/**
	 * set the model
	 */
	@Override
	public void setModel(Model m) {
		this.model = m;
		
	}

	/**
	 * set the view
	 */
	@Override
	public void setView(View v) {
		this.view = v;
		
	}

	/**
	 * Gets the hash command.
	 *
	 * @return the hash command
	 */
	@Override
	public HashMap<String, Command> getHashCommand() {
		return hashCommand;
	}

	/**
	 * gets string array from model and bring him to View
	 * 
	 */
	@Override
	public void display(String[] s) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * gets object from model, checking for type and inject to view the specific display for the object
	 */
	@Override
	public void update(Observable o, Object arg) {
		String type;
		if (o == model){
			type = arg.getClass().getCanonicalName();
			switch (type) {
			case "java.lang.String":
				view.display((String)arg);
				break;				
			}
		}
		if (o == view){
			type = arg.getClass().getInterfaces()[0].getSimpleName();
			switch (type) {
			case "Command":
				((Command)arg).doCommand();
				break;
			default:
				break;
			}

		}
		
	}
	

}
