package dao.impl.h2;

import dao.exceptions.DAOException;
import dao.interfaces.BaseDAO;
import dao.interfaces.ClueDAO;
import lombok.extern.slf4j.Slf4j;
import model.Clue;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ClueDAOH2Impl implements BaseDAO<Clue, Integer>, ClueDAO {

    private final Connection connection;
    private static final String nameObject = "clue";

    public ClueDAOH2Impl() {
        this.connection = ConnectionDAOH2Impl.getConnection();
    }

    @Override
    public Clue create(Clue clue) throws DAOException {
        String sql = "INSERT INTO ? (idRoom, name, description, price, isActive) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nameObject);
            stmt.setInt(2, clue.getIdRoom());
            stmt.setString(3, clue.getName());
            stmt.setString(4, clue.getDescription());
            stmt.setBigDecimal(5, clue.getPrice());
            stmt.setBoolean(6, clue.isActive());
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
        String sql = "SELECT idClue, idRoom, name, price, isActive FROM ? WHERE idClue = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nameObject);
            stmt.setInt(2, id);
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
        String sql = "SELECT idClue, idRoom, name, price, isActive FROM " + nameObject;
        try (Statement stmt = connection.createStatement();
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
        String sql = "UPDATE ? SET idRoom = ?, name = ?, description = ?, price = ?, isActive = ? WHERE idClue = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nameObject);
            stmt.setInt(2, clue.getIdRoom());
            stmt.setString(3, clue.getName());
            stmt.setString(4, clue.getDescription());
            stmt.setBigDecimal(5, clue.getPrice());
            stmt.setBoolean(6, clue.isActive());
            stmt.setInt(7, clue.getId());

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
        String sql = "DELETE FROM ? WHERE idClue = ?";
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
        String sql = "SELECT 1 FROM ? WHERE idClue = ?";
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

    private Clue mapResultSetToClue(ResultSet rs) throws SQLException {
        return Clue.builder()
                .id(rs.getInt("idClue"))
                .idRoom(rs.getInt("idRoom"))
                .name(rs.getString("name"))
                .price(rs.getBigDecimal("price"))
                .isActive(rs.getBoolean("isActive"))
                .build();
    }
}

