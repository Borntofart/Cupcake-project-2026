package entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order { // En gemt ordre

    private final int orderId; // Ordrenummer
    private final int userId; // Kundens id
    private final String email; // Kundens email
    private final LocalDateTime createdAt;
    private final List<OrderLine> lines = new ArrayList<>();

    public Order(int orderId, int userId, String email, LocalDateTime createdAt) { // Konstruktør
        this.orderId = orderId; // gem nummer
        this.userId = userId; // gem kunde id
        this.email = email; // gem email
        this.createdAt = createdAt; // gem tidspunkt
    }

    public void addLine(OrderLine line) { // Tilføj linje
        lines.add(line); // Læg i listen
    }

    public int getOrderId() {
        return orderId;
    } // Henter nummer
    public int getUserId() {
        return userId;
    } // Henter kunde id
    public String getEmail() {
        return email;
    } // Henter email
    public LocalDateTime getCreatedAt() {
        return createdAt;
    } // Henter tid
    public List<OrderLine> getLines() {
        return lines;
    } // Henter linjer

    public int getTotal() { // Samlet pris
        return lines.stream().mapToInt(OrderLine::getLinePrice).sum(); // Ligger linjer sammen
    }
}
