package dao.impl.h2;

import dao.exceptions.DAOException;
import dao.interfaces.ConnectionDAO;
import dao.interfaces.NotificationDAO;
import lombok.extern.slf4j.Slf4j;
import mvc.model.Notification;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class NotificationDAOH2Impl implements NotificationDAO {
    private final ConnectionDAO connectionDAO;

    public NotificationDAOH2Impl(ConnectionDAO connectionDAO) {
        this.connectionDAO = connectionDAO;
    }

    @Override
    public void saveNotification(Notification notification) {
        String sql = "INSERT INTO notifications (idPlayer, message) VALUES (?, ?);";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, notification.getIdPlayer());
            stmt.setString(2, notification.getMessage());
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error saving notification: {}", e.getMessage());
            throw new DAOException("Failed to save notification.", e);
        }
    }

    @Override
    public List<Notification> findNotificationsByPlayerId(int playerId) {
        String sql = "SELECT * FROM notifications WHERE idPlayer = ? ORDER BY dateTimeSent DESC;";
        List<Notification> notifications = new ArrayList<>();
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, playerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notifications.add(mapResultSetToNotification(rs));
            }
        } catch (SQLException e) {
            log.error("Error retrieving notifications for player {}: {}", playerId, e.getMessage());
            throw new DAOException("Failed to query notifications for player.", e);
        }
        return notifications;
    }

    @Override
    public List<Notification> findAllNotifications() {
        String sql = "SELECT * FROM notifications ORDER BY dateTimeSent DESC;";
        List<Notification> notifications = new ArrayList<>();
        try (Connection connection = connectionDAO.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                notifications.add(mapResultSetToNotification(rs));
            }
        } catch (SQLException e) {
            log.error("Error retrieving all notifications: {}", e.getMessage());
            throw new DAOException("Failed to query notifications.", e);
        }
        return notifications;
    }

    private Notification mapResultSetToNotification(ResultSet rs) throws SQLException {
        return Notification.builder()
                .id(rs.getInt("idNotification"))
                .idPlayer(rs.getInt("idPlayer"))
                .message(rs.getString("message"))
                .dateTimeSent(rs.getTimestamp("dateTimeSent").toLocalDateTime())
                .build();
    }
}