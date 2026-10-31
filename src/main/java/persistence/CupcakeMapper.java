package persistence;
import entities.Bottom;
import entities.Topping;
import exceptions.DBexceptions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CupcakeMapper { // Kun SQL for bunde og toppe

    public static List<Bottom> getAllBottoms(ConnectionPool pool) throws DBexceptions { // Alle bunde
        String sql = "SELECT bottom_id, name, price FROM bottoms ORDER BY price, name";
        List<Bottom> list = new ArrayList<>();

        try (Connection con = pool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Bottom(rs.getInt("bottom_id"), rs.getString("name"), rs.getInt("price")));
            }
            return list;


        } catch (SQLException e) {
            throw new DBexceptions("Kunne ikke hente bunde", e);
        }
    }

    public static List<Topping> getAllToppings(ConnectionPool pool) throws DBexceptions { // Alle toppe
        String sql = "SELECT topping_id, name, price FROM toppings ORDER BY price, name";
        List<Topping> list = new ArrayList<>();
        try (Connection con = pool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Topping(rs.getInt("topping_id"), rs.getString("name"), rs.getInt("price")));
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


            if (!rs.next()) throw new DBexceptions("Bunden findes ikke");
            return new Bottom(rs.getInt("bottom_id"), rs.getString("name"), rs.getInt("price"));
        } catch (SQLException e) {
            throw new DBexceptions("Kunne ikke hente bund", e);
        }
    }

    public static Topping getToppingById(int id, ConnectionPool pool) throws DBexceptions { // en top
        String sql = "SELECT topping_id, name, price FROM toppings WHERE topping_id = ?";
        try (Connection con = pool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) throw new DBexceptions("Toppen findes ikke");
            return new Topping(rs.getInt("topping_id"), rs.getString("name"), rs.getInt("price"));
        } catch (SQLException e) {
            throw new DBexceptions("Kunne ikke hente top", e);
        }
    }
}
