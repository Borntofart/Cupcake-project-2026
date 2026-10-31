package entities;

public class OrderLine { // En linje: bund + top + antal

    private final Bottom bottom;
    private final Topping topping;
    private final int quantity;
    private final int unitPrice;

    public OrderLine(Bottom bottom, Topping topping, int quantity) { // Ny linje fra kurven
        this(bottom, topping, quantity, bottom.getPrice() + topping.getPrice());
    }

    public OrderLine(Bottom bottom, Topping topping, int quantity, int unitPrice) { // Linje fra databasen
        this.bottom = bottom;
        this.topping = topping;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // Getters
    public Bottom getBottom() {
        return bottom;
    }
    public Topping getTopping() {
        return topping;
    }
    public int getQuantity() {
        return quantity;
    }
    public int getUnitPrice() {
        return unitPrice;
    }

    public int getLinePrice() { // Pris for hele lartet
        return unitPrice * quantity;
    }
}