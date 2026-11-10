package controllers;

import entities.Cart;
import entities.User;
import exceptions.DBexceptions;
import io.javalin.http.Context;
import services.UserService;

import java.util.HashMap;
import java.util.Map;

public class ViewModel { // Data som alle sider skal bruge

    private final UserService userService;

    public ViewModel(UserService userService) {
        this.userService = userService;
    }

    public Map<String, Object> base(Context ctx) { // Grund model til templates
        Map<String, Object> model = new HashMap<>();
        model.put("currentUser", refreshedUser(ctx));
        model.put("Cart", cart(ctx));
        model.put("message", ctx.consumeSessionAttribute("flash"));
        return model;
    }

    private User refreshedUser(Context ctx) { // Henter frisk saldo fra databasen, så indsatte penge vises med det samme
        User user = currentUser(ctx);
        if (user == null) {
            return null;
        }
        try {
            User fresh = userService.refresh(user);
            if (fresh != null) {
                ctx.sessionAttribute("currentUser", fresh);
                return fresh;
            }
        } catch (DBexceptions e) {
            // database fejl vis den gamle saldo i stedet for at crashe siden
        }
        return user;
    }

    public User currentUser(Context ctx) { // hvem er logget på
        return ctx.sessionAttribute("currentUser");
    }

    public Cart cart(Context ctx) { // hent eller lav kurv
        Cart c = ctx.sessionAttribute("Cart");
        if (c == null) {
            c = new Cart();
            ctx.sessionAttribute("Cart", c);
        }
        return c;
    }

    public void flash(Context ctx, String message) { // Gem besked til næste side
        ctx.sessionAttribute("flash", message);
    }
}
