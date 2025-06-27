package app;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.ClueDAO;
import dao.interfaces.DecorationDAO;
import dao.interfaces.RoomDAO;
import enums.OptionsMenuCrud;
import enums.OptionsMenuPlayer;
import model.Room;
import utils.ConsoleUtils;
import view.RoomView;

import java.math.BigDecimal;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RoomWorkers {

    private static RoomWorkers appWorkersInstance;
    private final RoomView roomView; // View for user interaction
    private final RoomDAO roomDAO;   // DAO for room database interactions
    private final ClueDAO clueDAO;  // DAO for clues
    private final DecorationDAO decorationDAO; // DAO for decorations

    private RoomWorkers() {
        this.roomView = new RoomView();
        try {
            this.roomDAO = DAOFactory.getDAOFactory().getRoomDAO();
            this.clueDAO = DAOFactory.getDAOFactory().getClueDAO();
            this.decorationDAO = DAOFactory.getDAOFactory().getDecorationDAO();
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    public static RoomWorkers getInstance() {
        if (appWorkersInstance == null) {
            synchronized (RoomWorkers.class) {
                if (appWorkersInstance == null) {
                    appWorkersInstance = new RoomWorkers();
                }
            }
        }
        return appWorkersInstance;
    }

    public void mainMenu() {
        boolean running = true;
        while (running) {
            roomView.displayRoomMenu("=== ROOM MANAGEMENT MENU ===");
            int answer = ConsoleUtils.readRequiredInt("Choose an option: ");
            OptionsMenuCrud selectedOption = OptionsMenuCrud.getOptionByNumber(answer);

            if (selectedOption != null) {
                try {
                    switch (selectedOption) {
                        case EXIT -> {
                            roomView.displayMessage("Returning to Main Menu...");
                            return;
                        }
                        case ADD -> createRoom();
                        case SHOW -> getRoomById();
                        case REMOVE -> deleteRoomById();
                        case UPDATE -> updateRoom();
                        case CALCULATE -> calculateTotalValue();

                        default -> roomView.displayErrorMessage("Unknown option selected.");
                    }
                } catch (DAOException e) {
                    roomView.displayErrorMessage("Database operation failed: " + e.getMessage());
                    log.error("DAO Error in PlayerWorkers: {}", e.getMessage(), e);
                } catch (Exception e) {
                    roomView.displayErrorMessage("An unexpected error occurred: " + e.getMessage());
                    log.error("Unexpected error in PlayerWorkers: {}", e.getMessage(), e);
                }
            } else {
                roomView.displayErrorMessage("Invalid option. Please choose a valid number from the menu.");
                log.warn("Error: The value {} is wrong in player management menu.", answer);
            }
        }
    }

    private void createRoom() {
        try {
            roomView.displayMessage("\n=== CREATE ROOM ===");
            Room newRoom = roomView.getRoomDetails(false);
            if (newRoom == null) {
                return; // Cancel if invalid input
            }
            Room createdRoom = roomDAO.create(newRoom);
            roomView.displayMessage("Room successfully created:\n" + createdRoom);
        } catch (Exception e) {
            roomView.displayErrorMessage("Error creating the room: " + e.getMessage());
        }
    }

    private void getRoomById() {
        try {
            roomView.displayMessage("\n=== QUERY ROOM BY ID ===");
            Optional<Integer> id = roomView.getRoomId();
            if (id.isEmpty()) {
                return; // Cancel if invalid input
            }

            Optional<Room> optionalRoom = roomDAO.findById(id.get());
            if (optionalRoom.isPresent()) {
                roomView.displayRoom(optionalRoom.get());
            } else {
                roomView.displayMessage("No room found with the provided ID.");
            }
        } catch (DAOException e) {
            roomView.displayErrorMessage("Error querying the room: " + e.getMessage());
        }
    }

    private void deleteRoomById() {
        try {
            roomView.displayMessage("\n=== DELETE ROOM ===");
            Optional<Integer> id = roomView.getRoomId();
            if (id.isEmpty()) {
                return; // Cancel if invalid input
            }

            if (roomDAO.isExistsById(id.get())) {
                roomDAO.deleteById(id.get());
                roomView.displayMessage("Room successfully deleted with ID: " + id.get());
            } else {
                roomView.displayMessage("No room found with the provided ID.");
            }
        } catch (DAOException e) {
            roomView.displayErrorMessage("Error deleting the room: " + e.getMessage());
        }
    }

    private void updateRoom() {
        try {
            roomView.displayMessage("\n=== UPDATE ROOM ===");
            Optional<Integer> id = roomView.getRoomId();
            if (id.isEmpty()) {
                return; // Cancel if invalid input
            }

            Optional<Room> optionalRoom = roomDAO.findById(id.get());
            if (optionalRoom.isEmpty()) {
                roomView.displayMessage("No room found with the provided ID.");
                return;
            }

            Room existingRoom = optionalRoom.get();
            Room updatedRoom = roomView.getRoomDetails(true);
            if (updatedRoom == null) {
                return; // Cancel if invalid input
            }

            // Update values
            existingRoom.setName(updatedRoom.getName());
            existingRoom.setDescription(updatedRoom.getDescription());
            existingRoom.setPrice(updatedRoom.getPrice());
            existingRoom.setDifficulty(updatedRoom.getDifficulty());
            existingRoom.setActive(updatedRoom.isActive());

            roomDAO.update(existingRoom);
            roomView.displayMessage("Room successfully updated:\n" + existingRoom);
        } catch (Exception e) {
            roomView.displayErrorMessage("Error updating the room: " + e.getMessage());
        }
    }

    private void calculateTotalValue() {
        try {
            roomView.displayMessage("\n=== CALCULATE TOTAL ROOM VALUE ===");
            Optional<Integer> id = roomView.getRoomId();
            if (id.isEmpty()) {
                return; // Cancel if invalid input
            }

            Optional<Room> optionalRoom = roomDAO.findById(id.get());
            if (optionalRoom.isEmpty()) {
                roomView.displayMessage("No room found with the provided ID.");
                return;
            }

            Room room = optionalRoom.get();
            BigDecimal roomPrice = room.getPrice();

            // Retrieve prices
            BigDecimal cluePrice = clueDAO.findPriceByRoomId(room.getId());
            BigDecimal decorationPrice = decorationDAO.findPriceByRoomId(room.getId());
            BigDecimal totalValue = roomPrice.add(cluePrice).add(decorationPrice);

            roomView.displayMessage("\n====== TOTAL VALUES ======\n" +
                    "Room: $" + roomPrice + "\n" +
                    "Clue: $" + cluePrice + "\n" +
                    "Decoration: $" + decorationPrice + "\n" +
                    "Total Value: $" + totalValue);
        } catch (Exception e) {
            roomView.displayErrorMessage("Error calculating total value: " + e.getMessage());
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
}