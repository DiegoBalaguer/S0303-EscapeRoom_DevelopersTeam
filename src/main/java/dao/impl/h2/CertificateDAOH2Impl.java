package dao.impl.h2;

import dao.exceptions.DAOException;
import dao.interfaces.BaseDAO;
import dao.interfaces.CertificateDAO;
import dao.interfaces.ConnectionDAO;
import lombok.extern.slf4j.Slf4j;
import mvc.model.Certificate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class CertificateDAOH2Impl implements BaseDAO<Certificate, Integer>, CertificateDAO {

    private final ConnectionDAO connectionDAO;
    private static final String NAME_OBJECT = "certificate";

    public CertificateDAOH2Impl(ConnectionDAO connectionDAO) {
        this.connectionDAO = connectionDAO;
    }

    @Override
    public Certificate create(Certificate certificate) throws DAOException {
        String sql = "INSERT INTO " + NAME_OBJECT + " (name, description, isActive) VALUES (?, ?, ?);";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, certificate.getName());
            stmt.setString(2, certificate.getDescription());
            stmt.setBoolean(3, certificate.isActive());
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                certificate.setId(keys.getInt(1));
            }
            return certificate;
        } catch (Exception e) {
            String messageError = "Error creating " + NAME_OBJECT + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public Optional<Certificate> findById(Integer id) throws DAOException {
        String sql = "SELECT idCertificate, name, isActive, description FROM " + NAME_OBJECT + " WHERE idCertificate = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(listResultSetToCertificate(rs));
            }
            return Optional.empty();
        } catch (Exception e) {
            String messageError = "Error finding " + NAME_OBJECT + " by ID: ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public List<Certificate> findAll() throws DAOException {
        List<Certificate> certificates = new ArrayList<>();
        String sql = "SELECT idCertificate, name, isActive, description FROM " + NAME_OBJECT + ";";
        try (Connection connection = connectionDAO.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                certificates.add(listResultSetToCertificate(rs));
            }
            return certificates;
        } catch (SQLException e) {
            String messageError = "Error retrieving all " + NAME_OBJECT + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public Certificate update(Certificate certificate) throws DAOException {
        String sql = "UPDATE " + NAME_OBJECT + " SET name = ?, description = ?, isActive = ? WHERE idCertificate = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, certificate.getName());
            stmt.setString(2, certificate.getDescription());
            stmt.setBoolean(3, certificate.isActive());
            stmt.setInt(4, certificate.getId());

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                String messageError = "No " + NAME_OBJECT + " found to update with ID: " + certificate.getId();
                log.error(messageError);
                throw new DAOException(messageError);
            }
            return certificate;
        } catch (Exception e) {
            String messageError = "Error updating " + NAME_OBJECT + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public void deleteById(Integer id) throws DAOException {
        String sql = "DELETE FROM " + NAME_OBJECT + " WHERE idCertificate = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affected = stmt.executeUpdate();
            if (affected == 0) {
                String messageError = "No " + NAME_OBJECT + " found to delete with ID: " + id;
                log.error(messageError);
                throw new DAOException(messageError);
            }
        } catch (Exception e) {
            String messageError = "Error deleting " + NAME_OBJECT + " by ID: " + id;
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public boolean isExistsById(Integer id) {
        String sql = "SELECT 1 FROM " + NAME_OBJECT + " WHERE idCertificate = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            String messageError = "Error check if exist in " + NAME_OBJECT + " the ID: " + id;
            log.error(messageError, e);
            return false;
        }
    }

    @Override
    public List<Certificate> findByPlayerId(Integer playerId) throws DAOException {
        List<Certificate> certificates = new ArrayList<>();
        return certificates;
    }

    @Override
    public List<Certificate> findByRoomId(Integer roomId) throws DAOException {
        List<Certificate> certificates = new ArrayList<>();
        return certificates;

    }

    private Certificate listResultSetToCertificate(ResultSet rs) throws SQLException {
        return Certificate.builder()
                .id(rs.getInt("idCertificate"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .isActive(rs.getBoolean("isActive"))
                .build();
    }
}

