package com.baticuisine.impl;

import com.baticuisine.config.DbConnection;
import com.baticuisine.models.Component;
import com.baticuisine.models.Project;
import com.baticuisine.models.enumerations.ProjectState;
import com.baticuisine.repository.ProjectRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProjectRepositoryImpl implements ProjectRepository {
    private final DbConnection dbConnection;
    private final ComponentRepositoryImpl componentRepository;

    public ProjectRepositoryImpl() {
        this.dbConnection = DbConnection.getInstance();
        this.componentRepository = new ComponentRepositoryImpl();
    }

    @Override
    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects";

        Connection conn = dbConnection.getConnection();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Project project = mapRowToProject(rs);

                List<Component> components = componentRepository.fetchComponentsForProject(project.getId());

                project.getComponents().addAll(components);

                projects.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }
    @Override
    public Optional<Project> findByName(String name) {
        String sql = "SELECT * FROM projects WHERE project_name = ?";
        Connection conn = dbConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Project project = mapRowToProject(rs);
                return Optional.of(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
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

            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setObject(1, project.getId());
                stmt.setString(2, project.getProjectName());
                stmt.setDouble(3, project.getProfitMargin());
                stmt.setDouble(4, project.getTotalCost());
                stmt.setDouble(5, project.getArea());
                stmt.setString(6, project.getProjectState().name());
                stmt.setObject(7, project.getClientId());

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
                    conn.commit();
                    return Optional.of(project);
                } else {
                    conn.rollback();
                    return Optional.empty();
                }
            } catch (SQLException e) {
                conn.rollback();
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
                    conn.setAutoCommit(true);
                   // conn.close();

                } catch (SQLException e) {
                    System.out.println("Failed to close connection: " + e.getMessage());
                }
            }
        }
    }
    private Project mapRowToProject(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        String projectName = rs.getString("project_name");
        double profitMargin = rs.getDouble("profit_margin");
        double totalCost = rs.getDouble("total_cost");
        double area = rs.getDouble("area");
        ProjectState projectState = ProjectState.valueOf(rs.getString("project_state"));
        boolean isCostCalculated = rs.getBoolean("is_cost_calculated");
        UUID clientId = (UUID) rs.getObject("client_id");
        return new Project(id, projectName, profitMargin, totalCost, area, projectState, isCostCalculated, clientId);
    }
    @Override
    public Optional<Project> updateProject(Project project) {
        String sql = "UPDATE projects " +
                "SET project_name = ?, area = ?, project_state = CAST(? AS project_state), " +
                "total_cost = ?, profit_margin = ?, is_cost_calculated = ? " +
                "WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, project.getProjectName());
            stmt.setDouble(2, project.getArea());
            stmt.setString(3, project.getProjectState().name());
            stmt.setDouble(4, project.getTotalCost());
            stmt.setDouble(5, project.getProfitMargin());
            stmt.setBoolean(6, project.isCostCalculated());
            stmt.setObject(7, project.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Project updated successfully.");
                return Optional.of(project);
            } else {
                System.out.println("No project found with the given ID.");
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error occurred while updating the project: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Project> findById(UUID id) {
        String sql = "SELECT * FROM projects WHERE id = ?";
        Connection conn = dbConnection.getConnection();

        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
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
