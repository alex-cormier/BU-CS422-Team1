package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	public static void main(String[] args) {

		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws IOException {
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("hellofx.fxml"));

		//MainController controller = loader.getController();
		Parent root = loader.load();

		primaryStage.setTitle("Welcome To Natalie's Shop!");
		primaryStage.setScene(new Scene(root ));
		primaryStage.show();
	}
}