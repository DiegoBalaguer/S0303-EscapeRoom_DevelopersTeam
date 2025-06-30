package mvc.controller;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.ClueDAO;
import dao.interfaces.DecorationDAO;
import dao.interfaces.RoomDAO;
import mvc.enumsMenu.OptionsMenuCrud;
import mvc.model.Room;
import mvc.view.BaseView;
import mvc.view.RoomView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RoomController {

    private static RoomController roomControllerInstance;
    private final RoomView roomView;
    private final RoomDAO roomDAO;
    private final ClueDAO clueDAO;
    private final DecorationDAO decorationDAO;
    private final BaseView baseView;
     

    private RoomController() {
        baseView = new BaseView();
        this.roomView = new RoomView();
        try {
            this.roomDAO = DAOFactory.getDAOFactory().getRoomDAO();
            this.clueDAO = DAOFactory.getDAOFactory().getClueDAO();
            this.decorationDAO = DAOFactory.getDAOFactory().getDecorationDAO();
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    public static RoomController getInstance() {
        if (roomControllerInstance == null) {
            synchronized (RoomController.class) {
                if (roomControllerInstance == null) {
                    roomControllerInstance = new RoomController();
                }
            }
        }
        log.debug("Created RoomWorkers Singleton");
        return roomControllerInstance;
    }

    public void mainMenu() {
        do {
            baseView.displayMessageln(OptionsMenuCrud.viewMenu("=== ROOM MANAGEMENT MENU ==="));
            int answer = baseView.getReadRequiredInt("Choose an option: ");
            OptionsMenuCrud selectedOption = OptionsMenuCrud.getOptionByNumber(answer);

            if (selectedOption != null) {
                try {
                    switch (selectedOption) {
                        case EXIT -> {
                            baseView.displayMessageln("Returning to Main Menu...");
                            return;
                        }
                        case ADD -> createRoom();
                        case SHOW -> getRoomById();
                        case REMOVE -> deleteRoomById();
                        case UPDATE -> updateRoom();
                        case CALCULATE -> calculateTotalValue();
                        default -> baseView.displayErrorMessage("Unknown option selected.");
                    }
                } catch (DAOException e) {
                    baseView.displayErrorMessage("Database operation failed: " + e.getMessage());
                } catch (Exception e) {
                    baseView.displayErrorMessage("An unexpected error occurred: " + e.getMessage());
                }
            } else {
                baseView.displayErrorMessage("Invalid option. Please choose a valid number from the menu.");
            }
        } while (true);
    }

    private void createRoom() {
        try {
            baseView.displayMessageln("\n=== CREATE ROOM ===");
            Room newRoom = roomView.getRoomDetails(false);
            if (newRoom == null) {
                return; // Cancel if invalid input
            }
            Room createdRoom = roomDAO.create(newRoom);
            baseView.displayMessageln("Room successfully created:\n" + createdRoom);
        } catch (Exception e) {
            baseView.displayErrorMessage("Error creating the room: " + e.getMessage());
        }
    }

    private void getRoomById() {
        try {
            baseView.displayMessageln("\n=== GET ROOM BY ID ===");
            Optional<Integer> id = roomView.getRoomId();
            if (id.isEmpty()) {
                return;
            }

            Optional<Room> optionalRoom = findRoomById(id.get());
            if (optionalRoom.isPresent()) {
                roomView.displayRoom(optionalRoom.get());
            } else {
                baseView.displayMessageln("No room found with the provided ID.");
            }
        } catch (DAOException e) {
            baseView.displayErrorMessage("Error querying the room: " + e.getMessage());
        }
    }

    private void deleteRoomById() {
        try {
            baseView.displayMessageln("\n=== DELETE ROOM ===");
            Optional<Integer> id = roomView.getRoomId();
            if (id.isEmpty()) {
                return; // Cancel if invalid input
            }

            if (roomDAO.isExistsById(id.get())) {
                roomDAO.deleteById(id.get());
                baseView.displayMessageln("Room successfully deleted with ID: " + id.get());
            } else {
                baseView.displayMessageln("No room found with the provided ID.");
            }
        } catch (DAOException e) {
            baseView.displayErrorMessage("Error deleting the room: " + e.getMessage());
        }
    }

    private void updateRoom() {
        try {
            baseView.displayMessageln("\n=== UPDATE ROOM ===");
            Optional<Integer> id = roomView.getRoomId(); // Solicitamos el ID de la Room
            if (id.isEmpty()) {
                baseView.displayMessageln("No ID provided. Canceling update operation.");
                return; // Cancelar si no hay ID proporcionado
            }

            Optional<Room> optionalRoom = roomDAO.findById(id.get());
            if (optionalRoom.isEmpty()) {
                baseView.displayMessageln("No room found with the provided ID.");
                return;
            }

            Room existingRoom = optionalRoom.get();
            baseView.displayMessageln("\n=== Current Room Data ===");
            roomView.displayRoom(existingRoom); // Mostramos la información actual de la Room

            // Solicitamos los nuevos datos permitiendo que algunos campos sean dejados vacíos
            Room updatedRoom = roomView.getRoomDetailsWithDefaults(existingRoom);
            if (updatedRoom == null) {
                return; // Cancelar si el usuario decide salir
            }

            // Actualizar la room en la base de datos
            roomDAO.update(updatedRoom);
            baseView.displayMessageln("Room successfully updated:\n" + updatedRoom);

        } catch (DAOException e) {
            baseView.displayErrorMessage("Error updating the room: " + e.getMessage());
        } catch (Exception e) {
            baseView.displayErrorMessage("An unexpected error occurred: " + e.getMessage());
            log.error("Unexpected error: ", e);
        }
    }

    private void calculateTotalValue() {
        try {
            baseView.displayMessageln("\n=== CALCULATE TOTAL ROOM VALUE ===");
            Optional<Integer> id = roomView.getRoomId();
            if (id.isEmpty()) {
                return; // Cancel if invalid input
            }

            Optional<Room> optionalRoom = roomDAO.findById(id.get());
            if (optionalRoom.isEmpty()) {
                baseView.displayMessageln("No room found with the provided ID.");
                return;
            }

            Room room = optionalRoom.get();
            BigDecimal roomPrice = room.getPrice();

            // Retrieve prices
            BigDecimal cluePrice = clueDAO.findPriceByRoomId(room.getId());
            BigDecimal decorationPrice = decorationDAO.findPriceByRoomId(room.getId());
            BigDecimal totalValue = roomPrice.add(cluePrice).add(decorationPrice);

            baseView.displayMessageln("\n====== TOTAL VALUES ======\n" +
                    "Room: $" + roomPrice + "\n" +
                    "Clue: $" + cluePrice + "\n" +
                    "Decoration: $" + decorationPrice + "\n" +
                    "Total Value: $" + totalValue);
        } catch (Exception e) {
            baseView.displayErrorMessage("Error calculating total value: " + e.getMessage());
        }
    }

    private OptionsMenuCrud getOptionByOrdinal(int ordinal) {
        for (OptionsMenuCrud option : OptionsMenuCrud.values()) {
            if (option.ordinal() == ordinal) {
                return option;
            }
        }
        return null;
    }

    public Optional<Room> findRoomById(int roomId) {
        return roomDAO.findById(roomId);
    }

    // Queries for other Classes

    public int getRoomIdWithList() throws DAOException, IllegalArgumentException {
            listAllRoomsDetail();
            Optional<Integer> roomIdOpt = roomView.getRoomId();
            Optional<Room> roomSearch = roomDAO.findById(roomIdOpt.get());
            if (roomSearch.isEmpty()) {
                String message = "Room ID required or not found.";
                baseView.displayErrorMessage(message);
                throw new IllegalArgumentException(message);
            }
            return roomIdOpt.get();
    }

    private void listAllRoomsDetail() throws DAOException {
        List<Room> rooms = roomDAO.findAll();
        roomView.displayListRooms(rooms);
    }


}