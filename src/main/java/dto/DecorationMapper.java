package dto;

import model.Decoration;

public class DecorationMapper {

    public static DecorationDisplayDTO toDisplayDTO(Decoration decoration) {
        return DecorationDisplayDTO.builder()
                .id(decoration.getId())
                .name(decoration.getName())
                .price(decoration.getPrice())
                .idRoom(decoration.getIdRoom())
                .isActive(decoration.isActive())
                .build();
    }
}