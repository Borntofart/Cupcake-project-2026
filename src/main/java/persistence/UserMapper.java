package persistence;

import entities.User;
import exceptions.DBexceptions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserMapper { // Kun SQL for users tabellen

    public static String getPasswordHash(String email, ConnectionPool pool) throws DBexceptions { // Henter hash til login
        String sql = "SELECT password FROM users WHERE email = ?";
        try (Connection con = pool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);


            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString("password") : null;
        } catch (SQLException e) {
            throw new DBexceptions("Kunne ikke hente bruger", e);
        }
    }

    public static User getByEmail(String email, ConnectionPool pool) throws DBexceptions { // Hent bruger på email
        String sql = "SELECT user_id, email, role, balance FROM users WHERE email = ?";
        try (Connection con = pool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            return rs.next() ? map(rs) : null;
        } catch (SQLException e) {
            throw new DBexceptions("Kunne ikke hente bruger", e);
        }


    }

    public static User getById(int userId, ConnectionPool pool) throws DBexceptions { // hent bruger på id (frisk saldo)
        String sql = "SELECT user_id, email, role, balance FROM users WHERE user_id = ?";
        try (Connection con = pool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();


            return rs.next() ? map(rs) : null;
        } catch (SQLException e) {
            throw new DBexceptions("Kunne ikke hente bruger", e);
        }
    }

    public static void createUser(String email, String passwordHash, ConnectionPool pool) throws DBexceptions { // Opret kunde (US-2)
        String sql = "INSERT INTO users (email, password) VALUES (?, ?)";
        try (Connection con = pool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, passwordHash);


            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DBexceptions("Email er allerede i brug", e);
        }


    }

    public static List<User> getAllCustomers(ConnectionPool pool) throws DBexceptions { // Alle kunder (US-6)
        String sql = "SELECT user_id, email, role, balance FROM users WHERE role = 'customer' ORDER BY email";
        List<User> users = new ArrayList<>();


        try (Connection con = pool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
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
