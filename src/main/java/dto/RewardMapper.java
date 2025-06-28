package dto;

import model.Reward;

public class RewardMapper {
    public static RewardDisplayDTO toDisplayDTO(Reward reward) {
        return RewardDisplayDTO.builder()
                .id(reward.getId())
                .name(reward.getName())
                .description(reward.getDescription())
                .isActive(reward.isActive())
                .build();
    }
}
