package dao.impl.h2;

import dao.exceptions.DAOException;
import dao.interfaces.BaseDAO;
import dao.interfaces.ClueDAO;
import dao.interfaces.ConnectionDAO;
import enums.Theme;
import lombok.extern.slf4j.Slf4j;
import mvc.dto.ClueDisplayDTO;
import mvc.model.Clue;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ClueDAOH2Impl implements BaseDAO<Clue, Integer>, ClueDAO {

    private final ConnectionDAO connectionDAO;
    private static final String NAME_OBJECT = "clue";

    public ClueDAOH2Impl(ConnectionDAO connectionDAO) {
        this.connectionDAO = connectionDAO;
    }

    @Override
    public Clue create(Clue clue) throws DAOException {
        String sql = "INSERT INTO " + NAME_OBJECT + " (idRoom, name, price, isActive, description) VALUES (?, ?, ?, ?, ?);";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, clue.getIdRoom());
            stmt.setString(2, clue.getName());
            stmt.setBigDecimal(3, clue.getPrice());
            stmt.setBoolean(4, clue.isActive());
            stmt.setString(5, clue.getDescription());
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                clue.setId(keys.getInt(1));
            }
            return clue;
        } catch (Exception e) {
            String messageError = "Error creating " + NAME_OBJECT + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public Optional<Clue> findById(Integer id) throws DAOException {
        String sql = "SELECT idClue, idRoom, name, price, isActive, description FROM " + NAME_OBJECT + " WHERE idClue = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(listResultSetToClue(rs));
            }
            return Optional.empty();
        } catch (Exception e) {
            String messageError = "Error finding " + NAME_OBJECT + " by ID: ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public List<Clue> findAll() throws DAOException {
        List<Clue> clues = new ArrayList<>();
        String sql = "SELECT idClue, idRoom, name, price, isActive, description FROM " + NAME_OBJECT + ";";
        try (Connection connection = connectionDAO.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clues.add(listResultSetToClue(rs));
            }
            return clues;
        } catch (SQLException e) {
            String messageError = "Error retrieving all " + NAME_OBJECT + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public Clue update(Clue clue) throws DAOException {
        String sql = "UPDATE " + NAME_OBJECT + " SET idRoom = ?, name = ?, description = ?, price = ?, isActive = ? WHERE idClue = ?;"; // Update description
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
                String messageError = "No " + NAME_OBJECT + " found to update with ID: " + clue.getId();
                log.error(messageError);
                throw new DAOException(messageError);
            }
            return clue;
        } catch (Exception e) {
            String messageError = "Error updating " + NAME_OBJECT + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public void deleteById(Integer id) throws DAOException {
        String sql = "DELETE FROM " + NAME_OBJECT + " WHERE idClue = ?;";
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
        String sql = "SELECT 1 FROM " + NAME_OBJECT + " WHERE idClue = ?;";
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
    public List<ClueDisplayDTO> findCluesByRoomId(Integer roomId) throws DAOException {
        List<ClueDisplayDTO> cluesDisplayDTO = new ArrayList<>();
        String sql = "SELECT " +
                "cu.idClue, cu.name, cu.idRoom, cu.price, cu.IsActive, cu.description, " +
                "r.name AS roomName, r.idTheme " +
                "FROM Clue cu " +
                "JOIN Room r ON cu.idRoom = r.idRoom " +
                "WHERE cu.idRoom = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cluesDisplayDTO.add(listResultSetToClueDisplayDTO(rs));
            }
        } catch (Exception e) {
            String messageError = "Error retrieving clues for room ID: " + roomId;
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
        return cluesDisplayDTO;
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
            String errorMessage = "Error getting total price of Clues for Room with ID: " + roomId;
            log.error(errorMessage, e);
            throw new DAOException(errorMessage, e);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public List<ClueDisplayDTO> findAllCluesCompleteInfo() throws DAOException {
        List<ClueDisplayDTO> cluesDisplayDTO = new ArrayList<>();
        String sql = "SELECT " +
                "cu.idClue, cu.name, cu.idRoom, cu.price, cu.IsActive, cu.description, " +
                "r.name AS roomName, r.idTheme " +
                "FROM Clue cu " +
                "JOIN Room r ON cu.idRoom = r.idRoom;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cluesDisplayDTO.add(listResultSetToClueDisplayDTO(rs));
            }
        } catch (SQLException e) {
            String messageError = "Error retrieving complete clues";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
        return cluesDisplayDTO;
    }

    private Clue listResultSetToClue(ResultSet rs) throws SQLException {
        return Clue.builder()
                .id(rs.getInt("idClue"))
                .idRoom(rs.getInt("idRoom"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .price(rs.getBigDecimal("price"))
                .isActive(rs.getBoolean("isActive"))
                .build();
    }

    private ClueDisplayDTO listResultSetToClueDisplayDTO(ResultSet rs) throws SQLException {
        return ClueDisplayDTO.builder()
                .id(rs.getInt("idClue"))
                .name(rs.getString("name"))
                .idRoom(rs.getInt("idRoom"))
                .room(rs.getString("roomName"))
                .idTheme(rs.getInt("idTheme"))
                .theme(Theme.values()[rs.getInt("idTheme")])
                .price(rs.getBigDecimal("price"))
                .isActive(rs.getBoolean("isActive"))
                .description(rs.getString("description"))
                .build();
    }
}

