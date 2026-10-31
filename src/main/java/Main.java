import config.ThymeleafConfig;
import controllers.ShopController;
import controllers.UserController;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import persistence.ConnectionPool;

public class Main { // Starter programmet

    public static void main(String[] args) { // Starter server og ruter
        ConnectionPool pool = ConnectionPool.getInstance();

        Javalin app = Javalin.create(config -> { // Laver serveren
            config.staticFiles.add("/public");
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        UserController.addRoutes(app, pool);
        ShopController.addRoutes(app, pool);
    }
}
