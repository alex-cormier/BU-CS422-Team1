package application;

import application.ShoppingItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShoppingBudget {

    public static Object[] goShopping(List<ShoppingItem> items, Double budget/*,
                                                Integer updatedItemId, Integer updatedItemQuantity*/) {

        List<ShoppingItem> purchasedItems = new ArrayList<>();
        for (ShoppingItem item : items) {
            if (budget >= (item.getPrice() * item.getQuantity())) {
                item.setQuantity(0);
                budget -= item.getPrice();
            } else if (budget >= item.getPrice()) {
                ShoppingItem purchasedItem = new ShoppingItem(item);
                int purchasedQuantity = (int) (budget / item.getPrice());
                int remainingQuantity = item.getQuantity() - purchasedQuantity;
                purchasedItem.setQuantity(purchasedQuantity);
                item.setQuantity(remainingQuantity);
                purchasedItems.add(purchasedItem);
                break;
            } else break;
        }
        Object[] shoppingResults = new Object[] {items, purchasedItems};
        return shoppingResults;

        /*List<ShoppingItem> purchasedItems = new ArrayList<>();
        Iterator<ShoppingItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            ShoppingItem item = iterator.next();
            if (budget >= (item.getPrice() * item.getQuantity())) {
                purchasedItems.add(item);
                budget -= item.getPrice() * item.getQuantity();
                iterator.remove();
            } else if (budget >= item.getPrice()) {
                ShoppingItem purchasedItem = new ShoppingItem(item);
                updatedItemId = purchasedItem.getId();

                int purchasedQuantity = (int) (budget / item.getPrice());
                updatedItemQuantity = item.getQuantity() - purchasedQuantity;

                purchasedItem.setQuantity(purchasedQuantity);
                item.setQuantity(updatedItemQuantity);

                purchasedItems.add(purchasedItem);
                break;
            } else break;
        }
        Object[] shoppingResults = new Object[] {purchasedItems, updatedItemId, updatedItemQuantity};
        return shoppingResults;*/
        //return purchasedItems;

        /*//Lists track which items have been purchased
        //and which can be fully removed from the shopping list
        List<ShoppingItem> purchasedItems = new ArrayList<>();

        items.sort(null);
        for (ShoppingItem item : items) {
            //if total value of an item is less than the budget
            //  purchase max quantity of item
            if (budget >= (item.getPrice() * item.getQuantity())) {
                purchasedItems.add(item);
                budget -= item.getPrice() * item.getQuantity();
            }
            //if you cannot afford max quantity of an item
            //  purchase as many as possible
            else if (budget >= item.getPrice()) {
                ShoppingItem purchasedItem = new ShoppingItem(item);
                updatedItemId = item.getId();

                int purchasedQuantity = (int) (budget / item.getPrice());
                updatedItemQuantity = item.getQuantity() - purchasedQuantity;

                purchasedItem.setQuantity(purchasedQuantity);
                purchasedItems.add(purchasedItem);

                break;
            } else break; //stop looping if you cant afford even 1 item
        }
        return purchasedItems;*/
    }
}
