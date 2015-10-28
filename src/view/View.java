package view;

/**
 * 
 * <h2>View-Interface</h1>
 * each view option will extand this interface and implements:<br>
 * 1)display method<br>
 * 2)start method
 *
 */
public interface View {

	public void start();
	public void display (String s);
	
}