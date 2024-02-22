package main;

import javafx.application.Application;
import javafx.stage.Stage;

public class MinesweeperMain extends Application {
	
	/** The stage for this application. */
	private static Stage stage;

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	public void start(Stage pStage) throws Exception {
		stage = pStage;
		resetMine();
		stage.show();
	}
	
	/**
	 * Resets the mine.
	 */
	public static void resetMine() {
		stage.setScene(new Mine(32, 16, 64, 2).getScene());
	}
}
