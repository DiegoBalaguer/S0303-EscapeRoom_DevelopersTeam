package mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utils.PairTextLength;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDisplayDTO {
    private int id; // ID de la notificación
    private String message; // Mensaje de la notificación
    private LocalDateTime dateTime; // Fecha y hora de envío

    private int getLong(int position) {
        return List.of(10, 50, 20).get(position);
    }

    public List<PairTextLength> toListHead() {
        List<PairTextLength> listValues = new ArrayList<>();
        int position = 0;
        listValues.add(new PairTextLength("Notification ID", getLong(position++))); // Cambiado a ID de notificación
        listValues.add(new PairTextLength("Message", getLong(position++)));
        listValues.add(new PairTextLength("Date Sent", getLong(position++)));
        return listValues;
    }

    public List<PairTextLength> toList() {
        List<PairTextLength> listValues = new ArrayList<>();
        int position = 0;
        listValues.add(new PairTextLength(String.valueOf(id), getLong(position++)));
        listValues.add(new PairTextLength(message, getLong(position++)));
        listValues.add(new PairTextLength(dateTime.toString(), getLong(position++)));
        return listValues;
    }
}