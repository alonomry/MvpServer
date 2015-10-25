package view;

import java.util.HashMap;
import java.util.Observable;

import presenter.Command;

public abstract class CommonView extends Observable implements View{

	public abstract void setCommands(HashMap<String,Command> newHashCommand);

}