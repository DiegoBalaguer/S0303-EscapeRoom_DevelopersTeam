package dao.impl.h2;

import dao.exceptions.DAOException;
import dao.interfaces.BaseDAO;
import dao.interfaces.RoomDAO;
import enums.Difficulty;
import enums.Theme;
import lombok.extern.slf4j.Slf4j;
import model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class RoomDAOH2Impl implements BaseDAO<Room, Integer>, RoomDAO {

    private final Connection connection;
    private static final String nameObject = "room";

    public RoomDAOH2Impl() {
        this.connection = ConnectionDAOH2Impl.getConnection();
    }

    @Override
    public Room create(Room room) throws DAOException {
        String sql = "INSERT INTO ? (idEscapeRoom, name, description, price, idDifficulty, idTheme, isActive) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nameObject);
            stmt.setInt(2, room.getIdEscapeRoom());
            stmt.setString(3, room.getName());
            stmt.setString(4, room.getDescription());
            stmt.setBigDecimal(5, room.getPrice());
            stmt.setInt(6, room.getDifficulty().ordinal());
            stmt.setInt(7, room.getTheme().ordinal());
            stmt.setBoolean(8, room.isActive());
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                room.setId(keys.getInt(1));
            }
            return room;
        } catch (Exception e) {
            String messageError = "Error creating " + nameObject + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public Optional<Room> findById(Integer id) throws DAOException {
        String sql = "SELECT idRoom, name, idDifficulty, price, idTheme, isActive FROM ? WHERE idRoom = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nameObject);
            stmt.setInt(2, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToRoom(rs));
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
        String sql = "SELECT idRoom, name, idDifficulty, price, idTheme, isActive FROM " + nameObject;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
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
        String sql = "UPDATE ? SET name = ?, description = ?, price = ?, idDifficulty = ?, idTheme = ?, isActive = ? WHERE idRoom = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nameObject);
            stmt.setString(2, room.getName());
            stmt.setString(3, room.getDescription());
            stmt.setBigDecimal(4, room.getPrice());
            stmt.setInt(5, room.getDifficulty().ordinal());
            stmt.setInt(6, room.getTheme().ordinal());
            stmt.setBoolean(7, room.isActive());
            stmt.setInt(8, room.getId());

            int affected = stmt.executeUpdate();
            if (affected == 0) {
                String messageError = "No " + nameObject + " found to update with ID: " + room.getId();
                log.error(messageError);
                throw new DAOException(messageError);
            }
            return room;
        } catch (Exception e) {
            String messageError = "Error updating " + nameObject + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public void deleteById(Integer id) throws DAOException {
        String sql = "DELETE FROM ? WHERE idRoom = ?";
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
        String sql = "SELECT 1 FROM ? WHERE idRoom = ?";
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

    @Override
    public double calculateTotalRoomValue() throws DAOException {
        String sql = "SELECT SUM(price) AS total FROM " + nameObject;
        try (PreparedStatement stmt = connection.prepareStatement(sql);
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

    private Room mapResultSetToRoom(ResultSet rs) throws SQLException {
        return Room.builder()
                .id(rs.getInt("idRoom"))
                .name(rs.getString("name"))
                .difficulty(Difficulty.values()[rs.getInt("idDifficulty")])
                .price(rs.getBigDecimal("price"))
                .theme(Theme.values()[rs.getInt("idTheme")])
                .build();
    }
}
