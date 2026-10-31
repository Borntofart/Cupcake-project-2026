package services;

import entities.Bottom;
import entities.OrderLine;
import entities.Topping;
import entities.User;
import entities.cart;
import exceptions.DBexceptions;
import persistence.ConnectionPool;
import persistence.CupcakeMapper;
import persistence.OrderMapper;

public class OrderService { // Regler for kurv og betaling

    public static void addToCart(cart cart, int bottomId, int toppingId, int quantity, ConnectionPool pool) throws DBexceptions { // Lægger en cupcake i kurven med priser fra databasen
        if (quantity < 1 || quantity > 50) {
            throw new IllegalArgumentException("Antal skal være mellem 1 og 50");
        }
        Bottom bottom = CupcakeMapper.getBottomById(bottomId, pool);
        Topping topping = CupcakeMapper.getToppingById(toppingId, pool);
        cart.add(new OrderLine(bottom, topping, quantity));
    }

    public static int checkout(User user, cart cart, ConnectionPool pool) throws DBexceptions { // Betaler med saldoen og gemmer ordren (US-1)
        if (cart.isEmpty()) {
            throw new IllegalArgumentException("Kurven er tom");
        }
        if (user.getBalance() < cart.getTotal()) {
            throw new IllegalArgumentException("Du har ikke penge nok på kontoen");
        }
        int orderId = OrderMapper.createOrder(user.getUserId(), cart, pool);
        cart.clear();
        return orderId;
    }
}
