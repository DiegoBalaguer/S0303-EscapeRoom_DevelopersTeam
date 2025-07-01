package mvc.dto;

import mvc.model.Notification;

public class NotificationMapper {

    public static NotificationDisplayDTO toDisplayDTO(Notification notification) {
        return NotificationDisplayDTO.builder()
                .id(notification.getIdNotification()) // Mapea el ID de la notificación
                .message(notification.getMessage())  // Mapea el mensaje
                .dateTime(notification.getDateTimeSent()) // Convierte LocalDateTime directamente
                .build();
    }
}