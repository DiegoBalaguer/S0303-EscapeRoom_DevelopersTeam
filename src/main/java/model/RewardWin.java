package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class RewardWin {
    private int id;
    private int idReward;
    private int idPlayer;
    private String description;
    private LocalDate dateDelivery;
    private boolean isActive;
}