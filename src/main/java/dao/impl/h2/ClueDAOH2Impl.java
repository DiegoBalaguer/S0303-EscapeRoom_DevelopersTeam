package dao.impl.h2;

import dao.exceptions.DAOException;
import dao.interfaces.BaseDAO;
import dao.interfaces.ClueDAO;
import dao.interfaces.ConnectionDAO;
import lombok.extern.slf4j.Slf4j;
import model.Clue;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ClueDAOH2Impl implements BaseDAO<Clue, Integer>, ClueDAO {

    private final ConnectionDAO connectionDAO;
    private static final String nameObject = "clue";

    public ClueDAOH2Impl(ConnectionDAO connectionDAO) {
        this.connectionDAO = connectionDAO;
    }

    @Override
    public Clue create(Clue clue) throws DAOException {
        String sql = "INSERT INTO " + nameObject + " (idRoom, name, description, price, isActive) VALUES (?, ?, ?, ?, ?);";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, clue.getIdRoom());
            stmt.setString(2, clue.getName());
            stmt.setString(3, clue.getDescription());
            stmt.setBigDecimal(4, clue.getPrice());
            stmt.setBoolean(5, clue.isActive());
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                clue.setId(keys.getInt(1));
            }
            return clue;
        } catch (Exception e) {
            String messageError = "Error creating " + nameObject + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public Optional<Clue> findById(Integer id) throws DAOException {
        String sql = "SELECT idClue, idRoom, name, price, isActive FROM " + nameObject + " WHERE idClue = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToClue(rs));
            }
            return Optional.empty();
        } catch (Exception e) {
            String messageError = "Error finding " + nameObject + " by ID: ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public List<Clue> findAll() throws DAOException {
        List<Clue> clues = new ArrayList<>();
        String sql = "SELECT idClue, idRoom, name, price, isActive FROM " + nameObject + ";";
        try (Connection connection = connectionDAO.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clues.add(mapResultSetToClue(rs));
            }
            return clues;
        } catch (SQLException e) {
            String messageError = "Error retrieving all " + nameObject + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public Clue update(Clue clue) throws DAOException {
        String sql = "UPDATE " + nameObject + " SET idRoom = ?, name = ?, description = ?, price = ?, isActive = ? WHERE idClue = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, clue.getIdRoom());
            stmt.setString(2, clue.getName());
            stmt.setString(3, clue.getDescription());
            stmt.setBigDecimal(4, clue.getPrice());
            stmt.setBoolean(5, clue.isActive());
            stmt.setInt(6, clue.getId());

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                String messageError = "No " + nameObject + " found to update with ID: " + clue.getId();
                log.error(messageError);
                throw new DAOException(messageError);
            }
            return clue;
        } catch (Exception e) {
            String messageError = "Error updating " + nameObject + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public void deleteById(Integer id) throws DAOException {
        String sql = "DELETE FROM " + nameObject + " WHERE idClue = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
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
        String sql = "SELECT 1 FROM " + nameObject + " WHERE idClue = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            String messageError = "Error check if exist in " + nameObject + " the ID: " + id;
            log.error(messageError, e);
            return false;
        }
    }

    private Clue mapResultSetToClue(ResultSet rs) throws SQLException {
        return Clue.builder()
                .id(rs.getInt("idClue"))
                .idRoom(rs.getInt("idRoom"))
                .name(rs.getString("name"))
                .price(rs.getBigDecimal("price"))
                .isActive(rs.getBoolean("isActive"))
                .build();
    }
    @Override
    public List<Clue> findCluesByRoomId(Integer roomId) throws DAOException {
        String sql = "SELECT idClue, idRoom, name, description, price, isActive FROM clue WHERE idRoom = ? AND isActive = TRUE;";
        List<Clue> clues = new ArrayList<>();
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                clues.add(mapResultSetToClue(rs));
            }
        } catch (Exception e) {
            String messageError = "Error retrieving clues for room ID: " + roomId;
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
        return clues;
    }
    @Override
    public BigDecimal findPriceByRoomId(Integer roomId) throws DAOException {
        String sql = "SELECT COALESCE(SUM(price), 0) AS totalPrice FROM clue WHERE idRoom = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("totalPrice");
                }
            }
        } catch (SQLException e) {
            String errorMessage = "Error al obtener el precio total de las Clues para Room con ID: " + roomId;
            log.error(errorMessage, e);
            throw new DAOException(errorMessage, e);
        }
        return BigDecimal.ZERO;
    }

}

