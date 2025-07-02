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
public class InventoryDisplayDTO {
    private String inventory;
    private int id;
    private String name;
    private BigDecimal price;

    private int getLong(int position) {
        return List.of(30, 6, 100, 20).get(position);
    }

    public List<PairTextLength> toListHead() {
        List<PairTextLength> listValues = new ArrayList<>();
        int position = 0;
        listValues.add(new PairTextLength("INVENTORY", getLong(position++)));
        listValues.add(new PairTextLength("ID", getLong(position++)));
        listValues.add(new PairTextLength("NAME", getLong(position++)));
        listValues.add(new PairTextLength("PRICE", getLong(position++)));
        return listValues;
    }

    public List<PairTextLength> toList() {
        List<PairTextLength> listValues = new ArrayList<>();
        int position = 0;

        if ("TOTAL".equalsIgnoreCase(inventory)) {
            listValues.add(new PairTextLength(inventory, getLong(position++)));
            listValues.add(new PairTextLength("-", getLong(position++)));
            listValues.add(new PairTextLength("-", getLong(position++)));
            listValues.add(new PairTextLength("$" + price.toString(), getLong(position++)));
        } else {
            listValues.add(new PairTextLength(inventory, getLong(position++)));
            listValues.add(new PairTextLength(String.valueOf(id), getLong(position++)));
            listValues.add(new PairTextLength(name, getLong(position++)));
            listValues.add(new PairTextLength("$" + price.toString(), getLong(position++)));
        }

        return listValues;
    }
}