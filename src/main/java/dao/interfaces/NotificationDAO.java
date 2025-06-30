package dao.interfaces;

import mvc.model.Notification;
import java.util.List;

public interface NotificationDAO {
    void saveNotification(Notification notification);
    List<Notification> findNotificationsByPlayerId(int playerId);
    List<Notification> findAllNotifications();
}