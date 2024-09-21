package com.baticuisine.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    private static DbConnection instance;
    private Connection connection;
    private final String jdbcUrl = "jdbc:postgresql://localhost:5432/baticuisine_db";
    private final String jdbcUser = "GreenPulse";
    private final String jdbcPassword = "";

    private DbConnection() {
        try {
            connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
            System.out.println("\u001B[92m" + "Connected to the database." + "\u001B[0m");
        } catch (SQLException e) {
            System.err.println("Failed to make connection: " + e.getMessage());
        }
    }

    public static synchronized DbConnection getInstance() {
        try {
            if (instance == null || instance.getConnection() == null || instance.getConnection().isClosed()) {
                instance = new DbConnection();
            }
        } catch (SQLException e) {
            System.err.println("Error while checking connection: " + e.getMessage());
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
