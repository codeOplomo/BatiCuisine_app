package com.baticuisine.impl;

import com.baticuisine.config.DbConnection;
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

    public ProjectRepositoryImpl() {
        this.dbConnection = DbConnection.getInstance();
    }

    @Override
    public Optional<Project> addProject(Project project) {
        String sql = "INSERT INTO projects (id, project_name, profit_margin, total_cost, area, project_state, client_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, project.getId());
            stmt.setString(2, project.getProjectName());
            stmt.setDouble(3, project.getProfitMargin());
            stmt.setDouble(4, project.getTotalCost());
            stmt.setDouble(5, project.getArea());
            stmt.setString(6, project.getProjectState().name());
            stmt.setObject(7, project.getClientId());  // Save the client_id in the project
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0 ? Optional.of(project) : Optional.empty();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }

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
    @Override
    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects";
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                projects.add(mapRowToProject(rs));
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
        return new Project(id, projectName, profitMargin, totalCost, area, projectState);
    }


}
