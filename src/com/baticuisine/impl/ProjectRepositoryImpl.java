package com.baticuisine.impl;

import com.baticuisine.config.DbConnection;
import com.baticuisine.models.Component;
import com.baticuisine.models.Material;
import com.baticuisine.models.Project;
import com.baticuisine.models.Workforce;
import com.baticuisine.models.enumerations.ProjectState;
import com.baticuisine.repository.ProjectRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProjectRepositoryImpl implements ProjectRepository {
    private final DbConnection dbConnection;

    public ProjectRepositoryImpl() {
        this.dbConnection = DbConnection.getInstance();
    }


    @Override
    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects";
        ComponentRepositoryImpl componentRepository = new ComponentRepositoryImpl(); // Create an instance of the component repository

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Create project object from ResultSet
                Project project = mapRowToProject(rs);

                // Fetch components for each project
                List<Component> components = componentRepository.fetchComponentsForProject(project.getId());

                // Even if no components are found, add empty list to project
                if (components != null && !components.isEmpty()) {
                    project.getComponents().addAll(components); // Add components to the project
                } else {
                    System.out.println("No components available for project: " + project.getProjectName()); // Debugging
                }

                // Add project to list
                projects.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }



    private Project mapRowToProject(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        String projectName = rs.getString("project_name");
        double profitMargin = rs.getDouble("profit_margin");
        double totalCost = rs.getDouble("total_cost");
        double area = rs.getDouble("area");
        ProjectState projectState = ProjectState.valueOf(rs.getString("project_state"));
        UUID clientId = (UUID) rs.getObject("client_id");
        return new Project(id, projectName, profitMargin, totalCost, area, projectState, clientId);
    }

    @Override
    public Optional<Project> addProject(Project project) {
        String sql = "INSERT INTO projects (id, project_name, profit_margin, total_cost, area, project_state, client_id) " +
                "VALUES (?, ?, ?, ?, ?, CAST(? AS project_state), ?)";
        Connection conn = null;

        try {
            conn = dbConnection.getConnection();
            if (conn == null || conn.isClosed()) {
                System.out.println("Connection failed.");
                return Optional.empty();
            }

            conn.setAutoCommit(false); // Start transaction

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setObject(1, project.getId());
                stmt.setString(2, project.getProjectName());
                stmt.setDouble(3, project.getProfitMargin());
                stmt.setDouble(4, project.getTotalCost());
                stmt.setDouble(5, project.getArea());
                stmt.setString(6, project.getProjectState().name());
                stmt.setObject(7, project.getClientId());

                // Debugging prints to check values
                System.out.println("Prepared Statement values: ");
                System.out.println("Project ID: " + project.getId());
                System.out.println("Project Name: " + project.getProjectName());
                System.out.println("Profit Margin: " + project.getProfitMargin());
                System.out.println("Total Cost: " + project.getTotalCost());
                System.out.println("Area: " + project.getArea());
                System.out.println("Project State: " + project.getProjectState());
                System.out.println("Client ID: " + project.getClientId());

                int affectedRows = stmt.executeUpdate();
                System.out.println("Affected Rows: " + affectedRows);

                if (affectedRows > 0) {
                    conn.commit(); // Commit transaction if successful
                    return Optional.of(project);
                } else {
                    conn.rollback(); // Rollback transaction if no rows were affected
                    return Optional.empty();
                }
            } catch (SQLException e) {
                conn.rollback(); // Rollback transaction if an exception occurs
                System.out.println("SQLException occurred: " + e.getMessage());
                e.printStackTrace();
                return Optional.empty();
            }
        } catch (SQLException e) {
            System.out.println("SQLException occurred during connection or transaction handling: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Reset auto-commit to true after transaction
                    //conn.close(); // Ensure the connection is closed
                } catch (SQLException e) {
                    System.out.println("Failed to close connection: " + e.getMessage());
                }
            }
        }
    }
    @Override
    public Optional<Project> updateProject(Project project) {
        String sql = "UPDATE projects SET project_name = ?, profit_margin = ?, total_cost = ?, area = ?, project_state = CAST(? AS project_state) WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, project.getProjectName());
            stmt.setDouble(2, project.getProfitMargin());
            stmt.setDouble(3, project.getTotalCost());
            stmt.setDouble(4, project.getArea());
            stmt.setString(5, project.getProjectState().name());
            stmt.setObject(6, project.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                return Optional.of(project);  // If update was successful, return the project
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();  // If something goes wrong, return an empty optional
    }

    @Override
    public Optional<Project> findByName(String name) {
        String sql = "SELECT * FROM projects WHERE project_name = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToProject(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Project> findById(UUID id) {
        String sql = "SELECT * FROM projects WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToProject(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }




}
