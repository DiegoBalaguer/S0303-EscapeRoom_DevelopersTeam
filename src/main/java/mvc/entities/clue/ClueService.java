package mvc.entities.clue;

import dao.exceptions.DAOException;
import dao.exceptions.NotFoundException;
import mvc.entities.Inputs;
import mvc.entities.MessageMinMax;
import mvc.entities.room.RoomService;
import utils.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ClueService {

    private final ClueDAO CLUE_DAO;
    private final RoomService ROOM_SERVICE;
    private static final String NAME_OBJECT = "Clue";

    public ClueService(ClueDAO clueDAO, RoomService roomService) {
        this.CLUE_DAO = clueDAO;
        this.ROOM_SERVICE = roomService;
    }

    protected Clue createClue(Inputs inputsValue) throws NotFoundException {

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

        Clue clue = Clue.builder()
                .name(name.get())
                .idRoom(roomId.get())
                .price(price.get())
                .description(description.get())
                .isActive(true)
                .build();
        return CLUE_DAO.create(clue);
    }

    protected Optional<Clue> getClueById(int id) throws DAOException {
        return CLUE_DAO.findById(id);
    }

    protected MessageMinMax getAllCluesCompleteInfo() throws DAOException {
        List<ClueDisplayDTO> clues = CLUE_DAO.findAllCompleteInfo();
        return new MessageMinMax(displayListCluesDto(clues), clues.size() > 0 ? 1 : 0, clues.size());
    }

    protected List<ClueDisplayDTO> getCluesByRoomId(int roomId) throws DAOException {
        return CLUE_DAO.findCluesByRoomId(roomId);
    }

    protected Clue updateClue(Clue clue) throws IllegalArgumentException, NotFoundException, DAOException {
        if (clue == null || clue.getId() <= 0) {
            throw new IllegalArgumentException("The " + NAME_OBJECT + " or its ID is not valid for the update.");
        }
        if (CLUE_DAO.findById(clue.getId()).isEmpty()) {
            throw new NotFoundException(NAME_OBJECT + " with ID " + clue.getId() + " not found for update.");
        }
        return CLUE_DAO.update(clue);
    }

    protected void deleteClue(int id) throws NotFoundException, DAOException {
        if (CLUE_DAO.findById(id).isEmpty()) {
            throw new NotFoundException(NAME_OBJECT + " with ID " + id + " not found for deletion.");
        }
        CLUE_DAO.deleteById(id);
    }

    protected Clue softDeleteClue(int id) throws NotFoundException, DAOException {
        Optional<Clue> existingClueOpt = CLUE_DAO.findById(id);
        if (existingClueOpt.isEmpty()) {
            throw new NotFoundException(NAME_OBJECT + " with ID " + id + " not found for soft deletion.");
        }
        Clue clueToSoftDelete = existingClueOpt.get();
        clueToSoftDelete.setActive(false);
        return CLUE_DAO.update(clueToSoftDelete);
    }

    public BigDecimal getCluePriceByRoomId(int roomId) throws DAOException {
        return CLUE_DAO.findPriceByRoomId(roomId);
    }

    protected MessageMinMax getAllRooms() {
        return ROOM_SERVICE.getAllRooms();
    }

    private String displayListCluesDto(List<ClueDisplayDTO> clueDisplayDTOS) {
        StringBuilder message = new StringBuilder();
        if (clueDisplayDTOS.isEmpty()) {
            return "No " + NAME_OBJECT + " found.";
        }
        message.append(
                StringUtils.makeLineToList(clueDisplayDTOS.getFirst().toListHead()));

        clueDisplayDTOS.forEach(rewardWins -> message.append(
                StringUtils.makeLineToList(rewardWins.toList())).append(System.lineSeparator()));
        message.append("-------------------").append(System.lineSeparator());
        return message.toString();
    }
}
