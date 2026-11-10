package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderServiceTest { // Tester at antal skal være inden for 1-50

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        // Mapperne bruges ikke i range-tjekket, så null er fint her
        orderService = new OrderService(null, null);
    }

    @Test
    void quantityInsideRange() {
        assertTrue(orderService.isQuantityInRange(1));
        assertTrue(orderService.isQuantityInRange(25));
        assertTrue(orderService.isQuantityInRange(50));
    }

    @Test
    void quantityOutsideRange() {
        assertFalse(orderService.isQuantityInRange(0));
        assertFalse(orderService.isQuantityInRange(51));
        assertFalse(orderService.isQuantityInRange(-1));
    }

    @Test
    void addToCartThrowsWhenQuantityOutsideRange() {
        // Fejlen kastes før databasen bliver brugt
        assertThrows(IllegalArgumentException.class,
                () -> orderService.addToCart(new entities.Cart(), 1, 1, 0));
    }
}
