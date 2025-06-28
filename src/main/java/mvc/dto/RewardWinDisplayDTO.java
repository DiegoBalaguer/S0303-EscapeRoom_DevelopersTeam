package mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utils.PairTextLength;
import utils.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardWinDisplayDTO {
    private Integer id;
    private Integer idReward;
    private Integer idPlayer;
    private String description;
    private LocalDateTime dateDelivery;
    private Boolean isActive;
    private String rewardName;

    private int getLong(int position) {
        return List.of(6, 30, 25, 10, 100).get(position);
    }

    public List<PairTextLength> toListHead() {
        List<PairTextLength> listValues = new ArrayList<>();
        int position = 0;;
        listValues.add(new PairTextLength("ID", getLong(position++)));
        listValues.add(new PairTextLength("REWARD", getLong(position++)));
        listValues.add(new PairTextLength("DATE DELIVERY", getLong(position++)));
        listValues.add(new PairTextLength("ACTIVE", getLong(position++)));
        listValues.add(new PairTextLength("DESCRIPTION", getLong(position++)));
        return listValues;
    }

    public List<PairTextLength> toList() {
        List<PairTextLength> listValues = new ArrayList<>();
        int position = 0;
        listValues.add(new PairTextLength(String.valueOf(id), getLong(position++)));
        listValues.add(new PairTextLength("0" + idReward + ".-" + rewardName, getLong(position++)));
        listValues.add(new PairTextLength(StringUtils.getDateFormatUSA(dateDelivery), getLong(position++)));
        listValues.add(new PairTextLength(isActive ? "Yes" : "No", getLong(position++)));
        listValues.add(new PairTextLength(description, getLong(position++)));
        return listValues;
    }
}