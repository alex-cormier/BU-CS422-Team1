package application;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

public class SceneController {

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
			Pattern.CASE_INSENSITIVE);
	Alert inalidEmail = new Alert(AlertType.NONE);
	Alert notMatchingPassword = new Alert(AlertType.NONE);
	Alert passwordtooShort = new Alert(AlertType.NONE);

	// LoginPageFXML
	@FXML
	TextField username = null;
	@FXML
	PasswordField password = null;

	// NewUserFXML
	@FXML
	TextField newuserEmail = null;
	@FXML
	PasswordField newuserPassword = null;
	@FXML
	PasswordField newuserConfirmPassword = null;

	// Logic to grab info off of the login page and then send it to the datebase
	public void submitLoginData() {

		String user_name;
		String tempPassword;

		// Will be sending these strings to the database to check for login
		user_name = username.getText();
		tempPassword = password.getText();

		// Check for valid email alert
		if (validate(user_name) && tempPassword.length() >= 6) {

			// Replace this with logic to check database for successful login
			// **********************

			// Send user_name to database
			// Send tempPassword to database

			// *********************************************************************************************
			System.out.println("Sending to database...: \n");
			System.out.println("Username: " + user_name + "\n" + "password: " + tempPassword);
		}
		if (!validate(user_name)) {
			// if the email entered is not valid, throw an alert and do not add to data base
			invalidEmailAlert();
			// if the password is too short
		}
		if (tempPassword.length() < 6) {
			passwordTooShortAlert();

		}
	}

	// Logic to submit new users to the database
	public void submitNewUserData() {

		String user_name;
		String tempPassword;
		String tempconfirmpassword;

		// These will be the strings we send to the database
		user_name = newuserEmail.getText();
		tempPassword = newuserPassword.getText();
		tempconfirmpassword = newuserConfirmPassword.getText();

		// if the passwords match, and it is a valid email, and the password is atleast 6 characters add to database
		if (tempPassword.equals(tempconfirmpassword) && validate(user_name) && tempPassword.length() >= 6) {

			// add logic to add to database here
			// ********************************************

			// ********************************************************************************

			// print the new user
			System.out.println("Sucess! \n New user being added to database");
			System.out.println("" + "       Username: " + user_name + "\n" + "       password: " + tempPassword + "\n"
					+ "confirmpassword: " + tempconfirmpassword);
		}
		///Alerts////////////
		
		// Throw alert if passwords arent the same
		if (!tempPassword.equals(tempconfirmpassword)) {
			notMatchingPasswordsAlert();
		}

		// Throw alert if invalid email
		if (!validate(user_name)) {
			invalidEmailAlert();

		}
		//Throw alert if password is less than 6 characters
		if(tempPassword.length() < 6) {
			passwordTooShortAlert();
		}

	}

	// Logic to switch scenes//////////////////////////////////////////////////////////////////////////////////////////
	public void switchToScene1(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	public void switchToScene2(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("CreateNewUser.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	

	// Email Verification Regex////////////////////////////////////////////////////////////////////////////////////////
	public static boolean validate(String emailStr) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		return matcher.find();
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
	
	//Alerts///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Password Too short alert
	public void passwordTooShortAlert() {
		passwordtooShort.setAlertType(AlertType.INFORMATION);
		passwordtooShort.setTitle("Password too short");
		passwordtooShort.setHeaderText("Password too short");
		passwordtooShort.setContentText("Password too short");
		passwordtooShort.show();
		System.out.println("Password too short");

	}

	// Invalid Email Alert
	public void invalidEmailAlert() {
		// if the email entered is not valid, throw an alert and do not add to data base
		inalidEmail.setAlertType(AlertType.INFORMATION);
		inalidEmail.setTitle("Not a valid email");
		inalidEmail.setHeaderText("Something went wrong");
		inalidEmail.setContentText("Please enter a valid email");
		inalidEmail.show();
		System.out.println("Invalid email");
	}

	// Not matching Password alert
	public void notMatchingPasswordsAlert() {
		notMatchingPassword.setAlertType(AlertType.INFORMATION);
		notMatchingPassword.setTitle("Passwords do not match");
		notMatchingPassword.setContentText("Something went wrong");
		notMatchingPassword.setHeaderText("Passwords do not match");
		notMatchingPassword.show();
		System.out.println("Passwords do not match");

	}

}
