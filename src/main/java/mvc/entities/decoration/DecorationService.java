package mvc.entities.decoration;

import dao.exceptions.DAOException;
import dao.exceptions.NotFoundException;
import mvc.entities.Inputs;
import mvc.entities.MessageMinMax;
import mvc.entities.room.RoomService;
import utils.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class DecorationService {

    private final DecorationDAO DECORATION_DAO;
    private static final String NAME_OBJECT = "Decoration";

    private final RoomService ROOM_SERVICE;

    public DecorationService(DecorationDAO decorationDAO, RoomService roomService) {
        this.DECORATION_DAO = decorationDAO;
        this.ROOM_SERVICE = roomService;
    }

    protected Decoration createDecoration(Inputs inputsValue) throws NotFoundException {

        Optional<String> name = inputsValue.getFieldAs("name", String.class);
        Optional<Integer> roomId = inputsValue.getFieldAs("roomId", Integer.class);
        Optional<BigDecimal> price = inputsValue.getFieldAs("price", BigDecimal.class);
        Optional<String> description = inputsValue.getFieldAs("description", String.class);

        if (name.isEmpty()) {
            throw new IllegalArgumentException("The " + NAME_OBJECT + " name cannot be empty.");
        }
        if (roomId.isEmpty() || ROOM_SERVICE.getRoomById(roomId.get()).isEmpty()) {
            throw new NotFoundException("Room ID with ID " + roomId.get() + " not found.");
        }
        if (price.isEmpty()) {
            throw new NotFoundException("Price not found.");
        }

        Decoration decoration = Decoration.builder()
                .name(name.get())
                .idRoom(roomId.get())
                .price(price.get())
                .description(description.get())
                .isActive(true)
                .build();
        return DECORATION_DAO.create(decoration);
    }

    protected Optional<Decoration> getDecorationById(int id) throws DAOException {
        return DECORATION_DAO.findById(id);
    }

    protected MessageMinMax getAllDecorationsCompleteInfo() throws DAOException {
        List<DecorationDisplayDTO> decorations = DECORATION_DAO.findAllCompleteInfo();
        return new MessageMinMax(displayListDecorationsDto(decorations), decorations.size() > 0 ? 1 : 0, decorations.size());
    }

    protected List<DecorationDisplayDTO> getDecorationsByRoomId(int roomId) throws DAOException {
        return DECORATION_DAO.findDecorationsByRoomId(roomId);
    }

    protected Decoration updateDecoration(Decoration decoration) throws IllegalArgumentException, NotFoundException, DAOException {
        if (decoration == null || decoration.getId() <= 0) {
            throw new IllegalArgumentException("The " + NAME_OBJECT + " or its ID is not valid for the update.");
        }
        if (DECORATION_DAO.findById(decoration.getId()).isEmpty()) {
            throw new NotFoundException(NAME_OBJECT + " with ID " + decoration.getId() + " not found for update.");
        }
        return DECORATION_DAO.update(decoration);
    }

    protected void deleteDecoration(int id) throws NotFoundException, DAOException {
        if (DECORATION_DAO.findById(id).isEmpty()) {
            throw new NotFoundException(NAME_OBJECT + " with ID " + id + " not found for deletion.");
        }
        DECORATION_DAO.deleteById(id);
    }

    protected Decoration softDeleteDecoration(int id) throws NotFoundException, DAOException {
        Optional<Decoration> existingDecorationOpt = DECORATION_DAO.findById(id);
        if (existingDecorationOpt.isEmpty()) {
            throw new NotFoundException(NAME_OBJECT + " with ID " + id + " not found for soft deletion.");
        }
        Decoration decorationToSoftDelete = existingDecorationOpt.get();
        decorationToSoftDelete.setActive(false);
        return DECORATION_DAO.update(decorationToSoftDelete);
    }

    public BigDecimal getDecorationPriceByRoomId(int roomId) throws DAOException {
        return DECORATION_DAO.findPriceByRoomId(roomId);
    }

    protected MessageMinMax getAllRooms() {
        return ROOM_SERVICE.getAllRooms();
    }

    private String displayListDecorationsDto(List<DecorationDisplayDTO> decorationDisplayDTOS) {
        StringBuilder message = new StringBuilder();
        if (decorationDisplayDTOS.isEmpty()) {
            return "No " + NAME_OBJECT + " found.";
        }
        message.append(
                StringUtils.makeLineToList(decorationDisplayDTOS.getFirst().toListHead()));

        decorationDisplayDTOS.forEach(rewardWins -> message.append(
                StringUtils.makeLineToList(rewardWins.toList())).append(System.lineSeparator()));
        message.append("-------------------").append(System.lineSeparator());
        return message.toString();
    }
}
