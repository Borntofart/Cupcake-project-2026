package entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderTotalTest { // Tester at totalen for kurv og ordre regnes rigtigt

    private final Bottom vanilla = new Bottom(1, "Vanilje", 15);
    private final Topping lemon = new Topping(1, "Citron", 22);
    private final Topping blueberry = new Topping(2, "Blåbær", 15);

    @Test
    void lineTotalIsBottomPlusToppingTimesQuantity() {
        OrderLine line = new OrderLine(vanilla, lemon, 2);
        assertEquals(37, line.getUnitPrice());
        assertEquals(74, line.getLinePrice());
    }

    @Test
    void cartTotalAddsAllLines() {
        Cart cart = new Cart();
        cart.add(new OrderLine(vanilla, lemon, 2));     // 2 x 37 = 74
        cart.add(new OrderLine(vanilla, blueberry, 1)); // 1 x 30 = 30
        assertEquals(104, cart.getTotal());
        assertEquals(3, cart.getCount());
    }

    @Test
    void emptyCartTotalIsZero() {
        assertEquals(0, new Cart().getTotal());
    }

    @Test
    void cartTotalAfterRemove() {
        Cart cart = new Cart();
        cart.add(new OrderLine(vanilla, lemon, 2));
        cart.add(new OrderLine(vanilla, blueberry, 1));
        cart.remove(0);
        assertEquals(30, cart.getTotal());
    }

    @Test
    void orderTotalUsesSavedUnitPrice() {
        // Prisen fra databasen bruges, selvom bund og top koster noget andet i dag
        Order order = new Order(1, 1, "test@cupcake.com", LocalDateTime.now());
        order.addLine(new OrderLine(vanilla, lemon, 3, 30));
        order.addLine(new OrderLine(vanilla, blueberry, 1, 25));
        assertEquals(115, order.getTotal());
    }
}
