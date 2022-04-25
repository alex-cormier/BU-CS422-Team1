package application;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import java.util.Collections;


public class ShoppingCart {

	// Class Constants
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


	// Class Variables -- state of the object

	private ArrayList<Item> shoppingCart;



	private double budget;
	private double totalCost;

	// Constructor -- set the state of the object

	public ShoppingCart(){	

		this.shoppingCart = new ArrayList<Item>(); 

		this.budget = 0.0;

		this.totalCost = 0.0;
	}  

	public ShoppingCart( double budget ){	

		this.shoppingCart = new ArrayList<Item>(); 
		
		this.budget = budget;

		this.totalCost = 0.0;
	}  

	// Accessors - getters - return the state

	public double getBudget(){

		return this.budget;
	}

	public double getTotalCost(){

		return this.totalCost;
	}

	public int getNumberItems(){

		return shoppingCart.size();
	}

	public String createFormattedShoppingCartListTextFile(){
		// Local Variables

		StringBuffer sb = new StringBuffer();

		String line = null;

		// Iterate thru list to format every item in the list

		for ( Item item : shoppingCart ){

			// Format the state of the object

			line = String.format( "%10s, %10.2f %2d, %2d%n", 
					item.getName(), 
					item.getPrice(), 
					item.getQuantity(), 
					item.getPriority() );

			sb.append( line );

		}
		return sb.toString();
	}


	public String createFormattedShoppingCartListGUI(){
		// Local Variables

		StringBuffer sb = new StringBuffer();

		// Add formatted header

		sb.append( displayFormattedHeader() );
		sb.append( "\n" );

		// Iterate thru list to format every item in the list

		for ( Item item : shoppingCart ){

			sb.append( displayFormattedItem( item ) );
		}

		// Now add total cost of items in shopping cart

		sb.append( displayFormattedTotalCost() );

		return sb.toString();
	}

	// Mutators/Transformers -- setters -- change the state

	public void setBudget( double budget ) {

		this.budget = budget;
	}

	public void addItem( Item newItem ) throws Exception{

		// Local Variables
		double costItem = 0.0;

		// Calculate cost of this item

		costItem = newItem.calculateTotalCost();

		if ( budget - costItem > 0.00 && !duplicateItemCheck(newItem))


		{
			shoppingCart.add( newItem );

			totalCost += costItem;
			budget -= costItem;
		}

		else if(!duplicateItemCheck(newItem)){

			//if i remove this line then it doesnt work and will still add another item that exceeds budget
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Alert!");
			alert.setHeaderText("Item exceeds budget");
			alert.showAndWait();
		}
		Collections.sort( shoppingCart ); 
	}	
	
	
	
	public void saveCartItem( Item newItem ) throws Exception{
		double costItem = 0.0;

		costItem = newItem.calculateTotalCost();
		if ( !duplicateItemCheck(newItem))


		{
			shoppingCart.add( newItem );
			totalCost += costItem;
		}

		Collections.sort( shoppingCart ); 
	}	


	public boolean duplicateItemCheck(Item newItem) {

		boolean duplicate = false;

		for ( Item item : shoppingCart ) {

			if(!item.equals(newItem)) {

				duplicate = false;

			}else {

				duplicate = true;

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Alert!");
				alert.setHeaderText("Item is already in cart");
				alert.showAndWait();
				break;
			}

		}
		return duplicate;
	}
	public void clear(){

		shoppingCart.clear();

		totalCost = 0.0;
	}

	public String displayFormattedHeader()
	{
		// Local Variables

		String header = null;

		// Format the state of the object

		header = String.format( "%-20s %13s    %8s    %8s%n", "Name", "Price", "Quantity", "Priority" );

		return header;
	}

	private String displayFormattedItem( Item item ) {
		// Local Variables

		String line = null;

		// Format the state of the object

		line = String.format( "%-20s %13.2f    %8d    %8d%n", 
				item.getName(), 
				item.getPrice(), 
				item.getQuantity(), 
				item.getPriority() );

		// Return formatted line

		return line;
	}

	private String displayFormattedTotalCost()
	{
		// Local Variables

		String formatStr = null;

		// Format total cost line

		formatStr = String.format( "%nTotal cost of items in shopping cart: $%10.2f%n", this.totalCost );

		return formatStr; 

	}
}
