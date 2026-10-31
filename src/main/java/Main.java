import config.ThymeleafConfig;
import controllers.ViewModel;
import exceptions.DBexceptions;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import persistence.ConnectionPool;
import persistence.CupcakeMapper;

import java.util.Map;

public class Main { // Starter programmet

    public static void main(String[] args) { // Starter server og ruter
        ConnectionPool pool = ConnectionPool.getInstance();

        Javalin app = Javalin.create(config -> { // Laver serveren
            config.staticFiles.add("/public");
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        app.get("/", ctx -> { // Forside
            Map<String, Object> model = ViewModel.base(ctx);
            try {
                model.put("bottoms", CupcakeMapper.getAllBottoms(pool));
                model.put("toppings", CupcakeMapper.getAllToppings(pool));
            } catch (DBexceptions e) {
                model.put("error", e.getMessage());
            }
            ctx.render("index", model);
        });
    }
}
