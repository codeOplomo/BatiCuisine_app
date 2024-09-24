package com.baticuisine.impl;

import com.baticuisine.config.DbConnection;
import com.baticuisine.models.Component;
import com.baticuisine.models.Material;
import com.baticuisine.models.Workforce;
import com.baticuisine.repository.ComponentRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ComponentRepositoryImpl implements ComponentRepository {
    private final DbConnection dbConnection;

    public ComponentRepositoryImpl() {
        this.dbConnection = DbConnection.getInstance();
    }

    @Override
    public List<Component> fetchComponentsForProject(UUID projectId) {
        List<Component> components = new ArrayList<>();
        String sql = "SELECT c.*, m.unit_cost, m.quantity, m.transport_cost, m.quality_coefficient, " +
                "w.hourly_rate, w.work_hours, w.worker_productivity " +
                "FROM components c " +
                "LEFT JOIN materials m ON c.id = m.id AND c.component_type = 'Material' " +
                "LEFT JOIN workforces w ON c.id = w.id AND c.component_type = 'Workforce' " +
                "WHERE c.project_id = ?";

        Connection conn = dbConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, projectId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Component component = mapRowToComponent(rs);
                components.add(component);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return components;
    }



    public Component mapRowToComponent(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        String name = rs.getString("name");
        String componentType = rs.getString("component_type");
        double tvaRate = rs.getDouble("tva_rate");

        if ("Material".equals(componentType)) {
            double unitCost = rs.getDouble("unit_cost");
            int quantity = rs.getInt("quantity");
            double transportCost = rs.getDouble("transport_cost");
            double qualityCoefficient = rs.getDouble("quality_coefficient");

            return new Material(id, name, componentType, tvaRate, unitCost, quantity, transportCost, qualityCoefficient);

        } else if ("Workforce".equals(componentType)) {
            double hourlyRate = rs.getDouble("hourly_rate");
            int workHours = rs.getInt("work_hours");
            double workerProductivity = rs.getDouble("worker_productivity");

            return new Workforce(id, name, componentType, tvaRate, hourlyRate, workHours, workerProductivity);
        }

        throw new SQLException("Unknown component type: " + componentType);
    }
}
