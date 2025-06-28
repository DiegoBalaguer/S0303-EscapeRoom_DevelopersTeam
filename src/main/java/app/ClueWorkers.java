
package app;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.ClueDAO;
import enums.OptionsMenuItem;
import lombok.extern.slf4j.Slf4j;
import model.Clue;
import utils.ConsoleUtils;
import view.ClueView;
import view.RoomView;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ClueWorkers {

    private static ClueWorkers clueWorkersInstance;
    private final ClueView clueView;
    private final RoomView roomView;
    private final ClueDAO clueDAO;

    private ClueWorkers() {
        this.clueView = new ClueView();
        try {
            this.roomView = new RoomView();
            this.clueDAO = DAOFactory.getDAOFactory().getClueDAO();
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    public static ClueWorkers getInstance() {
        if (clueWorkersInstance == null) {
            synchronized (ClueWorkers.class) {
                if (clueWorkersInstance == null) {
                    clueWorkersInstance = new ClueWorkers();
                }
            }
        }
        log.debug("Created ClueWorkers Singleton");
        return clueWorkersInstance;
    }

    public void mainMenu() {
        do {
            clueView.displayClueMenu("=== CLUE MANAGEMENT MENU ===");
            clueView.displayMessage("Choose an option: ");
            int answer = ConsoleUtils.readRequiredInt("");
            OptionsMenuItem selectedOption = OptionsMenuItem.getOptionByNumber(answer);

            if (selectedOption != null) {
                try {
                    switch (selectedOption) {
                        case EXIT -> {
                            clueView.displayMessage("Returning to Main Menu...");
                            return;
                        }
                        case ADD -> createClue();
                        case SHOW -> listCluesByRoom();
                        case REMOVE -> deleteClueById();
                        case UPDATE -> updateClue();
                        //case LIST -> listCluesByRoom();
                        default -> clueView.displayErrorMessage("Unknown option selected.");
                    }
                } catch (DAOException e) {
                    clueView.displayErrorMessage("Database operation failed: " + e.getMessage());
                } catch (Exception e) {
                    clueView.displayErrorMessage("An unexpected error occurred: " + e.getMessage());
                }
            } else {
                clueView.displayErrorMessage("Invalid option. Please choose a valid number from the menu.");
            }
        } while (true);
    }

    private void createClue() {
        try {
            clueView.displayMessage("\n=== CREATE CLUE ===");
            Clue newClue = clueView.getClueDetails(); // Collect clue details from the user.
            if (newClue == null || newClue.getIdRoom() == 0) {
                clueView.displayErrorMessage("Invalid input. Operation canceled.");
                return;
            }
            Clue createdClue = clueDAO.create(newClue); // Save clue to database.
            clueView.displayMessage("Clue successfully created:\n" + createdClue);
        } catch (Exception e) {
            clueView.displayErrorMessage("Error creating the clue: " + e.getMessage());
        }
    }
/*
    private void getClueById() {
        try {
            clueView.displayMessage("\n=== GET CLUE BY ID ===");
            Optional<Integer> id = clueView.getClueId();
            if (id.isEmpty()) {
                return;
            }

            Optional<Clue> optionalClue = clueDAO.findById(id.get());
            if (optionalClue.isPresent()) {
                clueView.displayClue(optionalClue.get());
            } else {
                clueView.displayMessage("No clue found with the provided ID.");
            }
        } catch (DAOException e) {
            clueView.displayErrorMessage("Error querying the clue: " + e.getMessage());
        }
    }*/

    private void deleteClueById() {
        try {
            clueView.displayMessage("\n=== DELETE CLUE ===");
            Optional<Integer> id = clueView.getClueId();
            if (id.isEmpty()) {
                return; // Cancel if invalid input
            }

            if (clueDAO.isExistsById(id.get())) {
                clueDAO.deleteById(id.get());
                clueView.displayMessage("Clue successfully deleted with ID: " + id.get());
            } else {
                clueView.displayMessage("No clue found with the provided ID.");
            }
        } catch (DAOException e) {
            clueView.displayErrorMessage("Error deleting the clue: " + e.getMessage());
        }
    }

    private void updateClue() {
        try {
            clueView.displayMessage("\n=== UPDATE CLUE ===");
            Optional<Integer> id = clueView.getClueId();
            if (id.isEmpty()) {
                clueView.displayMessage("No ID provided. Canceling update operation.");
                return;
            }

            Optional<Clue> optionalClue = clueDAO.findById(id.get());
            if (optionalClue.isEmpty()) {
                clueView.displayMessage("No clue found with the provided ID.");
                return;
            }

            Clue existingClue = optionalClue.get();
            clueView.displayMessage("\n=== Current Clue Data ===");
            clueView.displayClue(existingClue);

            Clue updatedClue = clueView.editClue(existingClue); // Allow user to modify details.
            if (updatedClue == null) {
                return; // Cancel if user decides not to proceed.
            }

            clueDAO.update(updatedClue);
            clueView.displayMessage("Clue successfully updated:\n" + updatedClue);

        } catch (DAOException e) {
            clueView.displayErrorMessage("Error updating the clue: " + e.getMessage());
        } catch (Exception e) {
            clueView.displayErrorMessage("An unexpected error occurred: " + e.getMessage());
            log.error("Unexpected error: ", e);
        }
    }

    private void listCluesByRoom() {
        try {
            clueView.displayMessage("\n=== LIST CLUES BY ROOM ===");
            Optional<Integer> idRoom = roomView.getRoomId();
            if (idRoom.isEmpty()) {
                return; // Cancel if invalid input
            }

            List<Clue> clues = clueDAO.findCluesByRoomId(idRoom.get());
            if (clues.isEmpty()) {
                clueView.displayMessage("No clues found for the provided Room ID.");
            } else {
                clueView.displayClues(clues);
            }
        } catch (DAOException e) {
            clueView.displayErrorMessage("Error retrieving clues: " + e.getMessage());
        }
    }
}
