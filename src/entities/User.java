package entities;

public class User { // En bruger i systemet

    private final int userId; // Id fra databasen
    private final String email; // Email
    private final String role; // customer eller admin
    private final int balance; // Saldo i kr

    public User(int userId, String email, String role, int balance) { // Konstruktør
        this.userId = userId; // Gem id
        this.email = email; // gem email
        this.role = role; // gem rolle
        this.balance = balance; // Gem saldo
    }

    public int getUserId() {
        return userId;
    } // Hnter id
    public String getEmail() {
        return email;
    } // Henter email
    public String getRole() {
        return role;
    } // henter rolle
    public int getBalance() {
        return balance;
    } // Henter saldo

    public boolean isAdmin() { // Er det en admin eller hva sir du
        return "admin".equals(role); // tjek rollen
    }
}
