package entities;

public class OrderLine {

    private final Bottom bottom; // Valgt bund
    private final Topping topping; // Valgt top
    private final int quantity; // Antal
    private final int unitPrice; // Stykpris

    public OrderLine(Bottom bottom, Topping topping, int quantity) { // Ny linje fra kurven
        this(bottom, topping, quantity, bottom.getPrice() + topping.getPrice()); // Pris = bund + top
    }

    public OrderLine(Bottom bottom, Topping topping, int quantity, int unitPrice) { // Linje fra databasen
        this.bottom = bottom; // Gem bund
        this.topping = topping; // Gem top
        this.quantity = quantity; // Gem antal
        this.unitPrice = unitPrice; // Gem stykpris
    }

    public Bottom getBottom() {
        return bottom;
    } // henter bund
    public Topping getTopping() {
        return topping;
    } // Henter top
    public int getQuantity() {
        return quantity;
    } // henter antal
    public int getUnitPrice() {
        return unitPrice;
    } // Henter stykpris

    public int getLinePrice() { // Pris for hele lartet
        return unitPrice * quantity; // Stykpris gange per antal
    }
}