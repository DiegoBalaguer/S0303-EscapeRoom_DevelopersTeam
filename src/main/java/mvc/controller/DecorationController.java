package mvc.controller;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.DecorationDAO;
import mvc.enumsMenu.OptionsMenuItem;
import lombok.extern.slf4j.Slf4j;
import mvc.model.Decoration;
import utils.ConsoleUtils;
import mvc.view.DecorationView;
import mvc.view.RoomView;

import java.util.List;
import java.util.Optional;

@Slf4j
public class DecorationController {

    private static DecorationController decorationControllerInstance;
    private final DecorationView decorationView;
    private final RoomView roomView;
    private final DecorationDAO decorationDAO;

    private DecorationController() {
        this.decorationView = new DecorationView();
        try {
            this.roomView = new RoomView();
            this.decorationDAO = DAOFactory.getDAOFactory().getDecorationDAO();
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    public static DecorationController getInstance() {
        if (decorationControllerInstance == null) {
            synchronized (DecorationController.class) {
                if (decorationControllerInstance == null) {
                    decorationControllerInstance = new DecorationController();
                }
            }
        }
        log.debug("Created DecorationWorkers Singleton");
        return decorationControllerInstance;
    }

    public void mainMenu() {
        do {
            decorationView.displayItemMenu("=== DECORATION MANAGEMENT MENU ===");
            decorationView.displayMessage("Choose an option: ");
            int answer = ConsoleUtils.readRequiredInt("");
            OptionsMenuItem selectedOption = OptionsMenuItem.getOptionByNumber(answer);

            if (selectedOption != null) {
                try {
                    switch (selectedOption) {
                        case EXIT -> {
                            decorationView.displayMessage("Returning to Main Menu...");
                            return;
                        }
                        case ADD -> createDecoration();
                        case SHOW -> listDecorationsByRoom();
                        case REMOVE -> deleteDecorationById();
                        case UPDATE -> updateDecoration();
                        default -> decorationView.displayErrorMessage("Unknown option selected.");
                    }
                } catch (DAOException e) {
                    decorationView.displayErrorMessage("Database operation failed: " + e.getMessage());
                } catch (Exception e) {
                    decorationView.displayErrorMessage("An unexpected error occurred: " + e.getMessage());
                }
            } else {
                decorationView.displayErrorMessage("Invalid option. Please choose a valid number from the menu.");
            }
        } while (true);
    }

    private void createDecoration() {
        try {
            decorationView.displayMessage("\n=== CREATE DECORATION ===");
            Decoration newDecoration = decorationView.getDecorationDetails(); // Collect decoration details from the user.
            if (newDecoration == null || newDecoration.getIdRoom() == 0) {
                decorationView.displayErrorMessage("Invalid input. Operation canceled.");
                return;
            }
            Decoration createdDecoration = decorationDAO.create(newDecoration); // Save decoration to database.
            decorationView.displayMessage("Decoration successfully created:\n" + createdDecoration);
        } catch (Exception e) {
            decorationView.displayErrorMessage("Error creating the decoration: " + e.getMessage());
        }
    }

    private void deleteDecorationById() {
        try {
            decorationView.displayMessage("\n=== DELETE DECORATION ===");
            Optional<Integer> id = decorationView.getDecorationId();
            if (id.isEmpty()) {
                return; // Cancel if invalid input
            }

            if (decorationDAO.isExistsById(id.get())) {
                decorationDAO.deleteById(id.get());
                decorationView.displayMessage("Decoration successfully deleted with ID: " + id.get());
            } else {
                decorationView.displayMessage("No decoration found with the provided ID.");
            }
        } catch (DAOException e) {
            decorationView.displayErrorMessage("Error deleting the decoration: " + e.getMessage());
        }
    }

    private void updateDecoration() {
        try {
            decorationView.displayMessage("\n=== UPDATE DECORATION ===");
            Optional<Integer> id = decorationView.getDecorationId();
            if (id.isEmpty()) {
                decorationView.displayMessage("No ID provided. Canceling update operation.");
                return;
            }

            Optional<Decoration> optionalDecoration = decorationDAO.findById(id.get());
            if (optionalDecoration.isEmpty()) {
                decorationView.displayMessage("No decoration found with the provided ID.");
                return;
            }

            Decoration existingDecoration = optionalDecoration.get();
            decorationView.displayMessage("\n=== Current Decoration Data ===");
            decorationView.displayDecoration(existingDecoration);

            Decoration updatedDecoration = decorationView.editDecoration(existingDecoration); // Allow user to modify details.
            if (updatedDecoration == null) {
                return; // Cancel if user decides not to proceed.
            }

            decorationDAO.update(updatedDecoration);
            decorationView.displayMessage("Decoration successfully updated:\n" + updatedDecoration);

        } catch (DAOException e) {
            decorationView.displayErrorMessage("Error updating the decoration: " + e.getMessage());
        } catch (Exception e) {
            decorationView.displayErrorMessage("An unexpected error occurred: " + e.getMessage());
            log.error("Unexpected error: ", e);
        }
    }

    private void listDecorationsByRoom() {
        try {
            decorationView.displayMessage("\n=== LIST DECORATIONS BY ROOM ===");
            Optional<Integer> idRoom = roomView.getRoomId();
            if (idRoom.isEmpty()) {
                return; // Cancel if invalid input
            }

            List<Decoration> decorations = decorationDAO.findDecorationsByRoomId(idRoom.get());
            if (decorations.isEmpty()) {
                decorationView.displayMessage("No decorations found for the provided Room ID.");
            } else {
                decorationView.displayDecorations(decorations);
            }
        } catch (DAOException e) {
            decorationView.displayErrorMessage("Error retrieving decorations: " + e.getMessage());
        }
    }
}