package mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utils.PairTextLength;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleDisplayDTO {
    private int id;
    private int idTicket;
    private int idPlayer;
    private int idRoom;
    private int players;
    private BigDecimal price;
    private int completion;
    private LocalDateTime dateSale;
    private boolean isActive;
    private String ticketName;
    private String playerName;
    private String roomName;

    private int getLong(int position) {
        return List.of(8, 20, 25, 35, 8, 10, 25, 8).get(position);
    }

        public List<PairTextLength> toListHead() {
        List<PairTextLength> listValues = new ArrayList<>();
        int position = 0;
        listValues.add(new PairTextLength("ID", getLong(position++)));
        listValues.add(new PairTextLength("TICKET", getLong(position++)));
        listValues.add(new PairTextLength("PLAYER", getLong(position++)));
        listValues.add(new PairTextLength("ROOM", getLong(position++)));
        listValues.add(new PairTextLength("PLAYERS", getLong(position++)));
        listValues.add(new PairTextLength("PRICE", getLong(position++)));
       // listValues.add(new PairTextLength("COMPLETION", getLong(position++)));
        listValues.add(new PairTextLength("SALE DATE", getLong(position++)));
        listValues.add(new PairTextLength("ACTIVE", getLong(position++)));
        return listValues;
    }

    public List<PairTextLength> toList() {
        List<PairTextLength> listValues = new ArrayList<>();
        int position = 0;
        listValues.add(new PairTextLength(String.valueOf(id), getLong(position++)));
        listValues.add(new PairTextLength("0" + idTicket + ".-" + ticketName, getLong(position++)));
        listValues.add(new PairTextLength("0" + idPlayer + ".-" + playerName, getLong(position++)));
        listValues.add(new PairTextLength("0" + idRoom + ".-" + roomName, getLong(position++)));
        listValues.add(new PairTextLength(String.valueOf(players), getLong(position++)));
        listValues.add(new PairTextLength(String.valueOf(price), getLong(position++)));
        //listValues.add(new PairTextLength(String.valueOf(completion), getLong(position++)));
        listValues.add(new PairTextLength(String.valueOf(dateSale), getLong(position++)));
        listValues.add(new PairTextLength(isActive ? "Yes" : "No", getLong(position++)));
        return listValues;
    }
}
