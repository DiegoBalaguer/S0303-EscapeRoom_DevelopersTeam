package dto;

import model.Player;

public class PlayerMapper {
    public static PlayerDisplayDTO toDisplayDTO(Player player) {
        return PlayerDisplayDTO.builder()
                .id(player.getId())
                .name(player.getName())
                .email(player.getEmail())
                .password(player.getPassword())
                .registrationDate(player.getRegistrationDate())
                .isSubscribed(player.isSubscribed())
                .isActive(player.isActive())
                .build();
    }
}