package controllers;

import entities.User;
import exceptions.DBexceptions;
import io.javalin.Javalin;
import io.javalin.http.Context;
import services.UserService;

import java.util.Map;

public class UserController { // kun HTTP/links for login, opret og log ud

    private final UserService userService;
    private final ViewModel viewModel;

    public UserController(UserService userService, ViewModel viewModel) {
        this.userService = userService;
        this.viewModel = viewModel;
    }

    public void addRoutes(Javalin app) { // Registrerer ruterne
        app.get("/login", ctx -> ctx.render("login", viewModel.base(ctx)));
        app.post("/login", this::login);
        app.get("/register", ctx -> ctx.render("register", viewModel.base(ctx)));
        app.post("/register", this::register);
        app.get("/logout", this::logout);
    }

    private void login(Context ctx) { // POST /login (US-4)
        String email = ctx.formParam("email");
        try {
            User user = userService.login(email, ctx.formParam("password"));
            ctx.sessionAttribute("currentUser", user);
            viewModel.flash(ctx, "Velkommen " + user.getEmail());
            ctx.redirect(user.isAdmin() ? "/admin/orders" : "/");
        } catch (IllegalArgumentException | DBexceptions e) {
            showFormWithError(ctx, "login", email, e.getMessage());
        }
    }

    private void register(Context ctx) { // POST /register (US-2)
        String email = ctx.formParam("email");
        try {
            User user = userService.register(email, ctx.formParam("password"), ctx.formParam("repeat"));
            ctx.sessionAttribute("currentUser", user);
            viewModel.flash(ctx, "Din konto er oprettet");
            ctx.redirect("/");
        } catch (IllegalArgumentException | DBexceptions e) {
            showFormWithError(ctx, "register", email, e.getMessage());
        }
    }

    private void logout(Context ctx) { // GET /logout smider sessionen væk når man lopgger ud
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }

    private void showFormWithError(Context ctx, String page, String email, String error) { // Viser formen igen med fejl og email udfyldt
        Map<String, Object> model = viewModel.base(ctx);
        model.put("error", error);
        model.put("email", email);
        ctx.render(page, model);
    }
}
