package mvc.controller;
import dao.impl.h2.PlayerDAOH2Impl;
import dao.interfaces.*;
import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import mvc.enumsMenu.OptionsMenuCrud;
import dao.impl.h2.ConnectionDAOH2Impl;
import dao.impl.h2.NotificationDAOH2Impl;
import mvc.model.Notification;
import mvc.model.Player;
import mvc.model.Room;
import mvc.view.BaseView;
import mvc.view.RoomView;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RoomController {

    private static RoomController roomControllerInstance;
    private final RoomView roomView;
    private final RoomDAO ROOM_DAO;
    private final ClueDAO clueDAO;
    private final DecorationDAO decorationDAO;
    private final BaseView baseView;
    private static final String NAME_OBJECT = "Room";


    private RoomController() {
        baseView = new BaseView();
        this.roomView = new RoomView();
        try {
            this.ROOM_DAO = DAOFactory.getDAOFactory().getRoomDAO();
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
                        case REMOVE ->  softDeleteRoom();
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
        baseView.displayMessageln("\n=== CREATE ROOM ===");
        try {
            Room newRoom = roomView.getRoomDetails(false);

            Room savedRoom = ROOM_DAO.create(newRoom);
            baseView.displayMessageln("Room successfully created:\n" + savedRoom);

            PlayerDAO playerDAO = new PlayerDAOH2Impl(ConnectionDAOH2Impl.getInstance());
            NotificationDAO notificationDAO = new NotificationDAOH2Impl(ConnectionDAOH2Impl.getInstance());

            List<Player> subscribedPlayers = playerDAO.findSubscribedPlayers();
            if (subscribedPlayers.isEmpty()) {
                baseView.displayMessageln("No subscribed players found. Notification not sent.");
                return;
            }

            for (Player player : subscribedPlayers) {
                Notification notification = Notification.builder()
                        .idPlayer(player.getId())
                        .message("A new room has been created: " + savedRoom.getName())
                        .dateTimeSent(LocalDateTime.now())
                        .isActive(true)
                        .build();

                notificationDAO.saveNotification(notification);

                player.update("Notification: A new room has been created: " + savedRoom.getName());
            }

            baseView.displayMessageln("Notifications sent to all subscribed players.");

        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error creating " + NAME_OBJECT + ": " + e.getMessage());
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException(e);
        }
    }


    private void getRoomById() {
        try {
            baseView.displayMessageln("\n=== GET ROOM BY ID ===");

            Optional<Room> optionalRoom = ROOM_DAO.findById(getRoomIdWithList());
            roomView.displayRoom(optionalRoom.get());

        } catch (DAOException e) {
            baseView.displayErrorMessage("Error querying the room: " + e.getMessage());
        }
    }
    private void softDeleteRoom() throws DAOException {
        baseView.displayMessage2ln("#### SOFT DELETE  " + NAME_OBJECT.toUpperCase() + "  #################");
        try {
            Optional<Room> existRoomOpt = ROOM_DAO.findById(getRoomIdWithList());
            existRoomOpt.get().setActive(false);

            ROOM_DAO.update(existRoomOpt.get());
            baseView.displayMessage2ln(NAME_OBJECT + " soft deleted successfully: " + existRoomOpt.get().getName() + " (ID: " + existRoomOpt.get().getId() + ")");
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error soft deleting " + NAME_OBJECT +": " + e.getMessage());
        }
    }

    private void updateRoom() {
        try {
            baseView.displayMessageln("\n=== UPDATE ROOM ===");
            Optional<Integer> id = roomView.getRoomId();
            if (id.isEmpty()) {
                baseView.displayMessageln("No ID provided. Canceling update operation.");
                return;
            }

            Optional<Room> optionalRoom = ROOM_DAO.findById(id.get());
            if (optionalRoom.isEmpty()) {
                baseView.displayMessageln("No room found with the provided ID.");
                return;
            }

            Room existingRoom = optionalRoom.get();
            baseView.displayMessageln("\n=== Current Room Data ===");
            roomView.displayRoom(existingRoom);
            Room updatedRoom = roomView.getRoomDetailsWithDefaults(existingRoom);
            if (updatedRoom == null) {
                return;
            }
            ROOM_DAO.update(updatedRoom);
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

            Optional<Room> optionalRoom = ROOM_DAO.findById(id.get());
            if (optionalRoom.isEmpty()) {
                baseView.displayMessageln("No room found with the provided ID.");
                return;
            }

            Room room = optionalRoom.get();
            BigDecimal roomPrice = room.getPrice();

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
        return ROOM_DAO.findById(roomId);
    }

    public int getRoomIdWithList() throws DAOException, IllegalArgumentException {
            listAllRoomsDetail();
            Optional<Integer> roomIdOpt = roomView.getRoomId();
            Optional<Room> roomSearch = ROOM_DAO.findById(roomIdOpt.get());
            if (roomSearch.isEmpty()) {
                String message = "Room ID required or not found.";
                baseView.displayErrorMessage(message);
                throw new IllegalArgumentException(message);
            }
            return roomIdOpt.get();
    }

    private void listAllRoomsDetail() throws DAOException {
        List<Room> rooms = ROOM_DAO.findAll();
        roomView.displayListRooms(rooms);
    }


}