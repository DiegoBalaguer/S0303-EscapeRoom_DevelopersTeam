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
    public void saveNotification(Notification notification) throws DAOException {
        String sql = "INSERT INTO notifications (idPlayer, message, dateTimeSent, isActive) VALUES (?, ?, ?, ?)";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, notification.getIdPlayer()); // ID del jugador
            stmt.setString(2, notification.getMessage()); // Mensaje
            stmt.setTimestamp(3, Timestamp.valueOf(notification.getDateTimeSent())); // Fecha y hora
            stmt.setBoolean(4, notification.isActive()); // Activa

            int rowsInserted = stmt.executeUpdate();
            System.out.println("Rows inserted: " + rowsInserted);
        } catch (SQLException e) {
            log.error("Error saving notification", e);
            throw new DAOException("Error saving notification", e);
        }
    }
    /*@Override
    public void saveNotification(Notification notification) throws DAOException {
        String sql = "INSERT INTO notifications (idPlayer, message, dateTimeSent, isActive) VALUES (?, ?, ?, ?)";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, notification.getIdPlayer());
            stmt.setString(2, notification.getMessage());
            stmt.setTimestamp(3, Timestamp.valueOf(notification.getDateTimeSent()));
            stmt.setBoolean(4, true);
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error saving notification: ", e);
            throw new DAOException("Error saving notification", e);
        }
    }*/

    @Override
    public List<Notification> findAllNotifications() throws DAOException {
        String sql = "SELECT idNotification, idPlayer, message, dateTimeSent, isActive FROM notifications WHERE isActive = TRUE";
        List<Notification> notifications = new ArrayList<>();

        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Notification notification = Notification.builder()
                        .idNotification(rs.getInt("idNotification"))
                        .idPlayer(rs.getInt("idPlayer"))
                        .message(rs.getString("message"))
                        .dateTimeSent(rs.getTimestamp("dateTimeSent").toLocalDateTime())
                        .isActive(rs.getBoolean("isActive"))
                        .build();
                notifications.add(notification);
            }
        } catch (SQLException e) {
            log.error("Error retrieving notifications: ", e);
            throw new DAOException("Error retrieving notifications", e);
        }

        return notifications;
    }

    private Notification mapResultSetToNotification(ResultSet rs) throws SQLException {
        return Notification.builder()
                .idNotification(rs.getInt("idNotification"))
                .idPlayer(rs.getInt("idPlayer"))
                .message(rs.getString("message"))
                .dateTimeSent(rs.getTimestamp("dateTimeSent").toLocalDateTime())
                .build();
    }
}