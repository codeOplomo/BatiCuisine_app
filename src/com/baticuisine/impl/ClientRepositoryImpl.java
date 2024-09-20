package com.baticuisine.impl;

import com.baticuisine.config.DbConnection;
import com.baticuisine.models.Client;
import com.baticuisine.models.Project;
import com.baticuisine.repository.ClientRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ClientRepositoryImpl implements ClientRepository {
    private final DbConnection dbConnection;

    public ClientRepositoryImpl() {
        this.dbConnection = DbConnection.getInstance();
    }

    @Override
    public Optional<Client> findByName(String name) {
        String trimmedName = name.trim();
        String sql = "SELECT * FROM clients WHERE name ILIKE ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, trimmedName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToClient(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    @Override
    public Optional<Client> addClientAndProject(Client client, Project project) {
        String clientSql = "INSERT INTO clients (id, name, phone, address, is_pro) VALUES (?, ?, ?, ?, ?)";
        String projectSql = "INSERT INTO projects (id, project_name, profit_margin, total_cost, area, project_state, client_id) " +
                "VALUES (?, ?, ?, ?, ?, CAST(? AS project_state), ?)";

        Connection conn = null; // Declare the connection outside the try block

        try {
            conn = dbConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Insert the client
            try (PreparedStatement clientStmt = conn.prepareStatement(clientSql)) {
                clientStmt.setObject(1, client.getId());
                clientStmt.setString(2, client.getName());
                clientStmt.setString(3, client.getPhone());
                clientStmt.setString(4, client.getAddress());
                clientStmt.setBoolean(5, client.isPro());
                clientStmt.executeUpdate();
            }

            // Insert the project
            try (PreparedStatement projectStmt = conn.prepareStatement(projectSql)) {
                projectStmt.setObject(1, project.getId());
                projectStmt.setString(2, project.getProjectName());
                projectStmt.setDouble(3, project.getProfitMargin());
                projectStmt.setDouble(4, project.getTotalCost());
                projectStmt.setDouble(5, project.getArea());
                projectStmt.setString(6, project.getProjectState().name());
                projectStmt.setObject(7, client.getId()); // Use client's ID as foreign key
                projectStmt.executeUpdate();
            }

            conn.commit(); // Commit transaction

            // Add the project to the client's list of projects
            client.getProjects().add(project);
            return Optional.of(client); // Return the client with associated projects

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback transaction in case of error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            return Optional.empty();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Reset auto-commit to true
                    //conn.close(); // Always close the connection
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }




    @Override
    public Optional<Client> findById(UUID id) {
        String sql = "SELECT * FROM clients WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToClient(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients";
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clients.add(mapRowToClient(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    private Client mapRowToClient(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        String name = rs.getString("name");
        String phone = rs.getString("phone");
        String address = rs.getString("address");
        boolean isPro = rs.getBoolean("is_pro");
        return new Client(name, phone, address, isPro);
    }
}
