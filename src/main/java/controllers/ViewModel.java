package controllers;

import entities.User;
import entities.cart;
import exceptions.DBexceptions;
import io.javalin.http.Context;
import persistence.ConnectionPool;
import services.UserService;

import java.util.HashMap;
import java.util.Map;

public class ViewModel { // Data som alle sider skal bruge

    public static Map<String, Object> base(Context ctx) { // Grund model til templates
        Map<String, Object> model = new HashMap<>();
        model.put("currentUser", refreshedUser(ctx));
        model.put("cart", cart(ctx));
        model.put("message", ctx.consumeSessionAttribute("flash"));
        return model;
    }

    private static User refreshedUser(Context ctx) { // Henter frisk saldo fra databasen, så indsatte penge vises med det samme
        User user = currentUser(ctx);
        if (user == null) {
            return null;
        }
        try {
            User fresh = UserService.refresh(user, ConnectionPool.getInstance());
            if (fresh != null) {
                ctx.sessionAttribute("currentUser", fresh);
                return fresh;
            }
        } catch (DBexceptions e) {
            // Database fejl: vis den gamle saldo i stedet for at crashe siden
        }
        return user;
    }

    public static User currentUser(Context ctx) { // Hvem er logget på
        return ctx.sessionAttribute("currentUser");
    }

    public static cart cart(Context ctx) { // Hent eller lav kurv
        cart c = ctx.sessionAttribute("cart");
        if (c == null) {
            c = new cart();
            ctx.sessionAttribute("cart", c);
        }
        return c;
    }

    public static void flash(Context ctx, String message) { // Gem besked til næste side
        ctx.sessionAttribute("flash", message);
    }
}
