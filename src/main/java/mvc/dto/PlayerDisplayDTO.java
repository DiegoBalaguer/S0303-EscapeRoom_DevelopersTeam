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
public class PlayerDisplayDTO {
    private int id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime registrationDate;
    private boolean isSubscribed;
    private boolean isActive;

    private int getLong(int position) {
        return List.of(8, 25, 35, 25, 24, 10, 10).get(position);
    }

    public List<PairTextLength> toListHead() {
        List<PairTextLength> listValues = new ArrayList<>();
        int position = 0;
        listValues.add(new PairTextLength("ID", getLong(position++)));
        listValues.add(new PairTextLength("NAME", getLong(position++)));
        listValues.add(new PairTextLength("EMAIL", getLong(position++)));
        listValues.add(new PairTextLength("PASSWORD", getLong(position++)));
        listValues.add(new PairTextLength("REG.DATE", getLong(position++)));
        listValues.add(new PairTextLength("SUBSCRIBE", getLong(position++)));
        listValues.add(new PairTextLength("ACTIVE", getLong(position++)));
        return listValues;
    }

    public List<PairTextLength> toList() {
        List<PairTextLength> listValues = new ArrayList<>();
        int position = 0;
        listValues.add(new PairTextLength(String.valueOf(id), getLong(position++)));
        listValues.add(new PairTextLength(name, getLong(position++)));
        listValues.add(new PairTextLength(email, getLong(position++)));
        listValues.add(new PairTextLength(password, getLong(position++)));
        listValues.add(new PairTextLength(StringUtils.getDateFormatUSA(registrationDate), getLong(position++)));
        listValues.add(new PairTextLength(isSubscribed ? "Yes" : "No", getLong(position++)));
        listValues.add(new PairTextLength(isActive ? "Yes" : "No", getLong(position++)));
        return listValues;
    }
}
