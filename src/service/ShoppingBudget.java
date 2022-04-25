package service;

import model.ShoppingItem;

import java.util.ArrayList;
import java.util.List;

public class ShoppingBudget {

    public static List<ShoppingItem> goShopping(List<ShoppingItem> items, Double budget,
                                                Integer updatedItemId, Integer updatedItemQuantity) {
        //THROW EXCEPTION FOR NEGATIVE BUDGET?

        //Lists track which items have been purchased
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
        return purchasedItems;
    }
}
