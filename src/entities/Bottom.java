package entities; // dataklasser

public class Bottom { // En cupcake bund

    private final int bottomId; // Id
    private final String name; // navn
    private final int price; // Pris kr

    public Bottom(int bottomId, String name, int price) { // Konstruktør
        this.bottomId = bottomId;  // gfem id
        this.name = name; // Gm navn
        this.price = price; // Gem pris
    }

    public int getBottomId() {
        return bottomId;
    } // henter id
    public String getName() {
        return name;
    } // Henter navn
    public int getPrice() {
        return price;
    } // genter pris
}
