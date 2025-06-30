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
    private String inventory; // Tipo: 'room', 'clue', 'decoration' o 'TOTAL'
    private int id;           // ID del inventario, será 0 para 'TOTAL'
    private String name;      // Nombre del ítem, será vacío para 'TOTAL'
    private BigDecimal price; // Precio del ítem o suma total en el caso de 'TOTAL'

    // Define el ancho de cada columna
    private int getLong(int position) {
        return List.of(10, 6, 20, 10).get(position); // Define los anchos de las columnas
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