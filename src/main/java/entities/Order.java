package entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order { // En gemt ordre

    private final int orderId;
    private final int userId;
    private final String email;
    private final LocalDateTime createdAt;
    private final List<OrderLine> lines = new ArrayList<>();

    public Order(int orderId, int userId, String email, LocalDateTime createdAt) { // Konstruktør
        this.orderId = orderId;
        this.userId = userId;
        this.email = email;
        this.createdAt = createdAt;
    }

    public void addLine(OrderLine line) { // Tilføj linje
        lines.add(line);
    }

    // Getters
    public int getOrderId() {
        return orderId;
    }
    public int getUserId() {
        return userId;
    }
    public String getEmail() {
        return email;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public List<OrderLine> getLines() {
        return lines;
    }

    public int getTotal() { // Samlet pris
        return lines.stream().mapToInt(OrderLine::getLinePrice).sum();
    }
}
