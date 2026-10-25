package persistence;

import entities.User; // Bruger klassen
import exceptions.DBexceptions; // Egen fejl

import java.sql.Connection; // Forbindelse
import java.sql.PreparedStatement; // Sikker SQL
import java.sql.ResultSet; // Svar fra databasen
import java.sql.SQLException; // JDBC fejl
import java.util.ArrayList; // Liste
import java.util.List; // Liste type

public class UserMapper { // Kun SQL for users tabellen

    public static String getPasswordHash(String email, ConnectionPool pool) throws DBexceptions {
        String sql = "SELECT password FROM users WHERE email = ?"; // Find på email
        try (Connection con = pool.getConnection(); // lån forbindelse
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email); // Sæt email ind


            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString("password") : null; // Hash eller null
        } catch (SQLException e) {
            throw new DBexceptions("Kunne ikke hente bruger", e); // Pak fejlen ind
        }
    }

    public static User getByEmail(String email, ConnectionPool pool) throws DBexceptions { // Hent bruger på email
        String sql = "SELECT user_id, email, role, balance FROM users WHERE email = ?";
        try (Connection con = pool.getConnection(); // Lån forbindelse
             PreparedStatement ps = con.prepareStatement(sql)) { // Forbered SQL
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            return rs.next() ? map(rs) : null;
        } catch (SQLException e) {
            throw new DBexceptions("Kunne ikke hente bruger", e);
        }


    }

    public static User getById(int userId, ConnectionPool pool) throws DBexceptions { // hent bruger på id (frisk saldo)
        String sql = "SELECT user_id, email, role, balance FROM users WHERE user_id = ?";
        try (Connection con = pool.getConnection(); // Lån forbindelse
             PreparedStatement ps = con.prepareStatement(sql)) { // Forbered SQL
            ps.setInt(1, userId); // Sæt id ind
            ResultSet rs = ps.executeQuery();


            return rs.next() ? map(rs) : null; // bruger eller null
        } catch (SQLException e) {
            throw new DBexceptions("Kunne ikke hente bruger", e);
        }
    }

    public static void createUser(String email, String passwordHash, ConnectionPool pool) throws DBexceptions { // Opret kunde (US-2)
        String sql = "INSERT INTO users (email, password) VALUES (?, ?)"; // Rolle og saldo får default
        try (Connection con = pool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, passwordHash); // Hash, aldrig klar tekst


            ps.executeUpdate();
        } catch (SQLException e) { // email findes allerede
            throw new DBexceptions("Email er allerede i brug", e);
        }


    }

    public static List<User> getAllCustomers(ConnectionPool pool) throws DBexceptions { // Alle kunder (US-6)
        String sql = "SELECT user_id, email, role, balance FROM users WHERE role = 'customer' ORDER BY email"; // Kun kunder
        List<User> users = new ArrayList<>();


        try (Connection con = pool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql); // Forbered SQL
             ResultSet rs = ps.executeQuery()) { // Kør
            while (rs.next()) {
                users.add(map(rs));
            }


            return users;
        } catch (SQLException e) {
            throw new DBexceptions("Kunne ikke hente kunder", e);
        }
    }

    private static User map(ResultSet rs) throws SQLException { // Række til objekt
        return new User(
                rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("role"),
                rs.getInt("balance"));

    }



}
