package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RewardWin {
    private int id;
    private int idReward;
    private int idPlayer;
    private String description;
    private LocalDateTime dateDelivery;
    private boolean isActive;
}