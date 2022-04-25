package server;

import model.Request;
import model.ShoppingItem;
import model.User;
import service.ShoppingBudget;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {
    private static ObjectOutputStream nOut;
    private static ObjectInputStream nIn;
    private static Socket ss;
    private static Request request;
    private static Connection c = null;
    private static Statement stmt = null;

    public ClientHandler(Socket clientSocket) throws IOException {
        ss=clientSocket;
        nOut=new ObjectOutputStream(ss.getOutputStream());
        nIn=new ObjectInputStream(ss.getInputStream());
    }

    @Override
    public void run() {
        try{
            dbc();

            while(true)
            {
                System.out.println("[CLIENT HANDLER]: Ready for new command");
                request = (Request) nIn.readObject();
                processRequest(request);
                System.out.println("[CLIENT HANDLER]: Processed a " + request.command + " request");
            }
/*
            ss.close();
            nIn.close();
            nOut.close();
            c.close();*/

        } catch(IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void processRequest(Request r) throws IOException, SQLException, ClassNotFoundException {
        /*
            Behavior:
                Switch statement determines how to process request based on supplied command field
         */
        Object payload=null;
        int id;
        switch (r.command)
        {
            case "addUser":
                writeUser((String[]) r.payload);
                break;
            case "getUser":
                readUser((String[]) r.payload);
                break;
            case "addItem":
                writeItem((ShoppingItem) r.payload);
                break;
            case "updateItem":
                updateItem((ShoppingItem) r.payload);
                break;
            case "getItems":
                readItems((String) r.payload);
            case "deleteItem":
                deleteItem((Integer) r.payload);
                break;
            case "goShopping":
                goShopping((Object[]) r.payload);
                break;
            default:
                break;
        }
    }

    private void writeUser(String[] userData) throws SQLException {
        stmt = c.createStatement();
        String query = "SELECT * FROM users WHERE uname='" + userData[0] + "'";
        ResultSet rs = stmt.executeQuery(query);

        if (!rs.next()) {
            query = "INSERT INTO users (username, password) VALUES ('" + userData[0] +"', '" + userData[1] + "')";
            stmt.executeUpdate(query);
            c.commit();
        } else {
            //THROW EXCEPTION THAT USER EXISTS
        }
    }

    private void readUser(String[] userData) throws SQLException {
        stmt = c.createStatement();
        String query = "SELECT * FROM users WHERE username='" + userData[0] + "' AND password='" + userData[1] + "'";
        ResultSet rs = stmt.executeQuery(query);

        if (rs.next()) {
            //RETURN AUTHORIZATION
        } else {
            //RETURN ERROR
        }
    }

    private void writeItem(ShoppingItem item) throws SQLException {
        String username = item.getUsername();
        String name = item.getName();

        stmt = c.createStatement();
        String query = "SELECT * FROM items WHERE username='" + username + "' AND name='" + name + "'";
        ResultSet rs = stmt.executeQuery(query);

        if (!rs.next()) {
            query = "INSERT INTO items (username, name, price, quantity, priority)" +
                    "VALUES ('" + username + "', '" + name + "', '" + item.getPrice() + "', '" + item.getQuantity() +
                    "', '" + item.getPriority() + "')";
            stmt.executeUpdate(query);
            c.commit();
        } else {
            //THROW EXCEPTION THAT ITEM ALREADY IN USER CART
        }
    }

    private void updateItem(ShoppingItem item) throws SQLException {
        //MAYBE EXCEPTIONS FOR INVALID ITEM DATA

        stmt = c.createStatement();
        String query = "UPDATE items SET price='" + item.getPrice() + "', quantity='" + item.getQuantity() +
                "', priority='" + item.getPriority() + "' WHERE id='" + item.getId() + "'";
        stmt.executeUpdate(query);
    }

    private void readItems(String username) throws SQLException, IOException {
        stmt = c.createStatement();
        String query = "SELECT * FROM items WHERE username='" + username + "'";
        ResultSet rs = stmt.executeQuery(query);

        List<ShoppingItem> items = new ArrayList<>();

        while (rs.next()) {
            ShoppingItem item = new ShoppingItem(rs.getInt("id"), rs.getString("username"),
                    rs.getString("name"), rs.getDouble("price"), rs.getInt("quantity"),
                    rs.getInt("priority"));
            items.add(item);
        }

        nOut.writeObject(items);
    }

    private void deleteItem(Integer id) throws SQLException {
        stmt = c.createStatement();
        String query = "DELETE * FROM items WHERE id='" + id + "'";
        stmt.executeUpdate(query);
    }

    private void goShopping(Object[] shoppingData) throws SQLException, IOException {
        String username = (String) shoppingData[0];
        Double budget = (Double) shoppingData[1];

        stmt = c.createStatement();
        String query = "SELECT * FROM items WHERE username='" + username + "'";
        ResultSet rs = stmt.executeQuery(query);

        Integer updatedId = Integer.valueOf(0), updatedQty = Integer.valueOf(0);
        List<ShoppingItem> items = new ArrayList<>();
        List<ShoppingItem> purchasedItems = ShoppingBudget.goShopping(items, budget, updatedId, updatedQty);

        for (ShoppingItem item : purchasedItems) {
            if (item.getId() == updatedId) {
                query = "UPDATE items SET quantity='" + updatedQty + "' WHERE id='" + updatedId + "'";
                stmt.executeUpdate(query);
                break;
            }
            query = "DELETE * FROM items WHERE id='" + item.getId() + "'";
            stmt.executeUpdate(query);
        }

        nOut.writeObject(purchasedItems);
    }

    private static void dbc()
    {
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/alex.cormier");
            c.setAutoCommit(false);
            System.out.println("[CLIENT HANDLER]: Opened database successfully");

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
    }
}
