package dao.impl.h2;

import dao.exceptions.DAOException;
import dao.interfaces.BaseDAO;
import dao.interfaces.CertificateWinDAO;
import lombok.extern.slf4j.Slf4j;
import model.CertificateWin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class CertificateWinDAOH2Impl implements BaseDAO<CertificateWin, Integer>, CertificateWinDAO {

    private final Connection connection;
    private static final String nameObject = "certificateWin";

    public CertificateWinDAOH2Impl() {
        this.connection = ConnectionDAOH2Impl.getConnection();
    }

    @Override
    public CertificateWin create(CertificateWin certificateWin) throws DAOException {
        String sql = "INSERT INTO ? (idCertificate, idPlayer, idRoom, description, dateDelivery, isActive) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nameObject);
            stmt.setInt(2, certificateWin.getIdCertificate());
            stmt.setInt(3, certificateWin.getIdPlayer());
            stmt.setInt(4, certificateWin.getIdRoom());
            stmt.setString(5, certificateWin.getDescription());
            stmt.setDate(6, Date.valueOf(certificateWin.getDateDelivery()));
            stmt.setBoolean(7, certificateWin.isActive());
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                certificateWin.setId(keys.getInt(1));
            }
            return certificateWin;
        } catch (Exception e) {
            String messageError = "Error creating " + nameObject + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public Optional<CertificateWin> findById(Integer id) throws DAOException {
        String sql = "SELECT idCertificateWin, idCertificate, idPlayer, idRoom, isActive FROM ? WHERE idCertificateWin = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nameObject);
            stmt.setInt(2, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToCertificateWin(rs));
            }
            return Optional.empty();
        } catch (Exception e) {
            String messageError = "Error finding " + nameObject + " by ID: ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public List<CertificateWin> findAll() throws DAOException {
        List<CertificateWin> certificateWins = new ArrayList<>();
        String sql = "SELECT idCertificateWin, idCertificate, idPlayer, idRoom, isActive FROM " + nameObject;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                certificateWins.add(mapResultSetToCertificateWin(rs));
            }
            return certificateWins;
        } catch (SQLException e) {
            String messageError = "Error retrieving all " + nameObject + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public CertificateWin update(CertificateWin certificateWin) throws DAOException {
        String sql = "UPDATE ? SET idCertificate = ?, idPlayer = ?, idRoom = ?, description = ?, dateDelivery = ?, isActive = ? WHERE idCertificateWin = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nameObject);
            stmt.setInt(2, certificateWin.getIdCertificate());
            stmt.setInt(3, certificateWin.getIdPlayer());
            stmt.setInt(4, certificateWin.getIdRoom());
            stmt.setString(5, certificateWin.getDescription());
            stmt.setDate(6, Date.valueOf(certificateWin.getDateDelivery()));
            stmt.setBoolean(7, certificateWin.isActive());
            stmt.setInt(8, certificateWin.getId());

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                String messageError = "No " + nameObject + " found to update with ID: " + certificateWin.getId();
                log.error(messageError);
                throw new DAOException(messageError);
            }
            return certificateWin;
        } catch (Exception e) {
            String messageError = "Error updating " + nameObject + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public void deleteById(Integer id) throws DAOException {
        String sql = "DELETE FROM ? WHERE idCertificateWin = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nameObject);
            stmt.setInt(2, id);
            int affected = stmt.executeUpdate();
            if (affected == 0) {
                String messageError = "No " + nameObject + " found to delete with ID: " + id;
                log.error(messageError);
                throw new DAOException(messageError);
            }
        } catch (Exception e) {
            String messageError = "Error deleting " + nameObject + " by ID: " + id;
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public boolean isExistsById(Integer id) {
        String sql = "SELECT 1 FROM ? WHERE idCertificateWin = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nameObject);
            stmt.setInt(2, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            String messageError = "Error check if exist in " + nameObject + " the ID: " + id;
            log.error(messageError, e);
            return false;
        }
    }

    private CertificateWin mapResultSetToCertificateWin(ResultSet rs) throws SQLException {
        return CertificateWin.builder()
                .id(rs.getInt("idCertificateWin"))
                .idCertificate(rs.getInt("idCertificate"))
                .idPlayer(rs.getInt("idPlayer"))
                .idRoom(rs.getInt("idRoom"))
                .isActive(rs.getBoolean("isActive"))
                .build();
    }
}
