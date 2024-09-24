package com.baticuisine.impl;

import com.baticuisine.config.DbConnection;
import com.baticuisine.models.Devis;
import com.baticuisine.repository.DevisRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DevisRepositoryImpl implements DevisRepository {
    private final DbConnection dbConnection;
    private final List<Devis> devisList = new ArrayList<>();

    public DevisRepositoryImpl() {
        this.dbConnection = DbConnection.getInstance();
    }

    public Optional<Devis> save(Devis devis) {
        String sql = "INSERT INTO devis (id, emission_date, validity_date, is_accepted, project_id, estimated_amount) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = dbConnection.getConnection();

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setObject(1, devis.getId());
            // Convert LocalDate to java.sql.Date
            statement.setDate(2, Date.valueOf(devis.getEmissionDate())); // Convert LocalDate to Date
            statement.setDate(3, Date.valueOf(devis.getValidityDate()));
            statement.setBoolean(4, devis.isAccepted());
            statement.setObject(5, devis.getProjectId());
            statement.setDouble(6, devis.getEstimatedAmount());

            statement.executeUpdate();
            return Optional.of(devis);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'enregistrement du devis : " + e.getMessage());
            return Optional.empty();
        }
    }


    public Optional<Devis> updateStatus(UUID devisId, boolean isAccepted) {
        String sql = "UPDATE devis SET is_accepted = ? WHERE id = ?";
        Connection conn = dbConnection.getConnection();
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setBoolean(1, isAccepted);
            statement.setObject(2, devisId);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                return findById(devisId);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la mise à jour du devis : " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Devis> findById(UUID devisId) {
        String sql = "SELECT * FROM devis WHERE id = ?";
        Connection conn = dbConnection.getConnection();
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setObject(1, devisId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Devis devis = new Devis(
                        resultSet.getDate("emission_date").toLocalDate(),
                        resultSet.getDate("validity_date").toLocalDate(),
                        (UUID) resultSet.getObject("project_id")
                );
                devis.setAccepted(resultSet.getBoolean("is_accepted"));
                devis.setEstimatedAmount(resultSet.getDouble("estimated_amount"));
                devis.setId(devisId);
                return Optional.of(devis);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la récupération du devis : " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Devis> findAll() {
        return new ArrayList<>(devisList);
    }


    @Override
    public boolean deleteById(UUID id) {
        return devisList.removeIf(devis -> devis.getId().equals(id));
    }
}
