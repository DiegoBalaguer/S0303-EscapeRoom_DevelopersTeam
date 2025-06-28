package dto;

import model.Room;

public class RoomMapper {
    public static RoomDisplayDTO toDisplayDTO(Room room) {
        return RoomDisplayDTO.builder()
                .id(room.getId())
                .idEscapeRoom(room.getIdEscapeRoom())
                .name(room.getName())
                .theme(room.getTheme())
                .difficulty(room.getDifficulty())
                .price(room.getPrice())
                .isActive(room.isActive())
                .description(room.getDescription())
                .build();
    }
}