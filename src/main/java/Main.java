import entities.Bottom;
import entities.Topping;
import exceptions.DBexceptions;
import io.javalin.Javalin; // Webserveren
import persistence.ConnectionPool;
import persistence.CupcakeMapper;

public class Main { // Starter programmet

    public static void main(String[] args) {
        ConnectionPool pool = ConnectionPool.getInstance(); // Henter den ene pool

        Javalin app = Javalin.create().start(7070); // Starter server på port 7070

        app.get("/", ctx -> {
            StringBuilder sb = new StringBuilder("Copenhagen Cupcakes\n\nBunde:\n");
            for (Bottom b : CupcakeMapper.getAllBottoms(pool)) {
                sb.append("  ").append(b.getName()).append(" - ").append(b.getPrice()).append(" kr\n");
            }
            sb.append("\nToppe:\n"); // Ny sektion
            for (Topping t : CupcakeMapper.getAllToppings(pool)) {
                sb.append("  ").append(t.getName()).append(" - ").append(t.getPrice()).append(" kr\n"); // Navn og pris
            }
            ctx.contentType("text/plain; charset=utf-8").result(sb.toString());
        });

        app.exception(DBexceptions.class, (e, ctx) -> {
            ctx.status(500).contentType("text/plain; charset=utf-8").result("Databasefejl: " + e.getMessage());
        });
    }
}
