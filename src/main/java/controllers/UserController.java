package controllers;

import entities.User;
import exceptions.DBexceptions;
import io.javalin.Javalin;
import io.javalin.http.Context;
import persistence.ConnectionPool;
import services.UserService;

import java.util.Map;

public class UserController { // Kun HTTP for login, opret og log ud

    public static void addRoutes(Javalin app, ConnectionPool pool) { // Registrerer ruterne
        app.get("/login", ctx -> ctx.render("login", ViewModel.base(ctx)));
        app.post("/login", ctx -> login(ctx, pool));
        app.get("/register", ctx -> ctx.render("register", ViewModel.base(ctx)));
        app.post("/register", ctx -> register(ctx, pool));
        app.get("/logout", UserController::logout);
    }

    private static void login(Context ctx, ConnectionPool pool) { // POST /login (US-4)
        String email = ctx.formParam("email");
        try {
            User user = UserService.login(email, ctx.formParam("password"), pool);
            ctx.sessionAttribute("currentUser", user);
            ViewModel.flash(ctx, "Velkommen " + user.getEmail());
            ctx.redirect(user.isAdmin() ? "/admin/orders" : "/");
        } catch (IllegalArgumentException | DBexceptions e) {
            showFormWithError(ctx, "login", email, e.getMessage());
        }
    }

    private static void register(Context ctx, ConnectionPool pool) { // POST /register (US-2)
        String email = ctx.formParam("email");
        try {
            User user = UserService.register(email, ctx.formParam("password"), ctx.formParam("repeat"), pool);
            ctx.sessionAttribute("currentUser", user);
            ViewModel.flash(ctx, "Din konto er oprettet");
            ctx.redirect("/");
        } catch (IllegalArgumentException | DBexceptions e) {
            showFormWithError(ctx, "register", email, e.getMessage());
        }
    }

    private static void logout(Context ctx) { // GET /logout - smider sessionen væk
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }

    private static void showFormWithError(Context ctx, String page, String email, String error) { // Viser formen igen med fejl og email udfyldt
        Map<String, Object> model = ViewModel.base(ctx);
        model.put("error", error);
        model.put("email", email);
        ctx.render(page, model);
    }
}
