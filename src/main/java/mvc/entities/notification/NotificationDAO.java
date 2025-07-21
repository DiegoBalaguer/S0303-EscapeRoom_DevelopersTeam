package mvc.entities.notification;

import dao.exceptions.DAOException;
import dao.interfaces.BaseDAO;

import java.util.List;

public interface NotificationDAO extends BaseDAO<Notification, Integer> {
    List<NotificationDisplayDTO> findAllCompleteInfo() throws DAOException;
    List<NotificationDisplayDTO> findAllCompleteInfoByPlayerId(Integer playerId) throws DAOException;
}
