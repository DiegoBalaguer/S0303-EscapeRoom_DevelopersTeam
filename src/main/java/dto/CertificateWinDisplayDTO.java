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
public class CertificateWinDisplayDTO {
    private Integer id;
    private Integer idCertificate;
    private Integer idPlayer;
    private Integer idRoom;
    private String description;
    private LocalDateTime dateDelivery;
    private Boolean isActive;
    private String certificateName;
    private String roomName;

    private int getLong(int position) {
        return List.of(6, 30, 35, 25, 10, 40).get(position);
    }

    public Map<String, Integer> toListHead() {
        Map<String, Integer> dataMap = new LinkedHashMap<>();
        int position = 0;
        dataMap.put("ID", getLong(position++));
        dataMap.put("CERTIFICATE", getLong(position++));
        dataMap.put("ROOM", getLong(position++));
        dataMap.put("DATE DELIVERY", getLong(position++));
        dataMap.put("ACTIVE", getLong(position++));
        dataMap.put("DESCRIPTION", getLong(position++));
        return dataMap;
    }

    public Map<String, Integer> toList() {
        Map<String, Integer> dataMap = new LinkedHashMap<>();
        int position = 0;
        dataMap.put(String.valueOf(id), getLong(position++));
        dataMap.put("0" + idCertificate + ".-" + certificateName, getLong(position++));
        dataMap.put("0" + idRoom + ".-" + roomName, getLong(position++));
        dataMap.put(StringUtils.getDateFormatUSA(dateDelivery), getLong(position++));
        dataMap.put(isActive ? "Yes" : "No", getLong(position++));
        dataMap.put(description, getLong(position++));
        return dataMap;
    }
}
