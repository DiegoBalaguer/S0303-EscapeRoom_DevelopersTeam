package mvc.controller;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.*;

import mvc.dto.ClueDisplayDTO;
import mvc.enumsMenu.OptionsMenuItem;
import lombok.extern.slf4j.Slf4j;
import mvc.model.Clue;

import mvc.model.Room;
import mvc.view.BaseView;
import mvc.view.ClueView;
import mvc.view.RoomView;

import java.util.List;
import java.util.Optional;

@Slf4j
public class ClueController {

    private static ClueController clueControllerInstance;
    private final ClueView clueView;
    private final RoomView roomView;
    private final BaseView baseView;
    private final ClueDAO clueDAO;
    private final RoomDAO roomDAO;

    private ClueController() {
        baseView = new BaseView();
        this.clueView = new ClueView();
        try {
            this.roomView = new RoomView();
            this.roomDAO = DAOFactory.getDAOFactory().getRoomDAO();
            this.clueDAO = DAOFactory.getDAOFactory().getClueDAO();
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    public static ClueController getInstance() {
        if (clueControllerInstance == null) {
            synchronized (ClueController.class) {
                if (clueControllerInstance == null) {
                    clueControllerInstance = new ClueController();
                }
            }
        }
        log.debug("Created ClueWorkers Singleton");
        return clueControllerInstance;
    }

    public void mainMenu() {
        do {
            clueView.displayClueMenu("=== CLUE MANAGEMENT MENU ===");
            int answer = baseView.getInputRequiredInt("Choose an option: ");
            OptionsMenuItem selectedOption = OptionsMenuItem.getOptionByNumber(answer);

            if (selectedOption != null) {
                try {
                    switch (selectedOption) {
                        case EXIT -> {
                            baseView.displayMessageln("Returning to Main Menu...");
                            return;
                        }
                        case ADD -> createClue();
                        case SHOW -> getClueById();
                        case LIST_ALL -> listAllClues();
                        case LIST_BY_ROOM -> listCluesByRoom();
                        case UPDATE -> updateClue();
                        case DELETE -> deleteClueById();
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

    private void createClue() {
        baseView.displayMessageln("#### CREATE CLUE  #################");
        try {
            int roomId = RoomController.getInstance().getRoomId();

            Clue newClue = clueView.getClueDetailsCreate(roomId);
            if (newClue == null || newClue.getIdRoom() == 0) {
                baseView.displayErrorMessage("Invalid input. Operation canceled.");
                return;
            }
            Clue savedClue = clueDAO.create(newClue);
            baseView.displayMessage2ln("Clue created successfully: " + savedClue.getName() + " (ID: " + savedClue.getId() + ")");
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error creating the clue: " + e.getMessage());
        }
    }

    private void getClueById() {
        try {
            baseView.displayMessageln(baseView.LINE + "=== GET CLUE BY ID ===");

            Optional<Integer> id = clueView.getClueId();
            if (id.isEmpty()) {
                return;
            }

            Optional<Clue> optionalClue = clueDAO.findById(id.get());
            if (optionalClue.isPresent()) {
                clueView.displayClue(optionalClue.get());
            } else {
                baseView.displayMessageln("No clue found with the provided ID.");
            }
        } catch (DAOException e) {
            baseView.displayErrorMessage("Error querying the clue: " + e.getMessage());
        }
    }

    private void listAllClues() throws DAOException {
        baseView.displayMessage2ln("#### LIST ALL CLUES  #################");
        listAllCluesDetail();
    }




    private void deleteClueById() {
        try {
            baseView.displayMessageln("\n=== DELETE CLUE ===");
            Optional<Integer> id = clueView.getClueId();
            if (id.isEmpty()) {
                return; // Cancel if invalid input
            }

            if (clueDAO.isExistsById(id.get())) {
                clueDAO.deleteById(id.get());
                baseView.displayMessageln("Clue successfully deleted with ID: " + id.get());
            } else {
                baseView.displayMessageln("No clue found with the provided ID.");
            }
        } catch (DAOException e) {
            baseView.displayErrorMessage("Error deleting the clue: " + e.getMessage());
        }
    }

    private void updateClue() {
        try {
            baseView.displayMessageln("\n=== UPDATE CLUE ===");
            Optional<Integer> id = clueView.getClueId();
            if (id.isEmpty()) {
                baseView.displayMessageln("No ID provided. Canceling update operation.");
                return;
            }

            Optional<Clue> optionalClue = clueDAO.findById(id.get());
            if (optionalClue.isEmpty()) {
                baseView.displayMessageln("No clue found with the provided ID.");
                return;
            }

            Clue existingClue = optionalClue.get();
            baseView.displayMessageln("\n=== Current Clue Data ===");
            clueView.displayClue(existingClue);

            Clue updatedClue = clueView.editClue(existingClue); // Allow user to modify details.
            if (updatedClue == null) {
                return; // Cancel if user decides not to proceed.
            }

            clueDAO.update(updatedClue);
            baseView.displayMessageln("Clue successfully updated:\n" + updatedClue);

        } catch (DAOException e) {
            baseView.displayErrorMessage("Error updating the clue: " + e.getMessage());
        } catch (Exception e) {
            baseView.displayErrorMessage("An unexpected error occurred: " + e.getMessage());
            log.error("Unexpected error: ", e);
        }
    }



    private void listCluesByRoom() {
        baseView.displayMessage2ln(baseView.LINE + "=== LIST CLUES BY ROOM ===");
        try {
            baseView.displayMessageln("=== list rooms ===");
            int roomId = RoomController.getInstance().getRoomId();

            List<Clue> clues = clueDAO.findCluesByRoomId(roomId);
            if (clues.isEmpty()) {
                baseView.displayMessageln("No clues found for the provided Room ID.");
            } else {
                clueView.displayClues(clues);
            }
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error retrieving clues: " + e.getMessage());
        }
    }


    public int getClueId() throws DAOException, IllegalArgumentException {
        listAllCluesDetail();
        Optional<Integer> roomIdOpt = roomView.getRoomId();
        Optional<Room> roomSearch = roomDAO.findById(roomIdOpt.get());
        if (roomSearch.isEmpty()) {
            String message = "Room ID required or not found.";
            baseView.displayErrorMessage(message);
            throw new IllegalArgumentException(message);
        }
        return roomIdOpt.get();
    }

    private void listAllCluesDetail() throws DAOException {

        List<ClueDisplayDTO> clues = clueDAO.findAllCluesCompleteInfo();
        clueView.displayClueList(clues);
    }
}