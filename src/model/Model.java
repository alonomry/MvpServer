package model;

import java.io.IOException;
import presenter.Presenter;

/**
 *<h2> Model Interface</h2>
 *This Interface dictates the behavior of our model
 *with the commands :<br>
 *@param openserver
 *@param closeserver

 * @author Alon Tal, Omry Dabush
 *
 */
public interface Model {
	public void setPresenter(Presenter presenter);
	public void openserver() throws IOException;
	public void closeserver();

	
}
