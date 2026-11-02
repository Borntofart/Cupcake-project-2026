package controllers;

import entities.Order;
import entities.User;
import exceptions.DBexceptions;
import io.javalin.Javalin;
import io.javalin.http.Context;
import persistence.ConnectionPool;
import persistence.OrderMapper;
import persistence.UserMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminController { // kun HTTP for admin sider

    public static void addRoutes(Javalin app, ConnectionPool pool) { // registrerer ruterne/links
        app.get("/admin/orders", ctx -> orders(ctx, pool));
        app.get("/admin/customers", ctx -> customers(ctx, pool));
    }

    private static boolean isAdmin(Context ctx) { // sender ikke-admins til login reroute hvis nogle prøver at blive admin uden admin rettigheder
        User user = ViewModel.currentUser(ctx);
        if (user == null || !user.isAdmin()) {
            ViewModel.flash(ctx, "Kun for administratorer");
            ctx.redirect("/login");
            return false;
        }
        return true;
    }

    private static void orders(Context ctx, ConnectionPool pool) { // GET /admin/orders. alle ordrer kan ses af admin er testet og virker (US-5)
        if (!isAdmin(ctx)) return;
        Map<String, Object> model = ViewModel.base(ctx);
        try {
            model.put("orders", OrderMapper.getAllOrders(pool));
        } catch (DBexceptions e) {
            model.put("error", e.getMessage());
        }
        ctx.render("admin/orders", model);
    }

    private static void customers(Context ctx, ConnectionPool pool) { // GET /admin/customers. kunder og deres ordrer kan sese sammen med kundernes info (US-6)
        if (!isAdmin(ctx)) return;
        Map<String, Object> model = ViewModel.base(ctx);
        try {
            List<User> customers = UserMapper.getAllCustomers(pool);
            Map<Integer, List<Order>> ordersByUser = OrderMapper.getAllOrders(pool).stream()
                    .collect(Collectors.groupingBy(Order::getUserId));
            model.put("customers", customers);
            model.put("ordersByUser", ordersByUser);
        } catch (DBexceptions e) {
            model.put("error", e.getMessage());
        }
        ctx.render("admin/customers", model);
    }
}
