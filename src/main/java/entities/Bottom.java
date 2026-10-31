package entities;

public class Bottom { // En cupcake bund

    private final int bottomId;
    private final String name;
    private final int price;

    public Bottom(int bottomId, String name, int price) { // Konstruktør
        this.bottomId = bottomId;
        this.name = name;
        this.price = price;
    }

    // Getters
    public int getBottomId() {
        return bottomId;
    }
    public String getName() {
        return name;
    }
    public int getPrice() {
        return price;
    }
}
