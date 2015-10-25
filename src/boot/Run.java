package boot;

import view.CommonView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import model.*;
import presenter.MyPresenter;
import presenter.Presenter;
import view.*;


public class Run {

	public static void main(String[] args) {
		CommonView view = new CLI(new BufferedReader(new InputStreamReader(System.in)), new PrintWriter(System.out));
		Mymodel model=new Mymodel();
		MyPresenter presenter = new MyPresenter(model,view);
		model.setPresenter(presenter);
		view.setCommands(presenter.getHashCommand());
		model.addObserver(presenter);
		view.addObserver(presenter);
		view.start();

	}

}
