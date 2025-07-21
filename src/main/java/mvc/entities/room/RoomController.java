package mvc.entities.room;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.exceptions.NotFoundException;
import mvc.entities.ServiceManager;
import mvc.view.BaseView;

import java.math.BigDecimal;
import java.util.Optional;

public class RoomController {

    private static RoomController instance;
    private final RoomService ROOM_SERVICE;
    private final RoomView ROOM_VIEW;
    private final BaseView BASE_VIEW;
    private static final String NAME_OBJECT = "Room";

    private RoomController(RoomService roomService) throws DatabaseConnectionException {
        BASE_VIEW = BaseView.getInstance();
        BASE_VIEW.displayDebugMessage("Creation Class: " + this.getClass().getName());
        this.ROOM_SERVICE = roomService;
        ROOM_VIEW = RoomView.getInstance();
    }

    public static RoomController getInstance() throws DatabaseConnectionException {
        if (instance == null) {
            synchronized (RoomController.class) {
                if (instance == null) {
                    RoomService service = ServiceManager.getInstance().getRoomService();
                    instance = new RoomController(service);
                }
            }
        }
        return instance;
    }

    public void mainMenu() throws DatabaseConnectionException {
        do {
            BASE_VIEW.displayMessageln(OptionsMenuRoom.viewMenu("=== ROOM MANAGEMENT MENU ==="));
            int answer = BASE_VIEW.getReadRequiredInt("Choose an option: ");
            OptionsMenuRoom selectedOption = OptionsMenuRoom.getOptionByNumber(answer);
            try {
                switch (selectedOption) {
                    case EXIT -> {
                        BASE_VIEW.displayMessageln("Returning to Main Menu...");
                        return;
                    }
                    case ADD -> createRoom();
                    case SHOW -> getRoomById();
                    case REMOVE -> softDeleteRoom();
                    case UPDATE -> updateRoom();
                    case CALCULATE -> calculateTotalValue();
                    default -> BASE_VIEW.displayErrorMessage("Unknown option selected.");
                }
            } catch (IllegalArgumentException e) {
                BASE_VIEW.displayErrorMessage("Error: Invalid data entered. " + e.getMessage());
            } catch (DAOException e) {
                BASE_VIEW.displayErrorMessage("Error: Database operation failed. " + e.getMessage());
            } catch (NullPointerException e) {
                BASE_VIEW.displayErrorMessage("An unexpected error occurred (Null Pointer): " + e.getMessage());
            } catch (NotFoundException e) {
                BASE_VIEW.displayErrorMessage("Entity not found: " + e.getMessage());
            }
        } while (true);
    }

    private void createRoom() throws DatabaseConnectionException, NotFoundException {
        BASE_VIEW.displayMessageln("\n=== CREATE ROOM ===");
        Room newRoom = ROOM_VIEW.getRoomDetails(false);
        if (newRoom == null) {
            BASE_VIEW.displayErrorMessage("Room creation canceled due to invalid input.");
            return;
        }
        Room savedRoom = ROOM_SERVICE.createRoom(newRoom);
        BASE_VIEW.displayMessageln("Room successfully created:\n" + savedRoom);
    }

    private void getRoomById() throws NotFoundException {
        BASE_VIEW.displayMessageln("\n=== GET ROOM BY ID ===");
        Optional<Integer> roomId = getRoomIdWithList();
        Optional<Room> optionalRoom = ROOM_SERVICE.getRoomById(roomId.get());
        if (optionalRoom.isPresent()) {
            ROOM_VIEW.displayRoom(optionalRoom.get());
        } else {
            BASE_VIEW.displayErrorMessage("No room found with the provided ID.");
        }
    }

    private void softDeleteRoom() throws NotFoundException {
        BASE_VIEW.displayMessage2ln("#### SOFT DELETE  " + NAME_OBJECT.toUpperCase() + "  #################");
        Optional<Integer> roomId = getRoomIdWithList();
        Room softDeletedRoom = ROOM_SERVICE.softDeleteRoom(roomId.get());
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " soft deleted successfully: " + softDeletedRoom.getName() + " (ID: " + softDeletedRoom.getId() + ")");
    }

    private void updateRoom() {
        BASE_VIEW.displayMessageln("\n=== UPDATE ROOM ===");
        Optional<Integer> idOpt = ROOM_VIEW.getRoomId();
        if (idOpt.isEmpty()) {
            BASE_VIEW.displayMessageln("No ID provided. Canceling update operation.");
            return;
        }
        int id = idOpt.get();

        Optional<Room> optionalRoom = ROOM_SERVICE.getRoomById(id);
        if (optionalRoom.isEmpty()) {
            BASE_VIEW.displayMessageln("No room found with the provided ID.");
            return;
        }

        Room existingRoom = optionalRoom.get();
        BASE_VIEW.displayMessageln("\n=== Current Room Data ===");
        ROOM_VIEW.displayRoom(existingRoom);

        Room updatedRoomDetails = ROOM_VIEW.getRoomDetailsWithDefaults(existingRoom);
        if (updatedRoomDetails == null) {
            BASE_VIEW.displayErrorMessage("Update operation canceled or invalid details provided.");
            return;
        }
        updatedRoomDetails.setId(existingRoom.getId());

        Room finalUpdatedRoom = ROOM_SERVICE.updateRoom(updatedRoomDetails);
        BASE_VIEW.displayMessageln("Room successfully updated:\n" + finalUpdatedRoom);
    }

    private void calculateTotalValue() {
        BASE_VIEW.displayMessageln("\n=== CALCULATE TOTAL ROOM VALUE ===");
        Optional<Integer> idOpt = ROOM_VIEW.getRoomId();
        if (idOpt.isEmpty()) {
            return;
        }
        int id = idOpt.get();

        BigDecimal totalValue = ROOM_SERVICE.calculateTotalRoomValue(id);

        BASE_VIEW.displayMessageln("\n====== TOTAL VALUES ======\n" +
                "Total Value for Room ID " + id + ": $" + totalValue);
    }

    private Optional<Integer> getRoomIdWithList() throws NotFoundException {
        Optional<Integer> roomIdOpt = BASE_VIEW.getReadValueIntMinMax("List of Rooms", "Input Room ID: ", ROOM_SERVICE.getAllRooms());
        if (roomIdOpt.isEmpty()) {
            String message = "No " + NAME_OBJECT + " found.";
            BASE_VIEW.displayMessageln(message);
            throw new NotFoundException(message);
        }
        return roomIdOpt;
    }
}
