package application;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;



public class Main extends Application {
	//private static User user;


	@Override
	public void start(Stage primaryStage) {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("LoginPage.fxml"));
			Parent root = loader.load();
			SceneController controller = loader.getController();
			controller.establishConnection();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Cloud Based Shopping Application");
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		//establishConnection();
		launch(args);

	}


//	private static void sendData(String command, Object obj) throws IOException 
//	{
//		out.writeObject(new Request(command, obj));
//	}
//
//	private static void sendData(Request request) throws IOException 
//	{
//		out.writeObject(request);
//	}
}