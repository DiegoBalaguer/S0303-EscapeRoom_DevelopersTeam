package dao.impl.h2;

import dao.exceptions.DAOException;
import dao.interfaces.BaseDAO;
import dao.interfaces.ConnectionDAO;
import dao.interfaces.RoomDAO;
import enums.Difficulty;
import enums.Theme;
import lombok.extern.slf4j.Slf4j;
import mvc.model.Room;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class RoomDAOH2Impl implements BaseDAO<Room, Integer>, RoomDAO {

    private final ConnectionDAO connectionDAO;
    private static final String nameObject = "room";

    public RoomDAOH2Impl(ConnectionDAO connectionDAO) {
        this.connectionDAO = connectionDAO;
    }

    @Override
    public Room create(Room room) throws DAOException {
        String sql = "INSERT INTO " + nameObject + " (idEscapeRoom, name, description, idTheme, idDifficulty, price, isActive) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, room.getIdEscapeRoom());
            stmt.setString(2, room.getName());
            stmt.setString(3, room.getDescription());
            if (room.getTheme() == null) {
                throw new DAOException("Theme in Room cannot be null.");
            }
            stmt.setInt(4, room.getTheme().ordinal());
            stmt.setInt(5, room.getDifficulty().ordinal());
            stmt.setBigDecimal(6, room.getPrice());
            stmt.setBoolean(7, room.isActive());
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                room.setId(keys.getInt(1));
            }
            return room;

        } catch (Exception e) {
            log.error("Error creating room: ", e);
            throw new DAOException("Error creating room: ", e);
        }
    }

    @Override
    public Optional<Room> findById(Integer id) throws DAOException {
        String sql = "SELECT idRoom, idEscapeRoom, name, idTheme, idDifficulty, price, isActive, description FROM " + nameObject + " WHERE idRoom = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(listResultSetToRoom(rs));
            }
            return Optional.empty();
        } catch (Exception e) {
            String messageError = "Error finding " + nameObject + " by ID: ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public List<Room> findAll() throws DAOException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT idRoom, idEscapeRoom, name, idTheme, idDifficulty, price, isActive, description FROM " + nameObject + ";";
        try (Connection connection = connectionDAO.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rooms.add(listResultSetToRoom(rs));
            }
            return rooms;
        } catch (SQLException e) {
            String messageError = "Error retrieving all " + nameObject + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public Room update(Room room) throws DAOException {
        // Primero obtenemos toda la información del registro actual
        Optional<Room> existingRoomOptional = findById(room.getId());
        if (existingRoomOptional.isEmpty()) {
            String messageError = "No " + nameObject + " found to update with ID: " + room.getId();
            log.error(messageError);
            throw new DAOException(messageError);
        }

        Room existingRoom = existingRoomOptional.get();

        // Conservamos los valores actuales si los campos en el objeto 'room' son nulos o vacíos
        String newName = room.getName() != null ? room.getName() : existingRoom.getName();
        String newDescription = room.getDescription() != null ? room.getDescription() : existingRoom.getDescription();
        BigDecimal newPrice = room.getPrice() != null ? room.getPrice() : existingRoom.getPrice();
        Difficulty newDifficulty = room.getDifficulty() != null ? room.getDifficulty() : existingRoom.getDifficulty();
        Theme newTheme = room.getTheme() != null ? room.getTheme() : existingRoom.getTheme();
        boolean newIsActive = room.isActive() || existingRoom.isActive(); // manejar campo booleano

        // Actualización en la base de datos
        String sql = "UPDATE " + nameObject + " SET name = ?, description = ?, price = ?, idDifficulty = ?, idTheme = ?, isActive = ? WHERE idRoom = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setString(2, newDescription);
            stmt.setBigDecimal(3, newPrice);
            stmt.setInt(4, newDifficulty.ordinal());
            stmt.setInt(5, newTheme.ordinal());
            stmt.setBoolean(6, newIsActive);
            stmt.setInt(7, room.getId());

            int affected = stmt.executeUpdate();
            if (affected == 0) {
                String messageError = "No " + nameObject + " found to update with ID: " + room.getId();
                log.error(messageError);
                throw new DAOException(messageError);
            }
            existingRoom.setName(newName);
            existingRoom.setDescription(newDescription);
            existingRoom.setPrice(newPrice);
            existingRoom.setDifficulty(newDifficulty);
            existingRoom.setTheme(newTheme);
            existingRoom.setActive(newIsActive);

            return existingRoom;
        } catch (Exception e) {
            String messageError = "Error updating " + nameObject + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public void deleteById(Integer id) throws DAOException {
        String sql = "DELETE FROM " + nameObject + " WHERE idRoom = ?;";
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
        String sql = "SELECT 1 FROM " + nameObject + " WHERE idRoom = ?;";
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

    @Override
    public double calculateTotalRoomValue() throws DAOException {
        String sql = "SELECT SUM(price) AS total FROM " + nameObject + ";";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
            return 0.0;

        } catch (Exception e) {
            String messageError = "Error calculating total " + nameObject + " value";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    public void addClueToRoom(Integer roomId, Integer clueId) throws DAOException {}
    public void addDecorationToRoom(Integer roomId, Integer decorationId) throws DAOException {}

    private Room listResultSetToRoom(ResultSet rs) throws SQLException {
        return Room.builder()
                .id(rs.getInt("idRoom"))
                .idEscapeRoom(rs.getInt("idEscapeRoom"))
                .name(rs.getString("name"))
                .theme(Theme.values()[rs.getInt("idTheme")])
                .difficulty(Difficulty.values()[rs.getInt("idDifficulty")])
                .price(rs.getBigDecimal("price"))
                .active(rs.getBoolean("isActive"))
                .description(rs.getString("description"))
                .build();
    }
}
