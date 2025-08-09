package productinventorysystem1;

public class Product {
    private int id;
    private String name;
    private int quantity;
    private double price;
    private String issueDate;
    private String expiryDate;

    public Product(int id, String name, int quantity, double price, String issueDate, String expiryDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public String getIssueDate() { return issueDate; }
    public String getExpiryDate() { return expiryDate; }
}