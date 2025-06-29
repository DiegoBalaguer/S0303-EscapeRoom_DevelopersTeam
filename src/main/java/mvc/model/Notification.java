package mvc.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Notification {
    private int id;
    private int idPlayer;
    private String message;
    private LocalDateTime dateTimeSent;
}