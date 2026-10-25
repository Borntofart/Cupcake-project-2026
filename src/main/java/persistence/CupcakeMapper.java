package persistence;
import entities.Bottom; // Bund
import entities.Topping; // Top
import exceptions.DBexceptions; // Egen fejl

import java.sql.Connection; // Forbindelse
import java.sql.PreparedStatement; // Sikker SQL
import java.sql.ResultSet; // Svar
import java.sql.SQLException; // JDBC fejl
import java.util.ArrayList; // Liste
import java.util.List; // Liste type

public class CupcakeMapper { // Kun SQL for bunde og toppe

    public static List<Bottom> getAllBottoms(ConnectionPool pool) throws DBexceptions {
        String sql = "SELECT bottom_id, name, price FROM bottoms ORDER BY price, name";
        List<Bottom> list = new ArrayList<>();

        try (Connection con = pool.getConnection(); // Lån forbindelse
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) { // Kør
            while (rs.next()) { // Hver række
                list.add(new Bottom(rs.getInt("bottom_id"), rs.getString("name"), rs.getInt("price"))); // Lav objekt
            }
            return list;


        } catch (SQLException e) {
            throw new DBexceptions("Kunne ikke hente bunde", e);
        }
    }

    public static List<Topping> getAllToppings(ConnectionPool pool) throws DBexceptions { // Alle toppe
        String sql = "SELECT topping_id, name, price FROM toppings ORDER BY price, name"; // Billigst først
        List<Topping> list = new ArrayList<>();
        try (Connection con = pool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) { // Kør

            while (rs.next()) { // Hver række
                list.add(new Topping(rs.getInt("topping_id"), rs.getString("name"), rs.getInt("price"))); // Lav objekt
            }
            return list;
        } catch (SQLException e) {
            throw new DBexceptions("Kunne ikke hente toppe", e);
        }
    }

    public static Bottom getBottomById(int id, ConnectionPool pool) throws DBexceptions { // Én bund
        String sql = "SELECT bottom_id, name, price FROM bottoms WHERE bottom_id = ?";
        try (Connection con = pool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();


            if (!rs.next()) throw new DBexceptions("Bunden findes ikke"); // Ukendt id
            return new Bottom(rs.getInt("bottom_id"), rs.getString("name"), rs.getInt("price")); // Lav objekt
        } catch (SQLException e) {
            throw new DBexceptions("Kunne ikke hente bund", e);
        }
    }

    public static Topping getToppingById(int id, ConnectionPool pool) throws DBexceptions {
        String sql = "SELECT topping_id, name, price FROM toppings WHERE topping_id = ?"; // Find på id
        try (Connection con = pool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) throw new DBexceptions("Toppen findes ikke");
            return new Topping(rs.getInt("topping_id"), rs.getString("name"), rs.getInt("price")); // Lav objekt
        } catch (SQLException e) {
            throw new DBexceptions("Kunne ikke hente top", e);
        }
    }
}
