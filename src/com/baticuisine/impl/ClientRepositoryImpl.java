package com.baticuisine.impl;

import com.baticuisine.config.DbConnection;
import com.baticuisine.models.*;
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
    public Optional<Client> addClientProjectAndComponents(Client client, Project project, List<Material> materials, List<Workforce> workforces) {
        String clientSql = "INSERT INTO clients (id, name, phone, address, is_pro) VALUES (?, ?, ?, ?, ?)";
        String projectSql = "INSERT INTO projects (id, project_name, profit_margin, total_cost, area, project_state, client_id) " +
                "VALUES (?, ?, ?, ?, ?, CAST(? AS project_state), ?)";
        String componentSql = "INSERT INTO components (id, name, component_type, tva_rate, project_id, cost) VALUES (?, ?, ?, ?, ?, ?)";
        String materialSql = "INSERT INTO materials (id, unit_cost, quantity, transport_cost, quality_coefficient) VALUES (?, ?, ?, ?, ?)";
        String workforceSql = "INSERT INTO workforces (id, hourly_rate, work_hours, worker_productivity) VALUES (?, ?, ?, ?)";

        Connection conn = null;

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

            // Insert components, materials, and workforces
            for (Material material : materials) {
                // Insert into components table
                try (PreparedStatement componentStmt = conn.prepareStatement(componentSql)) {
                    componentStmt.setObject(1, material.getId());
                    componentStmt.setString(2, material.getName());
                    componentStmt.setString(3, "Material"); // Assuming component type is "Material"
                    componentStmt.setDouble(4, material.getTvaRate());
                    componentStmt.setObject(5, project.getId());
                    componentStmt.setDouble(6, material.getUnitCost()); // Assuming cost is the unit cost
                    componentStmt.executeUpdate();
                }

                // Now insert into the materials table
                try (PreparedStatement materialStmt = conn.prepareStatement(materialSql)) {
                    materialStmt.setObject(1, material.getId());
                    materialStmt.setDouble(2, material.getUnitCost());
                    materialStmt.setInt(3, material.getQuantity());
                    materialStmt.setDouble(4, material.getTransportCost());
                    materialStmt.setDouble(5, material.getQualityCoefficient());
                    materialStmt.executeUpdate();
                }
            }

            for (Workforce workforce : workforces) {
                // Insert into components table
                try (PreparedStatement componentStmt = conn.prepareStatement(componentSql)) {
                    componentStmt.setObject(1, workforce.getId());
                    componentStmt.setString(2, workforce.getName());
                    componentStmt.setString(3, "Workforce"); // Assuming component type is "Workforce"
                    componentStmt.setDouble(4, 0.0); // Assuming tva_rate is not applicable for workforce
                    componentStmt.setObject(5, project.getId());
                    componentStmt.setDouble(6, 0.0); // Assuming cost is not applicable for workforce
                    componentStmt.executeUpdate();
                }

                // Now insert into the workforces table
                try (PreparedStatement workforceStmt = conn.prepareStatement(workforceSql)) {
                    workforceStmt.setObject(1, workforce.getId());
                    workforceStmt.setDouble(2, workforce.getHourlyRate());
                    workforceStmt.setInt(3, workforce.getWorkHours());
                    workforceStmt.setDouble(4, workforce.getWorkerProductivity());
                    workforceStmt.executeUpdate();
                }
            }

            conn.commit(); // Commit transaction
            client.getProjects().add(project); // Add the project to the client's list of projects
            return Optional.of(client);

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
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /*
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
*/



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
