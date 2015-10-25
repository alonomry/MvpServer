package presenter;

import java.util.HashMap;

import model.Model;
import view.View;


/**
 * @author alon tal,omry dabush
 * <h2>Controller-Interface</h2>
 * control Model and View in MVC architecture
 */

public interface Presenter {
	
	/**
	 * Sets the model.
	 *
	 * @param m the new model
	 */
	void setModel (Model m);
	
	/**
	 * Sets the view.
	 *
	 * @param v the new view
	 */
	void setView (View v);
	
	/**
	 * Display.
	 *
	 * @param s the string that need to by displayed
	 */
	void display (String[] s);

	HashMap<String, Command> getHashCommand();
	

}
