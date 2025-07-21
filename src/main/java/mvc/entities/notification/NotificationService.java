package mvc.entities.notification;

import dao.exceptions.DAOException;
import dao.exceptions.NotFoundException;
import mvc.entities.MessageMinMax;
import mvc.entities.player.PlayerService;
import utils.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class NotificationService {

    private final NotificationDAO NOTIFICATION_DAO;
    private final PlayerService PLAYER_SERVICE;
    private static final String NAME_OBJECT = "Notification";

    public NotificationService(NotificationDAO notificationDAO, PlayerService playerService) {
        this.NOTIFICATION_DAO = notificationDAO;
        this.PLAYER_SERVICE = playerService;
    }

    public Notification createNotification(Optional<Integer> playerId, Optional<String> message) throws IllegalArgumentException, DAOException {
        if (playerId.isEmpty() || message.isEmpty()) {
            throw new IllegalArgumentException("The " + NAME_OBJECT + " name cannot be empty.");
        }
        Notification notification = Notification.builder()
                .idPlayer(playerId.get())
                .message(message.get())
                .dateTimeSent(LocalDateTime.now())
                .isActive(true)
                .build();
        return NOTIFICATION_DAO.create(notification);
    }

    protected Optional<Notification> getNotificationById(int id) throws DAOException {
        return NOTIFICATION_DAO.findById(id);
    }


    public MessageMinMax getAllNotificationsCompleteInfoDto() {
        List<NotificationDisplayDTO> notifications = NOTIFICATION_DAO.findAllCompleteInfo();
        return new MessageMinMax(displayNotificationListDto(notifications), notifications.isEmpty() ? 0 : 1, notifications.size());
    }

    protected List<NotificationDisplayDTO> getAllNotificationsCompleteInfo() throws DAOException {
        return NOTIFICATION_DAO.findAllCompleteInfo();
    }

    protected List<NotificationDisplayDTO> getNotificationsByPlayerId(int playerId) throws DAOException {
        return NOTIFICATION_DAO.findAllCompleteInfoByPlayerId(playerId);
    }

    protected Notification updateNotification(Notification notification) throws IllegalArgumentException, NotFoundException, DAOException {
        if (notification == null || notification.getId() <= 0) {
            throw new IllegalArgumentException("The " + NAME_OBJECT + " or its ID is not valid for the update.");
        }
        if (NOTIFICATION_DAO.findById(notification.getId()).isEmpty()) {
            throw new NotFoundException(NAME_OBJECT + " with ID " + notification.getId() + " not found for update.");
        }
        return NOTIFICATION_DAO.update(notification);
    }

    protected void deleteNotification(int id) throws NotFoundException, DAOException {
        if (NOTIFICATION_DAO.findById(id).isEmpty()) {
            throw new NotFoundException(NAME_OBJECT + " with ID " + id + " not found for deletion.");
        }
        NOTIFICATION_DAO.deleteById(id);
    }

    protected Notification softDeleteNotification(int id) throws NotFoundException, DAOException {
        Optional<Notification> existingNotificationOpt = NOTIFICATION_DAO.findById(id);
        if (existingNotificationOpt.isEmpty()) {
            throw new NotFoundException(NAME_OBJECT + " with ID " + id + " not found for soft deletion.");
        }
        Notification notificationToSoftDelete = existingNotificationOpt.get();
        notificationToSoftDelete.setActive(false);
        return NOTIFICATION_DAO.update(notificationToSoftDelete);
    }

    protected String displayNotificationListDto(List<NotificationDisplayDTO> notificationDisplayDTOS) {
        StringBuilder message = new StringBuilder();
        if (notificationDisplayDTOS.isEmpty()) {
            return "No " + NAME_OBJECT + " found.";
        }
        message.append(
                StringUtils.makeLineToList(notificationDisplayDTOS.getFirst().toListHead()));

        notificationDisplayDTOS.forEach(rewardWins -> message.append(
                StringUtils.makeLineToList(rewardWins.toList())).append(System.lineSeparator()));
        message.append("-------------------").append(System.lineSeparator());
        return message.toString();
    }

    protected MessageMinMax getAllPlayers() {
        return PLAYER_SERVICE.getAllPlayers();
    }
}
