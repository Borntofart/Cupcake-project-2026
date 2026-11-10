package persistence;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool { // pool der deler forbindelser

    private final HikariDataSource dataSource;

    public ConnectionPool() {
        String user = env("DB_USER", "postgres");
        String password = env("DB_PASSWORD", "postgres");
        String url = env("DB_URL", "jdbc:postgresql://localhost:5432/Cupcake");

        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(url);
        config.setUsername(user);


        config.setPassword(password);
        config.setMaximumPoolSize(5);
        dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException { // låner en forbindelse
        return dataSource.getConnection();
    }

    private String env(String key, String fallback) { // Læser miljøvariabel
        String value = System.getenv(key);
        return value != null ? value : fallback;

    }



}
