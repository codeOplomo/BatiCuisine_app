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

    public List<Component> fetchComponentsForProject(UUID projectId) {
        List<Component> components = new ArrayList<>();
        String sql = "SELECT c.*, m.unit_cost, m.quantity, m.transport_cost, m.quality_coefficient, " +
                "w.hourly_rate, w.work_hours, w.worker_productivity " +
                "FROM components c " +
                "LEFT JOIN materials m ON c.id = m.id " +
                "LEFT JOIN workforces w ON c.id = w.id " +
                "WHERE c.project_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, projectId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String componentType = rs.getString("component_type");
                if (componentType.equals("Material")) {
                    Material material = new Material(
                            rs.getString("name"),
                            componentType,
                            rs.getDouble("tva_rate"),
                            rs.getDouble("unit_cost"), // Now it should be available
                            rs.getInt("quantity"),
                            rs.getDouble("transport_cost"),
                            rs.getDouble("quality_coefficient")
                    );
                    components.add(material);
                } else if (componentType.equals("Workforce")) {
                    Workforce workforce = new Workforce(
                            rs.getString("name"),
                            componentType,
                            rs.getDouble("tva_rate"),
                            rs.getDouble("hourly_rate"),
                            rs.getInt("work_hours"),
                            rs.getDouble("worker_productivity")
                    );
                    components.add(workforce);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return components;
    }


}
