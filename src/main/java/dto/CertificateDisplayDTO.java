package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateDisplayDTO {
    private int id;
    private String name;
    private String description;
    private boolean isActive;

    private int getLong(int position) {
        return List.of(8, 25, 8, 40).get(position);
    }

    public Map<String, Integer> toListHead() {
        Map<String, Integer> dataMap = new LinkedHashMap<>();
        int position = 0;
        dataMap.put("ID", getLong(position++));
        dataMap.put("NAME", getLong(position++));
        dataMap.put("ACTIVE", getLong(position++));
        dataMap.put("DESCRIPTION", getLong(position++));
        return dataMap;
    }

    public Map<String, Integer> toList() {
        Map<String, Integer> dataMap = new LinkedHashMap<>();
        int position = 0;
        dataMap.put(String.valueOf(id), getLong(position++));
        dataMap.put(name, getLong(position++));
        dataMap.put(isActive ? "Yes" : "No", getLong(position++));
        dataMap.put(description, getLong(position++));
        return dataMap;
    }
}
