package view;


/**
 * <h2>CommonView</h2>
 * This is an calss that implement the view interface which will init the commands from the presenter
 * 
 * 
 */
import java.util.HashMap;
import java.util.Observable;

import presenter.Command;

public abstract class CommonView extends Observable implements View{

	public abstract void setCommands(HashMap<String,Command> newHashCommand);

}