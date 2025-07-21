package mvc.entities.notification;

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
    private int id;
    private int idPlayer;
    private String message;
    private LocalDateTime dateTimeSent;
    private boolean isActive;
    private String player;

    private int getLong(int position) {
        return List.of(10, 30, 22, 8, 50).get(position);
    }

    public List<PairTextLength> toListHead() {
        List<PairTextLength> listValues = new ArrayList<>();
        int position = 0;
        listValues.add(new PairTextLength("Notification ID", getLong(position++)));
        listValues.add(new PairTextLength("Player", getLong(position++)));
        listValues.add(new PairTextLength("Date Sent", getLong(position++)));
        listValues.add(new PairTextLength("Active", getLong(position++)));
        listValues.add(new PairTextLength("Message", getLong(position++)));
        return listValues;
    }

    public List<PairTextLength> toList() {
        List<PairTextLength> listValues = new ArrayList<>();
        int position = 0;
        listValues.add(new PairTextLength(String.valueOf(id), getLong(position++)));
        listValues.add(new PairTextLength("0" + idPlayer + ".-" + player, getLong(position++)));
        listValues.add(new PairTextLength(dateTimeSent.toString(), getLong(position++)));
        listValues.add(new PairTextLength(isActive ? "Yes" : "No", getLong(position++)));
        listValues.add(new PairTextLength(message, getLong(position++)));

        return listValues;
    }
}