package mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utils.PairTextLength;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardDisplayDTO {
    private int id;
    private String name;
    private String description;
    private boolean isActive;

    private int getLong(int position) {
        return List.of(8, 25, 8, 100).get(position);
    }

    public List<PairTextLength> toListHead() {
        List<PairTextLength> listValues = new ArrayList<>();
        int position = 0;
        listValues.add(new PairTextLength("ID", getLong(position++)));
        listValues.add(new PairTextLength("NAME", getLong(position++)));
        listValues.add(new PairTextLength("ACTIVE", getLong(position++)));
        listValues.add(new PairTextLength("DESCRIPTION", getLong(position++)));
        return listValues;
    }

    public List<PairTextLength> toList() {
        List<PairTextLength> listValues = new ArrayList<>();
        int position = 0;
        listValues.add(new PairTextLength(String.valueOf(id), getLong(position++)));
        listValues.add(new PairTextLength(name, getLong(position++)));
        listValues.add(new PairTextLength(isActive ? "Yes" : "No", getLong(position++)));
        listValues.add(new PairTextLength(description, getLong(position++)));
        return listValues;
    }
}
