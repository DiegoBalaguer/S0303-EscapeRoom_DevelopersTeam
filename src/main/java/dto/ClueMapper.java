package dto;

import model.Clue;

public class ClueMapper {

    public static ClueDisplayDTO toDisplayDTO(Clue clue) {
        return ClueDisplayDTO.builder()
                .id(clue.getId())
                .name(clue.getName())
                .price(clue.getPrice())
                .idRoom(clue.getIdRoom())
                .isActive(clue.isActive())
                .description(clue.getDescription())
                .build();
    }
}