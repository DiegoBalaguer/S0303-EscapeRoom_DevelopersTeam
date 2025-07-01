package mvc.dto;

import mvc.model.Notification;

public class NotificationMapper {

    public static NotificationDisplayDTO toDisplayDTO(Notification notification) {
        return NotificationDisplayDTO.builder()
                .id(notification.getIdPlayer())     // ID del jugador asociado a la notificación
                .message(notification.getMessage())
                .build();
    }
}