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

    public static DbConnection getInstance() {
        if (instance == null) {
            instance = new DbConnection();
        } else {
            try {
                if (instance.getConnection() == null || instance.getConnection().isClosed()) {
                    System.err.println("Re-establishing the connection...");
                    instance = new DbConnection(); // Re-initialize only if the connection is closed or null
                }
            } catch (SQLException e) {
                System.err.println("Error while checking connection state: " + e.getMessage());
            }
        }
        return instance;
    }


    public Connection getConnection() {
        return connection;
    }
}
