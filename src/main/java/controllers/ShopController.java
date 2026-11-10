package controllers;

import entities.User;
import exceptions.DBexceptions;
import io.javalin.Javalin;
import io.javalin.http.Context;
import persistence.CupcakeMapper;
import persistence.OrderMapper;
import services.OrderService;
import services.UserService;

import java.util.Map;

public class ShopController { // kun HTTP/links for forside, kurv, betaling og mine ordrer

    private final OrderService orderService;
    private final UserService userService;
    private final CupcakeMapper cupcakeMapper;
    private final OrderMapper orderMapper;
    private final ViewModel viewModel;

    public ShopController(OrderService orderService, UserService userService, CupcakeMapper cupcakeMapper,
                          OrderMapper orderMapper, ViewModel viewModel) {
        this.orderService = orderService;
        this.userService = userService;
        this.cupcakeMapper = cupcakeMapper;
        this.orderMapper = orderMapper;
        this.viewModel = viewModel;
    }

    public void addRoutes(Javalin app) { // registrereriong af ruterne
        app.get("/", this::index);
        app.post("/Cart/add", this::addToCart);
        app.get("/Cart", ctx -> ctx.render("Cart", viewModel.base(ctx)));
        app.post("/Cart/remove", this::removeFromCart);
        app.post("/checkout", this::checkout);
        app.get("/orders", this::myOrders);
    }

    private void index(Context ctx) { // GET / forside med bestillingsform
        Map<String, Object> model = viewModel.base(ctx);
        try {
            model.put("bottoms", cupcakeMapper.getAllBottoms());
            model.put("toppings", cupcakeMapper.getAllToppings());
        } catch (DBexceptions e) {
            model.put("error", e.getMessage());
        }
        ctx.render("index", model);
    }

    private void addToCart(Context ctx) { // POST /Cart/add kan tilføje til din kurv(US-1)
        try {
            int bottomId = Integer.parseInt(ctx.formParam("bottom"));
            int toppingId = Integer.parseInt(ctx.formParam("topping"));
            int quantity = Integer.parseInt(ctx.formParam("quantity"));
            orderService.addToCart(viewModel.cart(ctx), bottomId, toppingId, quantity);
            viewModel.flash(ctx, "Lagt i kurven");
        } catch (NumberFormatException e) {
            viewModel.flash(ctx, "Vælg både bund, top og antal");
        } catch (IllegalArgumentException | DBexceptions e) {
            viewModel.flash(ctx, e.getMessage());
        }
        ctx.redirect("/");
    }

    private void removeFromCart(Context ctx) { // POST /Cart/remove kan fjerne ting fra din kurv  (US-7)
        try {
            viewModel.cart(ctx).remove(Integer.parseInt(ctx.formParam("index")));
        } catch (NumberFormatException ignored) {
        }
        ctx.redirect("/Cart");
    }

    private void checkout(Context ctx) { // POST /checkout :kan betale med saldo (US-1)
        User user = viewModel.currentUser(ctx);
        if (user == null) {
            viewModel.flash(ctx, "Log ind eller opret en konto for at betale");
            ctx.redirect("/login");
            return;
        }
        try {
            User fresh = userService.refresh(user);
            int orderId = orderService.checkout(fresh, viewModel.cart(ctx));
            viewModel.flash(ctx, "Tak! Ordre nr. " + orderId + " er betalt og klar til afhentning");
            ctx.redirect("/orders");
        } catch (IllegalArgumentException | DBexceptions e) {
            viewModel.flash(ctx, e.getMessage());
            ctx.redirect("/Cart");
        }
    }

    private void myOrders(Context ctx) { // GET /orders - kundens egne ordrer så de kan se dem
        User user = viewModel.currentUser(ctx);
        if (user == null) {
            ctx.redirect("/login");
            return;
        }
        Map<String, Object> model = viewModel.base(ctx);
        try {
            model.put("orders", orderMapper.getOrdersByUser(user.getUserId()));
        } catch (DBexceptions e) {
            model.put("error", e.getMessage());
        }
        ctx.render("orders", model);
    }
}
