package model;

public class ShoppingItem {
    private int id;
    private String username;

    private String name;
    private Double price;
    private Integer quantity;
    private Integer priority;

    public ShoppingItem() {id = -1;}

    public ShoppingItem(int id, String username, String name, Double price, Integer quantity, Integer priority) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.priority = priority;
    }

    public ShoppingItem(String username, String name, Double price, Integer quantity, Integer priority) {
        if (name.length() == 0)
            throw new IllegalArgumentException("Please provide a name for this item");
        if (price < 0.0)
            throw new IllegalArgumentException("Item price cannot be negative");
        if (quantity < 1)
            throw new IllegalArgumentException("Item quantity must be > 0");
        if (priority < 1)
            throw new IllegalArgumentException("Item priority must be > 0");

        this.username = username;

        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.priority = priority;
    }

    public ShoppingItem(ShoppingItem that) {
        this.name = that.name;
        this.price = that.price;
        this.quantity = that.quantity;
        this.priority = that.priority;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
