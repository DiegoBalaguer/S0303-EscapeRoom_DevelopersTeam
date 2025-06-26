package model;

import lombok.*;
import utils.StringUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@Builder
@ToString(exclude = "password")
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private int id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime registrationDate;
    private boolean isSubscribed;
    private List<CertificateWin> certificatesWin;
    private List<RewardWin> rewardsWin;
    private boolean isActive;


    public ArrayList<String> toList() {
        ArrayList<String> dataLine = new ArrayList<>();
        dataLine.add(String.valueOf(id));
        dataLine.add(name);
        dataLine.add(email);
        dataLine.add(password);
        dataLine.add(StringUtils.getDateFormatUSA(registrationDate));
        //dataLine.add(registrationDate.getYear() + "-" + registrationDate.getMonthValue() + "-" + registrationDate.getDayOfMonth());
        dataLine.add(isSubscribed ? "True" : "False");
        dataLine.add(isActive ? "True" : "False");
       return dataLine;
    }
}
