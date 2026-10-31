package entities;

public class Topping { // En cupcake top

    private final int toppingId;
    private final String name;
    private final int price;

    public Topping(int toppingId, String name, int price) { // Konstruktør
        this.toppingId = toppingId;
        this.name = name;
        this.price = price;
    }

    // Getters
    public int getToppingId() {
        return toppingId;
    }
    public String getName() {
        return name;
    }
    public int getPrice() {
        return price;
    }
}