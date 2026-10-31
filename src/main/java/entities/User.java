package entities;

public class User { // En bruger i systemet

    private final int userId;
    private final String email;
    private final String role;
    private final int balance;

    public User(int userId, String email, String role, int balance) { // Konstruktør
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.balance = balance;
    }

    // Getters
    public int getUserId() {
        return userId;
    }
    public String getEmail() {
        return email;
    }
    public String getRole() {
        return role;
    }
    public int getBalance() {
        return balance;
    }

    public boolean isAdmin() { // Er det en admin eller hva sir du
        return "admin".equals(role);
    }
}
