package mvc.entities.notification;

public class NotificationMapper {

    public static NotificationDisplayDTO toDisplayDTO(Notification notification) {
        return NotificationDisplayDTO.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .dateTimeSent(notification.getDateTimeSent())
                .build();
    }
}