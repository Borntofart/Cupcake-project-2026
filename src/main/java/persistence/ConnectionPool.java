package persistence;

import com.zaxxer.hikari.HikariConfig; // Pool indstillinger
import com.zaxxer.hikari.HikariDataSource; // Selve poolen

import java.sql.Connection; // En forbindelse
import java.sql.SQLException; // Fejl fra JDBC

public class ConnectionPool { // Singleton der deler forbindelser

    private static ConnectionPool instance;
    private final HikariDataSource dataSource; // Hikari pool

    private ConnectionPool() { // privat så ingen laver flere
        String user = env("DB_USER", "postgres"); // Brugernavn
        String password = env("DB_PASSWORD", "postgres"); // Kodeord
        String url = env("DB_URL", "jdbc:postgresql://localhost:5432/Cupcake"); // Adresse til databasen

        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver"); // Postgres driver
        config.setJdbcUrl(url); // hvor databasen ligger
        config.setUsername(user);


        config.setPassword(password);
        config.setMaximumPoolSize(5); // max forbindelser
        dataSource = new HikariDataSource(config);
    }

    public static synchronized ConnectionPool getInstance() { // Henter poolen
        if (instance == null) { // Første gang
            instance = new ConnectionPool();
        }
        return instance; // Giv den tilbage!!!!!
    }

    public Connection getConnection() throws SQLException { // låner en forbindelse
        return dataSource.getConnection(); // Fra poolen
    }

    private static String env(String key, String fallback) { // Læser miljøvariabel
        String value = System.getenv(key);
        return value != null ? value : fallback;

    }



}
