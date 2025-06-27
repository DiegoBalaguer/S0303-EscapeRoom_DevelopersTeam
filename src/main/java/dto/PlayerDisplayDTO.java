package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utils.StringUtils;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerDisplayDTO {
    private int id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime registrationDate;
    private boolean isSubscribed;
    private boolean isActive;

    private int getLong(int position) {
        return List.of(8, 25, 35, 25, 24, 8, 8).get(position);
    }

    public Map<String, Integer> toListHead() {
        Map<String, Integer> dataMap = new LinkedHashMap<>();
        int position = 0;
        dataMap.put("ID", getLong(position++));
        dataMap.put("NAME", getLong(position++));
        dataMap.put("EMAIL", getLong(position++));
        dataMap.put("PASSWORD", getLong(position++));
        dataMap.put("REG.DATE", getLong(position++));
        dataMap.put("SUBSCRIBE", getLong(position++));
        dataMap.put("ACTIVE", getLong(position++));
        return dataMap;
    }

    public Map<String, Integer> toList() {
        Map<String, Integer> dataMap = new LinkedHashMap<>();
        int position = 0;
        dataMap.put(String.valueOf(id), getLong(position++));
        dataMap.put(name, getLong(position++));
        dataMap.put(email, getLong(position++));
        dataMap.put(password, getLong(position++));
        dataMap.put(StringUtils.getDateFormatUSA(registrationDate), getLong(position++));
        dataMap.put(isSubscribed ? "Yes" : "No", getLong(position++));
        dataMap.put(isActive ? "Yes" : "No", getLong(position++));
        return dataMap;
    }
}
