package entities;

public class Topping { // En cupcake top

    private final int toppingId; // Id
    private final String name; // Navn
    private final int price; // Pris i kr

    public Topping(int toppingId, String name, int price) { // Konstruktør
        this.toppingId = toppingId; // gem id
        this.name = name; // gem navn
        this.price = price; // gem pris
    }

    public int getToppingId() {
        return toppingId;
    } // Henter id
    public String getName() {
        return name;
    } // Henter navn
    public int getPrice() {
        return price;
    } // Henter pris
}