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
	private static Scanner kb = new Scanner(System.in);
	private static Socket client;
	private static ObjectOutputStream out;
	private static ObjectInputStream in;

	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
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

	private static void establishConnection()
	{
		try
		{
			client = new Socket("54.89.249.120", 6789);
			out = new ObjectOutputStream(client.getOutputStream());
			in = new ObjectInputStream(client.getInputStream());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
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
