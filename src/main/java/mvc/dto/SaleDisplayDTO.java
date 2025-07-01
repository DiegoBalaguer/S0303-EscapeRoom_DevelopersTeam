package mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import utils.PairTextLength;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class SaleDisplayDTO {
    private int id;
    private int idTicket;
    private int idPlayer;
    private int idRoom;
    private int players;
    private BigDecimal price;
    private int completion;
    private LocalDate dateSale;
    private boolean isActive;

    private int getLong(int position) {
        return List.of(8, 25, 15, 15, 10, 8, 10, 15, 8).get(position);
    }

        public List<PairTextLength> toListHead() {
        List<PairTextLength> listValues = new ArrayList<>();
        int position = 0;
        listValues.add(new PairTextLength("ID", getLong(position++)));
        listValues.add(new PairTextLength("TICKET ID", getLong(position++)));
        listValues.add(new PairTextLength("PLAYER ID", getLong(position++)));
        listValues.add(new PairTextLength("ROOM ID", getLong(position++)));
        listValues.add(new PairTextLength("PLAYERS", getLong(position++)));
        listValues.add(new PairTextLength("PRICE", getLong(position++)));
        listValues.add(new PairTextLength("COMPLETION", getLong(position++)));
        listValues.add(new PairTextLength("SALE DATE", getLong(position++)));
        listValues.add(new PairTextLength("ACTIVE", getLong(position++)));
        return listValues;
    }

    public List<PairTextLength> toList() {
        List<PairTextLength> listValues = new ArrayList<>();
        int position = 0;
        listValues.add(new PairTextLength(String.valueOf(id), getLong(position++)));
        listValues.add(new PairTextLength(String.valueOf(idTicket), getLong(position++)));
        listValues.add(new PairTextLength(String.valueOf(idPlayer), getLong(position++)));
        listValues.add(new PairTextLength(String.valueOf(idRoom), getLong(position++)));
        listValues.add(new PairTextLength(String.valueOf(players), getLong(position++)));
        listValues.add(new PairTextLength(String.valueOf(price), getLong(position++)));
        listValues.add(new PairTextLength(String.valueOf(completion), getLong(position++)));
        listValues.add(new PairTextLength(String.valueOf(dateSale), getLong(position++)));
        listValues.add(new PairTextLength(isActive ? "Yes" : "No", getLong(position++)));
        return listValues;
    }
}
