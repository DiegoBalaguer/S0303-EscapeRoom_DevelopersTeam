package mvc.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Data
@Builder
@Slf4j
public class Notification {
    private int idNotification;
    private int idPlayer;
    private String message;
    private LocalDateTime dateTimeSent;
    private boolean isActive;
}
