import config.ThymeleafConfig;
import controllers.AdminController;
import controllers.ShopController;
import controllers.UserController;
import controllers.ViewModel;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import persistence.ConnectionPool;
import persistence.CupcakeMapper;
import persistence.OrderMapper;
import persistence.UserMapper;
import services.OrderService;
import services.UserService;

public class Main { // Starter programmet

    public static void main(String[] args) { // Starter server og ruter
        ConnectionPool pool = new ConnectionPool();

        UserMapper userMapper = new UserMapper(pool);
        CupcakeMapper cupcakeMapper = new CupcakeMapper(pool);
        OrderMapper orderMapper = new OrderMapper(pool);

        UserService userService = new UserService(userMapper);
        OrderService orderService = new OrderService(cupcakeMapper, orderMapper);
        ViewModel viewModel = new ViewModel(userService);

        Javalin app = Javalin.create(config -> { // Laver serveren
            config.staticFiles.add("/public");
            config.fileRenderer(new JavalinThymeleaf(new ThymeleafConfig().templateEngine()));
        }).start(7070);

        new UserController(userService, viewModel).addRoutes(app);
        new ShopController(orderService, userService, cupcakeMapper, orderMapper, viewModel).addRoutes(app);
        new AdminController(userMapper, orderMapper, viewModel).addRoutes(app);
    }
}
