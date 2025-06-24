package model;

import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@Getter
@Setter
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
