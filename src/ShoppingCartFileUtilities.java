package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ShoppingCartFileUtilities {    
	
    public static void read( ShoppingCart shoppingCart, String filename ){    
    	// Local Variables
    	Scanner sc = null;
    	
    	String record = null;
    	String fields[] = null;

    	Item item = null;
    	
        String name  = null;
        double price = 0.0;
        int priority = 0;
        int quantity = 0;
    	
    	try {
    		
            sc = new Scanner( new File( filename ) );

            while ( sc.hasNextLine() ){
            	// Read in line of text file
            	record = sc.nextLine().trim();
            	
            	System.out.println( record );
            	
            	if ( record.length() > 0 ){
	            	// Parse line to extract item information
	            	fields = record.split( "," );
	            	
	                name     = fields[ 0 ].trim();
	                price    = Double.parseDouble( fields[ 1 ].trim() );
	                quantity = Integer.parseInt( fields[ 2 ].trim() );
	                priority = Integer.parseInt( fields[ 3 ].trim() );
                
	                // Instantiate item
                    item = new Item( name, price, quantity, priority );
                    // Add item to shopping cart
                    
                    try {
                    	
						shoppingCart.addItem( item );
						
					} 
                    catch (Exception e) {
                    	
                    	System.out.println( e.getMessage() );
					}
            	}
            }
            
            sc.close();
        }
    	catch ( FileNotFoundException e ){
    		
    		Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Alert!");
			alert.setHeaderText("Filename" + filename + "does not exist");
			alert.showAndWait();
    	}
    }
    
    public static void write( ShoppingCart shoppingCart, String filename ){
    	// Local Variables
        PrintWriter writer = null;
        
        try {
        	
            writer = new PrintWriter( filename );
        
            writer.write( shoppingCart.createFormattedShoppingCartListTextFile() );
            writer.close();
   
        } 
        catch ( Exception e ) {
        	
        	Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Alert!");
			alert.setHeaderText("Exception error writing shopping to filename: " + filename + "!");
			alert.showAndWait();
    		
        }
    }
}