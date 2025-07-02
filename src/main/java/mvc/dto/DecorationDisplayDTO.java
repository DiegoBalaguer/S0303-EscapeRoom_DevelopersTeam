package mvc.dto;

import enums.Theme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utils.PairTextLength;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DecorationDisplayDTO {
    private int id;
    private String name;
    private BigDecimal price;
    private int idRoom;
    private boolean isActive;
    private String room;
    private int idTheme;
    private Theme theme;
    private String description;

    private int getLong(int position) {
        return List.of(6, 20, 35, 15, 10, 8, 100).get(position);
    }

    public List<PairTextLength> toListHead() {
        List<PairTextLength> listValues = new ArrayList<>();
        int position = 0;
        listValues.add(new PairTextLength("ID", getLong(position++)));
        listValues.add(new PairTextLength("NAME", getLong(position++)));
        listValues.add(new PairTextLength("ROOM", getLong(position++)));
        listValues.add(new PairTextLength("THEME", getLong(position++)));
        listValues.add(new PairTextLength("PRICE", getLong(position++)));
        listValues.add(new PairTextLength("ACTIVE", getLong(position++)));
        listValues.add(new PairTextLength("DESCRIPTION", getLong(position++)));
        return listValues;
    }

    public List<PairTextLength> toList() {
        List<PairTextLength> listValues = new ArrayList<>();
        int position = 0;
        listValues.add(new PairTextLength(String.valueOf(id), getLong(position++)));
        listValues.add(new PairTextLength(name, getLong(position++)));
        listValues.add(new PairTextLength("0" + idRoom + ".-" + room, getLong(position++)));
        listValues.add(new PairTextLength("0" + idTheme + ".-" + theme, getLong(position++)));
        listValues.add(new PairTextLength(String.valueOf(price),  getLong(position++)));
        listValues.add(new PairTextLength(isActive ? "Yes" : "No", getLong(position++)));
        listValues.add(new PairTextLength(description, getLong(position++)));
        return listValues;
    }
}