package mvc.model;

import interfaces.Observer;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@Builder
@ToString(exclude = "password")
@AllArgsConstructor
@NoArgsConstructor
public class Player implements Observer {
    private int id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime registrationDate;
    private boolean isSubscribed;
    private List<CertificateWin> certificatesWin;
    private List<RewardWin> rewardsWin;
    private boolean isActive;

    public Player(int id, String name, String email, boolean isSubscribed) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.isSubscribed = isSubscribed;
    }

    public void update(String message) {
        System.out.println("Player updated with message: " + message);
    }

}
