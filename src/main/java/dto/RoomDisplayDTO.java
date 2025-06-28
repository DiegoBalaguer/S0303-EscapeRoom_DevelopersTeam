package dto;

import enums.Difficulty;
import enums.Theme;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDisplayDTO {
    private int id;
    private int idEscapeRoom;
    private String name;
    private Theme theme;
    private Difficulty difficulty;
    private BigDecimal price;
    private boolean isActive;
    private String description;

    private int getLong(int position) {
        return List.of(8, 25, 35, 25, 24, 8, 8).get(position);
    }

    public Map<String, Integer> toListHead() {
        Map<String, Integer> dataMap = new LinkedHashMap<>();
        int position = 0;
        dataMap.put("ID", getLong(position++));
        dataMap.put("NAME", getLong(position++));
        dataMap.put("THEME", getLong(position++));
        dataMap.put("DIFFICULTY", getLong(position++));
        dataMap.put("PRICE", getLong(position++));
        dataMap.put("ACTIVE", getLong(position++));
        dataMap.put("DESCRIPTION", getLong(position++));
        return dataMap;
    }

    public Map<String, Integer> toList() {
        Map<String, Integer> dataMap = new LinkedHashMap<>();
        int position = 0;
        dataMap.put(String.valueOf(id), getLong(position++));
        dataMap.put(name, getLong(position++));
        dataMap.put(theme.name(), getLong(position++));
        dataMap.put(difficulty.name(), getLong(position++));
        dataMap.put(String.valueOf(price), getLong(position++));
        dataMap.put(isActive ? "Yes" : "No", getLong(position++));
        dataMap.put(description, getLong(position++));
        return dataMap;
    }
}
