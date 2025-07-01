package dao.interfaces;

import dao.exceptions.DAOException;
import mvc.model.Notification;
import java.util.List;

public interface NotificationDAO {
    void saveNotification(Notification notification);
    List<Notification> findAllNotifications() throws DAOException;
}