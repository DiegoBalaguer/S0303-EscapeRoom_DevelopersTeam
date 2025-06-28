package app;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.ClueDAO;
import dao.interfaces.DecorationDAO;
import dao.interfaces.RoomDAO;
import enums.OptionsMenuCrud;
import model.Room;
import utils.ConsoleUtils;
import view.RoomView;

import java.math.BigDecimal;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RoomWorkers {

    private static RoomWorkers roomWorkersInstance;
    private final RoomView roomView;
    private final RoomDAO roomDAO;
    private final ClueDAO clueDAO;
    private final DecorationDAO decorationDAO;

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
        if (roomWorkersInstance == null) {
            synchronized (RoomWorkers.class) {
                if (roomWorkersInstance == null) {
                    roomWorkersInstance = new RoomWorkers();
                }
            }
        }
        log.debug("Created RoomWorkers Singleton");
        return roomWorkersInstance;
    }

    public void mainMenu() {
        do {
            roomView.displayRoomMenu("=== ROOM MANAGEMENT MENU ===");
            roomView.displayMessage("Choose an option: ");
            int answer = ConsoleUtils.readRequiredInt("");
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
                } catch (Exception e) {
                    roomView.displayErrorMessage("An unexpected error occurred: " + e.getMessage());
                }
            } else {
                roomView.displayErrorMessage("Invalid option. Please choose a valid number from the menu.");
            }
        } while (true);
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
            roomView.displayMessage("\n=== GET ROOM BY ID ===");
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
            Optional<Integer> id = roomView.getRoomId(); // Solicitamos el ID de la Room
            if (id.isEmpty()) {
                roomView.displayMessage("No ID provided. Canceling update operation.");
                return; // Cancelar si no hay ID proporcionado
            }

            Optional<Room> optionalRoom = roomDAO.findById(id.get());
            if (optionalRoom.isEmpty()) {
                roomView.displayMessage("No room found with the provided ID.");
                return;
            }

            Room existingRoom = optionalRoom.get();
            roomView.displayMessage("\n=== Current Room Data ===");
            roomView.displayRoom(existingRoom); // Mostramos la información actual de la Room

            // Solicitamos los nuevos datos permitiendo que algunos campos sean dejados vacíos
            Room updatedRoom = roomView.getRoomDetailsWithDefaults(existingRoom);
            if (updatedRoom == null) {
                return; // Cancelar si el usuario decide salir
            }

            // Actualizar la room en la base de datos
            roomDAO.update(updatedRoom);
            roomView.displayMessage("Room successfully updated:\n" + updatedRoom);

        } catch (DAOException e) {
            roomView.displayErrorMessage("Error updating the room: " + e.getMessage());
        } catch (Exception e) {
            roomView.displayErrorMessage("An unexpected error occurred: " + e.getMessage());
            log.error("Unexpected error: ", e);
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