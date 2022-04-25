package application;

import java.util.ArrayList;
import java.util.Collections;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;


public class MainController {


	
	private final String file = "./ShoppingCart.txt";
	
	@FXML 
	Button savedItemButton = null;
	@FXML 
	Button addQuant = null;
	@FXML 
	Button subQuant = null;
	@FXML 
	Button addPrior = null;
	@FXML 
	Button subPrior = null;
	@FXML 
	TextField quantTField = null;
	@FXML
	TextField priorTField = null;
	@FXML
	TextField budgetTField = null;
	@FXML
	TextField priceTField = null;
	@FXML
	TextField itemTField = null;
	@FXML
	TextArea cartOutput = new TextArea();
	
	@FXML
	TextArea saveItemCart = new TextArea();

	ShoppingCart cart = new ShoppingCart();
	ShoppingCart cart2 = new ShoppingCart();



	public void updateBudget() {

		double budget = 0.0;


		try

		{
			budget = Double.parseDouble( budgetTField.getText() );
			cart.setBudget(budget);


			if ( budget > 0 )
			{

				budgetTField.setText( "" + budget );
				budgetTField.setEditable(false);

			}

		}
		catch ( NumberFormatException e )
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Alert!");
			alert.setHeaderText("Please enter a valid budget");
			alert.showAndWait();
		}
	}
	

	public void checkOut() {

		ShoppingCartFileUtilities.write( cart, file);
		

		cart.clear();
		
		
		cartOutput.setText( cart.createFormattedShoppingCartListGUI() );
	
	}


	public void addQuant() {
		int quantity = 0;

		quantity = Integer.parseInt( quantTField.getText() );



		if ( quantity < 10 )
		{

			quantity++;
		}


		quantTField.setText( "" + quantity );
		System.out.println("add quant clicked");

	}
	public void subQuant() {
		
		int quantity = 0;


		quantity = Integer.parseInt( quantTField.getText() );


		if ( quantity > 0 )
		{
			
			quantity--;
		}

	

		quantTField.setText( "" + quantity );
		System.out.println("sub quant clicked");

	}

	public void addPrior() {
		
		int priority = 0;

		priority = Integer.parseInt( priorTField.getText() );

		

		if ( priority < 10 )
		{
			
			priority++;
		}

	

		priorTField.setText( "" + priority );
		System.out.println("add prior clicked");

	}

	
	public void subPrior() {
		int priority = 0;

	

		priority = Integer.parseInt( priorTField.getText() );


		if ( priority > 0 )
		{
		
			priority--;
		}

		priorTField.setText( "" + priority );
		System.out.println("sub prior clicked");

	}


	public  void addToCart( ) {
		Item newItem = null;

		String name  = null;
		double price = 0.0;
		int quantity = 0;
		int priority = 0;


		name     = itemTField.getText();
		price    = Double.parseDouble( priceTField.getText()  );
		quantity = Integer.parseInt( quantTField.getText() );
		priority = Integer.parseInt( priorTField.getText() );



		newItem = new Item( name, price, quantity, priority );


		try 
		{
			cart.addItem(newItem);
			cartOutput.setText( " " );
			cartOutput.appendText( cart.createFormattedShoppingCartListGUI() );

			itemTField.setText("");
			priceTField.setText("");
			quantTField.setText("0");
			priorTField.setText("0");



		} 
		catch ( Exception e )
		{


			itemTField.setText("");
			priceTField.setText("");
			quantTField.setText("0");
			priorTField.setText("0");

		}
	}
		public  void saveItem( ) {
			Item newItem = null;

			String name  = null;
			double price = 0.0;
			int quantity = 0;
			int priority = 0;


			name     = itemTField.getText();
			price    = Double.parseDouble( priceTField.getText()  );
			quantity = Integer.parseInt( quantTField.getText() );
			priority = Integer.parseInt( priorTField.getText() );



			newItem = new Item( name, price, quantity, priority );
			
			try 
			{
				cart2.saveCartItem(newItem);
				saveItemCart.setText( " " );
				saveItemCart.appendText( cart2.createFormattedShoppingCartListGUI() );


				itemTField.setText("");
				priceTField.setText("");
				quantTField.setText("0");
				priorTField.setText("0");



			} 
			catch ( Exception e )
			{


				itemTField.setText("");
				priceTField.setText("");
				quantTField.setText("0");
				priorTField.setText("0");

			
			}
		
	}
	
}







