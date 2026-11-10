package services;

import entities.Bottom;
import entities.OrderLine;
import entities.Topping;
import entities.User;
import entities.Cart;
import exceptions.DBexceptions;
import persistence.CupcakeMapper;
import persistence.OrderMapper;

public class OrderService { // regler og forretningslogik for kurv og betaling

    private final CupcakeMapper cupcakeMapper;
    private final OrderMapper orderMapper;

    public OrderService(CupcakeMapper cupcakeMapper, OrderMapper orderMapper) {
        this.cupcakeMapper = cupcakeMapper;
        this.orderMapper = orderMapper;
    }

    public void addToCart(Cart cart, int bottomId, int toppingId, int quantity) throws DBexceptions { // Lægger en cupcake i kurven med priser fra databasen
        if (!isQuantityInRange(quantity)) {
            throw new IllegalArgumentException("Antal skal være mellem 1 og 50");
        }
        Bottom bottom = cupcakeMapper.getBottomById(bottomId);
        Topping topping = cupcakeMapper.getToppingById(toppingId);
        cart.add(new OrderLine(bottom, topping, quantity));
    }

    public boolean isQuantityInRange(int quantity) {
        return quantity >= 1 && quantity <= 50;
    }

    public int checkout(User user, Cart cart) throws DBexceptions { // Betaler med saldoen og gemmer ordren (US-1)
        if (cart.isEmpty()) {
            throw new IllegalArgumentException("Kurven er tom");
        }
        if (user.getBalance() < cart.getTotal()) {
            throw new IllegalArgumentException("Du har ikke penge nok på kontoen");
        }
        int orderId = orderMapper.createOrder(user.getUserId(), cart);
        cart.clear();
        return orderId;
    }
}
