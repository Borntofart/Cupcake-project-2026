package controllers;

import entities.User;
import exceptions.DBexceptions;
import io.javalin.Javalin;
import io.javalin.http.Context;
import persistence.ConnectionPool;
import persistence.CupcakeMapper;
import persistence.OrderMapper;
import services.OrderService;
import services.UserService;

import java.util.Map;

public class ShopController { // Kun HTTP for forside, kurv, betaling og mine ordrer

    public static void addRoutes(Javalin app, ConnectionPool pool) { // Registrerer ruterne
        app.get("/", ctx -> index(ctx, pool));
        app.post("/cart/add", ctx -> addToCart(ctx, pool));
        app.get("/cart", ctx -> ctx.render("cart", ViewModel.base(ctx)));
        app.post("/cart/remove", ShopController::removeFromCart);
        app.post("/checkout", ctx -> checkout(ctx, pool));
        app.get("/orders", ctx -> myOrders(ctx, pool));
    }

    private static void index(Context ctx, ConnectionPool pool) { // GET / - forside med bestillingsform
        Map<String, Object> model = ViewModel.base(ctx);
        try {
            model.put("bottoms", CupcakeMapper.getAllBottoms(pool));
            model.put("toppings", CupcakeMapper.getAllToppings(pool));
        } catch (DBexceptions e) {
            model.put("error", e.getMessage());
        }
        ctx.render("index", model);
    }

    private static void addToCart(Context ctx, ConnectionPool pool) { // POST /cart/add (US-1)
        try {
            int bottomId = Integer.parseInt(ctx.formParam("bottom"));
            int toppingId = Integer.parseInt(ctx.formParam("topping"));
            int quantity = Integer.parseInt(ctx.formParam("quantity"));
            OrderService.addToCart(ViewModel.cart(ctx), bottomId, toppingId, quantity, pool);
            ViewModel.flash(ctx, "Lagt i kurven");
        } catch (NumberFormatException e) {
            ViewModel.flash(ctx, "Vælg både bund, top og antal");
        } catch (IllegalArgumentException | DBexceptions e) {
            ViewModel.flash(ctx, e.getMessage());
        }
        ctx.redirect("/");
    }

    private static void removeFromCart(Context ctx) { // POST /cart/remove (US-7)
        try {
            ViewModel.cart(ctx).remove(Integer.parseInt(ctx.formParam("index")));
        } catch (NumberFormatException ignored) {
        }
        ctx.redirect("/cart");
    }

    private static void checkout(Context ctx, ConnectionPool pool) { // POST /checkout - betal med saldo (US-1)
        User user = ViewModel.currentUser(ctx);
        if (user == null) {
            ViewModel.flash(ctx, "Log ind eller opret en konto for at betale");
            ctx.redirect("/login");
            return;
        }
        try {
            User fresh = UserService.refresh(user, pool);
            int orderId = OrderService.checkout(fresh, ViewModel.cart(ctx), pool);
            ViewModel.flash(ctx, "Tak! Ordre nr. " + orderId + " er betalt og klar til afhentning");
            ctx.redirect("/orders");
        } catch (IllegalArgumentException | DBexceptions e) {
            ViewModel.flash(ctx, e.getMessage());
            ctx.redirect("/cart");
        }
    }

    private static void myOrders(Context ctx, ConnectionPool pool) { // GET /orders - kundens egne ordrer
        User user = ViewModel.currentUser(ctx);
        if (user == null) {
            ctx.redirect("/login");
            return;
        }
        Map<String, Object> model = ViewModel.base(ctx);
        try {
            model.put("orders", OrderMapper.getOrdersByUser(user.getUserId(), pool));
        } catch (DBexceptions e) {
            model.put("error", e.getMessage());
        }
        ctx.render("orders", model);
    }
}
