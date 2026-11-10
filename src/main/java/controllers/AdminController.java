package controllers;

import entities.Order;
import entities.User;
import exceptions.DBexceptions;
import io.javalin.Javalin;
import io.javalin.http.Context;
import persistence.OrderMapper;
import persistence.UserMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminController { // kun HTTP for admin sider

    private final UserMapper userMapper;
    private final OrderMapper orderMapper;
    private final ViewModel viewModel;

    public AdminController(UserMapper userMapper, OrderMapper orderMapper, ViewModel viewModel) {
        this.userMapper = userMapper;
        this.orderMapper = orderMapper;
        this.viewModel = viewModel;
    }

    public void addRoutes(Javalin app) { // registrerer ruterne/links
        app.get("/admin/orders", this::orders);
        app.get("/admin/customers", this::customers);
        app.post("/admin/orders/delete", this::deleteOrder);
    }

    private boolean isAdmin(Context ctx) { // sender ikke-admins til login reroute hvis nogle prøver at blive admin uden admin rettigheder
        User user = viewModel.currentUser(ctx);
        if (user == null || !user.isAdmin()) {
            viewModel.flash(ctx, "Kun for administratorer");
            ctx.redirect("/login");
            return false;
        }
        return true;
    }

    private void orders(Context ctx) { // GET /admin/orders. alle ordrer kan ses af admin er testet og virker (US-5)
        if (!isAdmin(ctx)) return;
        Map<String, Object> model = viewModel.base(ctx);
        try {
            model.put("orders", orderMapper.getAllOrders());
        } catch (DBexceptions e) {
            model.put("error", e.getMessage());
        }
        ctx.render("admin/orders", model);
    }

    private void deleteOrder(Context ctx) { // POST /admin/orders/delete - sletter en ordre, fx hvis den er ugyldig (US-8)
        if (!isAdmin(ctx)) return;
        try {
            int orderId = Integer.parseInt(ctx.formParam("orderId"));
            orderMapper.deleteOrder(orderId);
            viewModel.flash(ctx, "Ordre nr. " + orderId + " er slettet");
        } catch (NumberFormatException | DBexceptions e) {
            viewModel.flash(ctx, "Kunne ikke slette ordren");
        }
        ctx.redirect("/admin/orders");
    }

    private void customers(Context ctx) { // GET /admin/customers. kunder og deres ordrer kan sese sammen med kundernes info (US-6)
        if (!isAdmin(ctx)) return;
        Map<String, Object> model = viewModel.base(ctx);
        try {
            List<User> customers = userMapper.getAllCustomers();
            Map<Integer, List<Order>> ordersByUser = orderMapper.getAllOrders().stream()
                    .collect(Collectors.groupingBy(Order::getUserId));
            model.put("customers", customers);
            model.put("ordersByUser", ordersByUser);
        } catch (DBexceptions e) {
            model.put("error", e.getMessage());
        }
        ctx.render("admin/customers", model);
    }
}
