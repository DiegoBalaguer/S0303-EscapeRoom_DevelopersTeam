package mvc.entities.notification;

import dao.exceptions.DAOException;
import dao.interfaces.BaseDAO;
import dao.interfaces.ConnectionDAO;
import lombok.extern.slf4j.Slf4j;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class NotificationDAOH2Impl implements BaseDAO<Notification, Integer>, NotificationDAO {

    private final ConnectionDAO connectionDAO;
    private static final String NAME_OBJECT = "notification";

    public NotificationDAOH2Impl(ConnectionDAO connectionDAO) {
        this.connectionDAO = connectionDAO;
    }

    @Override
    public Notification create(Notification notification) throws DAOException {
        String sql = "INSERT INTO " + NAME_OBJECT + " (idPlayer, dateTimeSent, isActive, message) VALUES (?, ?, ?, ?);";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, notification.getIdPlayer());
            stmt.setObject(2, notification.getDateTimeSent());
            stmt.setBoolean(3, notification.isActive());
            stmt.setString(4, notification.getMessage());
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                notification.setId(keys.getInt(1));
            }
            return notification;
        } catch (Exception e) {
            String messageError = "Error creating " + NAME_OBJECT + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public Optional<Notification> findById(Integer id) throws DAOException {
        String sql = "SELECT idNotification, idPlayer, dateTimeSend, isActive, message FROM " + NAME_OBJECT + " WHERE idNotification = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(listResultSetToNotification(rs));
            }
            return Optional.empty();
        } catch (Exception e) {
            String messageError = "Error finding " + NAME_OBJECT + " by ID: ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public List<Notification> findAll() throws DAOException {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT idNotification, idPlayer, dateTimeSend, isActive, message FROM " + NAME_OBJECT + ";";
        try (Connection connection = connectionDAO.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                notifications.add(listResultSetToNotification(rs));
            }
            return notifications;
        } catch (SQLException e) {
            String messageError = "Error retrieving all " + NAME_OBJECT + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public Notification update(Notification notification) throws DAOException {
        String sql = "UPDATE " + NAME_OBJECT + " SET idPlayer = ?, message = ?, dateTimeSent = ?, isActive = ? WHERE idNotification = ?;"; // Update description
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, notification.getIdPlayer());
            stmt.setString(2, notification.getMessage());
            stmt.setObject(3, notification.getDateTimeSent());
            stmt.setBoolean(4, notification.isActive());
            stmt.setInt(5, notification.getId());

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                String messageError = "No " + NAME_OBJECT + " found to update with ID: " + notification.getId();
                log.error(messageError);
                throw new DAOException(messageError);
            }
            return notification;
        } catch (Exception e) {
            String messageError = "Error updating " + NAME_OBJECT + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public void deleteById(Integer id) throws DAOException {
        String sql = "DELETE FROM " + NAME_OBJECT + " WHERE idNotification = ?;";
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
        String sql = "SELECT 1 FROM " + NAME_OBJECT + " WHERE idNotification = ?;";
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
    public List<NotificationDisplayDTO> findAllCompleteInfoByPlayerId(Integer playerId) throws DAOException {
        List<NotificationDisplayDTO> notificationsDisplayDTO = new ArrayList<>();
        String sql = "SELECT " +
                "n.idNotification, n.idPlayer, n.dateTimeSent, n.IsActive, n.message, " +
                "p.name AS playerName " +
                "FROM " + NAME_OBJECT + " n " +
                "JOIN player p ON n.idPlayer = p.idPlayer " +
                "WHERE n.idPlayer = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, playerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notificationsDisplayDTO.add(listResultSetToNotificationDisplayDTO(rs));
            }
        } catch (Exception e) {
            String messageError = "Error retrieving notifications for room ID: " + playerId;
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
        return notificationsDisplayDTO;
    }

    @Override
    public List<NotificationDisplayDTO> findAllCompleteInfo() throws DAOException {
        List<NotificationDisplayDTO> notificationsDisplayDTO = new ArrayList<>();
        String sql = "SELECT " +
                "n.idNotification, n.idPlayer, n.dateTimeSent, n.IsActive, n.message, " +
                "p.name AS playerName " +
                "FROM " + NAME_OBJECT + " n " +
                "JOIN player p ON n.idPlayer = p.idPlayer;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notificationsDisplayDTO.add(listResultSetToNotificationDisplayDTO(rs));
            }
        } catch (SQLException e) {
            String messageError = "Error retrieving complete notifications";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
        return notificationsDisplayDTO;
    }

    private Notification listResultSetToNotification(ResultSet rs) throws SQLException {
        return Notification.builder()
                .id(rs.getInt("idNotification"))
                .idPlayer(rs.getInt("idPlayer"))
                .message(rs.getString("message"))
                .dateTimeSent(rs.getTimestamp("dateTimeSent").toLocalDateTime())
                .isActive(rs.getBoolean("isActive"))
                .build();
    }

    private NotificationDisplayDTO listResultSetToNotificationDisplayDTO(ResultSet rs) throws SQLException {
        return NotificationDisplayDTO.builder()
                .id(rs.getInt("idNotification"))
                .idPlayer(rs.getInt("idPlayer"))
                .player(rs.getString("playerName"))
                .dateTimeSent(rs.getTimestamp("dateTimeSent").toLocalDateTime())
                .isActive(rs.getBoolean("isActive"))
                .message(rs.getString("message"))
                .build();
    }
}