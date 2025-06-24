package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@ToString(exclude = "password")
@AllArgsConstructor
public class Player {
    private int id;
    private String name;
    private String email;
    private String password;
    private boolean isSubscribed;
    private List<CertificateWin> certificatesWin;
    private List<RewardWin> rewardsWin;
    private boolean isActive;

}
