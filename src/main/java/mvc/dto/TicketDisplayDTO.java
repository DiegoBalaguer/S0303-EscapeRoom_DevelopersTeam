package mvc.dto;

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
public class TicketDisplayDTO {
    private Integer id;
    private String name;
    private BigDecimal price;
    private Boolean isActive;
    private String description;

    private int getLong(int position) {
        return List.of(6, 30, 25, 10, 100).get(position);
    }

    public List<PairTextLength> toListHead() {
        List<PairTextLength> listValues = new ArrayList<>();
        int position = 0;;
        listValues.add(new PairTextLength("ID", getLong(position++)));
        listValues.add(new PairTextLength("NAME", getLong(position++)));
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
        listValues.add(new PairTextLength(String.valueOf(price), getLong(position++)));
        listValues.add(new PairTextLength(isActive ? "Yes" : "No", getLong(position++)));
        listValues.add(new PairTextLength(description, getLong(position++)));
        return listValues;
    }
}