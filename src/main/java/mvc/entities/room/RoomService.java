package mvc.entities.room;


import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.exceptions.NotFoundException;
import lombok.Setter;
import mvc.entities.MessageMinMax;
import mvc.entities.clue.ClueService;
import mvc.entities.decoration.DecorationService;
import mvc.entities.escapeRoom.EscapeRoom;
import utils.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class RoomService {

    private final RoomDAO ROOM_DAO;
    @Setter
    private ClueService clueService;
    @Setter
    private DecorationService decorationService;
    private static final String NAME_OBJECT = "Room";

    public RoomService(RoomDAO roomDAO) {
        this.ROOM_DAO = roomDAO;
    }

    protected Room createRoom(Room newRoom) throws DAOException, DatabaseConnectionException, NotFoundException {
        if (newRoom == null) {
            throw new IllegalArgumentException(NAME_OBJECT + " object cannot be null.");
        }
        Room savedRoom = ROOM_DAO.create(newRoom);
        EscapeRoom.getInstance().notifyObservers("A new " + NAME_OBJECT + " has been created: " + savedRoom.getName());
        return savedRoom;
    }

    public Optional<Room> getRoomById(int roomId) throws DAOException {
        if (roomId <= 0) {
            throw new IllegalArgumentException(NAME_OBJECT + " ID must be positive.");
        }
        return ROOM_DAO.findById(roomId);
    }

    public Room softDeleteRoom(int roomId) throws DAOException {
        Optional<Room> existRoomOpt = ROOM_DAO.findById(roomId);
        if (existRoomOpt.isEmpty()) {
            throw new IllegalArgumentException(NAME_OBJECT + " with ID " + roomId + " not found for soft deletion.");
        }
        Room roomToUpdate = existRoomOpt.get();
        roomToUpdate.setActive(false);
        return ROOM_DAO.update(roomToUpdate);
    }

    public Room updateRoom(Room updatedRoom) throws DAOException {
        if (updatedRoom == null || updatedRoom.getId() <= 0) {
            throw new IllegalArgumentException("Updated " + NAME_OBJECT + " and a valid ID are required.");
        }
        return ROOM_DAO.update(updatedRoom);
    }

    public BigDecimal calculateTotalRoomValue(int roomId) throws DAOException {
        Optional<Room> optionalRoom = ROOM_DAO.findById(roomId);
        if (optionalRoom.isEmpty()) {
            throw new IllegalArgumentException("No " + NAME_OBJECT + " found with the provided ID: " + roomId);
        }

        Room room = optionalRoom.get();
        BigDecimal roomPrice = room.getPrice();

        BigDecimal cluePrice = clueService.getCluePriceByRoomId(room.getId());
        BigDecimal decorationPrice = decorationService.getDecorationPriceByRoomId(room.getId());

        return roomPrice.add(cluePrice).add(decorationPrice);
    }

    public MessageMinMax getAllRooms() throws DAOException {
        List<Room> rooms = ROOM_DAO.findAll();
        return new MessageMinMax(displayListRooms(rooms), rooms.size() > 0 ? 1: 0, rooms.size());
    }

    public String displayListRooms(List<Room> rooms) {
        StringBuilder message = new StringBuilder();
        if (rooms.isEmpty()) {
            return "No " + NAME_OBJECT + " found.";
        }
        message.append(
                StringUtils.makeLineToList(RoomMapper.toDisplayDTO(rooms.getFirst()).toListHead())).append(System.lineSeparator());

        rooms.forEach(room -> message.append(
                StringUtils.makeLineToList(RoomMapper.toDisplayDTO(room).toList())).append(System.lineSeparator()));
        message.append("-------------------").append(System.lineSeparator());
        return message.toString();
    }


}
