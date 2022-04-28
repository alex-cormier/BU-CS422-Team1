package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ShoppingCartController {

    private final String file = "./ShoppingCart.txt";

    @FXML
    private TextField quantTField;
    @FXML
    private TextField priorTField;
    @FXML
    private TextField budgetTField;
    @FXML
    private TextField shopperName;
    @FXML
    private TextField shopperNumber;
    @FXML
    private TextField itemTField;
    @FXML
    private TextField priceTField;
    @FXML
    private Button addQuant;
    @FXML
    private Button subQuant;
    @FXML
    private Button addPrior;
    @FXML
    private Button subPrior;
    @FXML
    private Button userDetails;
    @FXML
    private TextArea cartOutput = new TextArea();
    @FXML
    private TextArea saveItemCart = new TextArea();

    //ShoppingCart itemsPurchasedCart = new ShoppingCart();
    //ShoppingCart itemsMissedCart = new ShoppingCart();

    private String username;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;

    private ObservableList<ShoppingItem> shoppingList;

    Alert invalidItemAlert = new Alert(AlertType.NONE);
    Alert invalidCheckoutAlert = new Alert(AlertType.NONE);
    Alert purchasedItemsAlert = new Alert(AlertType.NONE);

    /*public void initialize() {
        priceTField.setTextFormatter(new TextFormatter<>(change -> {
            if (priceTField.getText().matches("[0-9]+\\.?[0-9]*|\\.[0-9]+") || priceTField.getText().equals("")) {
                return change;
            } else {
                change.setText("");
                change.setRange(
                        change.getRangeStart(),
                        change.getRangeStart()
                );
                return change;
            }
        }));

        //priceTField.setTextFormatter(decFormatter);

        quantTField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().matches("\\d+") || change.getText().equals("")) {
                return change;
            } else {
                change.setText("");
                change.setRange(
                        change.getRangeStart(),
                        change.getRangeStart()
                );
                return change;
            }
        }));

        priorTField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().matches("\\d+") || change.getText().equals("")) {
                return change;
            } else {
                change.setText("");
                change.setRange(
                        change.getRangeStart(),
                        change.getRangeStart()
                );
                return change;
            }
        }));

        //quantTField.setTextFormatter(intFormatter);
        //priorTField.setTextFormatter(intFormatter);
    }*/

    public void initializeCart(String username, ObjectOutputStream out, ObjectInputStream in) {
        this.username = username;
        this.out = out;
        this.in = in;

        try {
            sendData("getItems", username);
            List<ShoppingItem> returnedList = (List<ShoppingItem>) in.readObject();
            shoppingList = FXCollections.observableArrayList(returnedList);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void sendData(String command, Object obj) throws IOException
    {
        out.writeObject(new Request(command, obj));
    }

    @FXML
    public void increaseQuantity() {
        int quantity = Integer.parseInt( quantTField.getText());
        quantity++;
        quantTField.setText( "" + quantity );
    }

    @FXML
    public void decreaseQuantity() {
        int quantity = Integer.parseInt( quantTField.getText() );
        if (quantity == 0) return;
        quantity--;
        quantTField.setText( "" + quantity );
    }

    @FXML
    public void increasePriority() {
        int priority = Integer.parseInt( priorTField.getText() );
        priority++;
        priorTField.setText( "" + priority );
    }

    @FXML
    public void decreasePriority() {
        int priority = Integer.parseInt(priorTField.getText());
        if (priority == 0) return;
        priority--;
        priorTField.setText( "" + priority );
    }

    @FXML
    private void addToCart() {
        try {
            String name = itemTField.getText();
            double price = Double.parseDouble(priceTField.getText());
            int quantity = Integer.parseInt(quantTField.getText());
            int priority = Integer.parseInt(priorTField.getText());
            ShoppingItem newItem = new ShoppingItem(username, name, price, quantity, priority);

            sendData("addItem", newItem);
            Boolean result = (Boolean) in.readObject();
            if (!result)
                throw new IllegalArgumentException("This item is already in your shopping list.");

            itemTField.setText("");
            priceTField.setText("");
            quantTField.setText("0");
            priorTField.setText("0");
        } catch (NumberFormatException e) {
            String warning = "Please enter numeric values for Price, Quantity, and Priority\n" +
                    "Price may be a decimal number";
            invalidItemAlert(warning);
        } catch (IllegalArgumentException e) {
            invalidItemAlert(e.getMessage());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void checkout() {
        try {
            Double budget = Double.parseDouble(budgetTField.getText());

            if (budget < 0.0)
                throw new IllegalArgumentException("Your budget cannot be negative.");
            if (shoppingList.isEmpty())
                throw new IllegalStateException("Your shopping list is empty");

            sendData("goShopping", shoppingList);
            Object[] returnedLists = (Object[]) in.readObject();
            shoppingList = (ObservableList<ShoppingItem>) returnedLists[0];
            List<ShoppingItem> purchasedItems = (List<ShoppingItem>) returnedLists[1];

            if(!purchasedItems.isEmpty()) {
                //table.refresh
                purchasedItemsAlert(purchasedItems);
            }

        } catch (NumberFormatException e) {
            invalidCheckoutAlert("Please enter a numeric value for the Budget.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            invalidCheckoutAlert(e.getMessage());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void invalidItemAlert(String message) {
        invalidItemAlert.setAlertType(AlertType.INFORMATION);
        invalidItemAlert.setTitle("Invalid Item");
        invalidItemAlert.setContentText(message);
        invalidItemAlert.setHeaderText("Invalid Item");
        invalidItemAlert.show();
        System.out.println("Invalid Login");
    }

    private void invalidCheckoutAlert(String message) {
        invalidCheckoutAlert.setAlertType(AlertType.INFORMATION);
        invalidCheckoutAlert.setTitle("Invalid Checkout");
        invalidCheckoutAlert.setContentText(message);
        invalidCheckoutAlert.setHeaderText("Invalid Checkout");
        invalidCheckoutAlert.show();
        System.out.println("Invalid Login");
    }

    private void purchasedItemsAlert(List<ShoppingItem> purchasedItems) {
        String header = "You purchased the following items:\n\n" +
                "Item (quantity) (price)\n\n";
        String footer = "Any items that were not purchased are saved in your shopping list.";

        StringBuilder purchasedMessage = new StringBuilder(header);
        for (ShoppingItem item : purchasedItems)
            purchasedMessage.append("- " + item + " " + "\n");
        purchasedMessage.append("\n" + footer);

        purchasedItemsAlert.setAlertType(AlertType.INFORMATION);
        purchasedItemsAlert.setTitle("Checkout Complete");
        purchasedItemsAlert.setContentText(purchasedMessage.toString());
        purchasedItemsAlert.setHeaderText("Checkout Complete");
        purchasedItemsAlert.show();
        System.out.println("Invalid Login");
    }

    /*public void updateBudget() {
        //Local Variables
        double budget = 0.0;

        try {
            //get and set budget
            budget = Double.parseDouble( budgetTField.getText() );
            itemsPurchasedCart.setBudget(budget);

            if ( budget > 0 ) {

                budgetTField.setText( "" + budget );
                budgetTField.setEditable(false);

            }

        }
        catch ( NumberFormatException e ) {

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Alert!");
            alert.setHeaderText("Please enter a valid budget");
            alert.showAndWait();
        }
    }*/


    /*public void checkOut() {
        //Write file of purchased Items and format output in textfile.
        ShoppingCartFileUtilities.write( itemsPurchasedCart, file);

        itemsPurchasedCart.clear();

        cartOutput.setText( itemsPurchasedCart.createFormattedShoppingCartListGUI() );

    }*/


    /*public void addQuant() {
        //Local Variables
        int quantity = 0;

        quantity = Integer.parseInt( quantTField.getText() );
        //User can increase up to a quantity of ten.
        if ( quantity < 10 ){

            quantity++;
        }

        quantTField.setText( "" + quantity );

    }*/

    /*public void subQuant() {
        //Local Variables
        int quantity = 0;
        //Lower Quantity
        quantity = Integer.parseInt( quantTField.getText() );

        if ( quantity > 0 ) {

            quantity--;
        }

        quantTField.setText( "" + quantity );

    }*/

    /*public void addPrior() {
        //Local Variables
        int priority = 0;

        priority = Integer.parseInt( priorTField.getText() );


        //Increase priority up to ten
        if ( priority < 10 ) {

            priority++;

        }

        priorTField.setText( "" + priority );


    }*/


    /*public void subPrior() {
        //Local Variables
        int priority = 0;

        priority = Integer.parseInt( priorTField.getText() );
        //Decrease priority
        if ( priority > 0 ){

            priority--;
        }

        priorTField.setText( "" + priority );


    }*/

    /*public  void addToCart( ) {

        Item newItem;

        String name  = "";
        double price = 0.0;
        int quantity = 0;
        int priority = 0;


        name     = itemTField.getText();
        price    = Double.parseDouble( priceTField.getText()  );
        quantity = Integer.parseInt( quantTField.getText() );
        priority = Integer.parseInt( priorTField.getText() );


        //create a new item
        newItem = new Item( name, price, quantity, priority );


        try  {

            if(itemsPurchasedCart.getBudget() > newItem.getPrice()) {
                //Add to cart if item is less than budget
                itemsPurchasedCart.addItem(newItem);
                cartOutput.setText("Purchased List Items:              \n " + "\n");
                cartOutput.appendText( itemsPurchasedCart.createFormattedShoppingCartListGUI() );

                itemTField.setText("");
                priceTField.setText("");
                quantTField.setText("0");
                priorTField.setText("0");

            }else {
                //if item is more than budget, add to missed list
                itemsMissedCart.saveCartItem(newItem);
                saveItemCart.setText("Missed List Items:              \n " + "\n");
                saveItemCart.appendText( itemsMissedCart.createFormattedShoppingCartListGUI() );

                itemTField.setText("");
                priceTField.setText("");
                quantTField.setText("0");
                priorTField.setText("0");

            }
        }
        catch ( Exception e ) {

            System.out.println("error adding item to cart");

        }
    }*/

    /*public void userWelcome() {
        //Allow users to enter name and employee number
        String name  =  shopperName.getText();
        final int number = Integer.parseInt( shopperNumber.getText() );


        userDetails.setOnAction(ae->{ shopperName.setText("" + name);});
        userDetails.setOnAction(ae->{ shopperNumber.setText("" + number);});

        //Welcome User
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText(" Welcome " + name + "! \n "+ " \n" + " Rewards number : " + number);
        alert.show();

        shopperName.setEditable(false);
        shopperNumber.setEditable(false);
    }*/
}
