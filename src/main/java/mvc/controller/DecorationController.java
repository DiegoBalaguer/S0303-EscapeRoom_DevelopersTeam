package mvc.controller;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.DecorationDAO;
import mvc.enumsMenu.OptionsMenuItem;
import mvc.model.Decoration;
import mvc.view.BaseView;
import mvc.view.DecorationView;
import mvc.view.RoomView;

import java.util.List;
import java.util.Optional;

public class DecorationController {

    private static DecorationController decorationControllerInstance;
    private BaseView baseView;
    private DecorationView decorationView;
    private RoomView roomView;
    private DecorationDAO decorationDAO;
    private static final String NAME_OBJECT = "Decoration";

    private DecorationController() {
        baseView = new BaseView();
        baseView.displayDebugMessage("Creation Class: " + this.getClass().getName());
        decorationView = new DecorationView();
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
        return decorationControllerInstance;
    }

    public void mainMenu() {
        do {
            baseView.displayMessageln(OptionsMenuItem.viewMenu(NAME_OBJECT.toUpperCase() + " MANAGEMENT"));
            int answer = baseView.getReadRequiredInt("Choose an option: ");
            OptionsMenuItem selectedOption = OptionsMenuItem.getOptionByNumber(answer);

            if (selectedOption != null) {
                try {
                    switch (selectedOption) {
                        case EXIT -> {
                            baseView.displayMessageln("Returning to Main Menu...");
                            return;
                        }
                        case ADD -> createDecoration();
                        case SHOW -> listDecorationsByRoom();
                        case DELETE -> deleteDecorationById();
                        case UPDATE -> updateDecoration();
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

    private void createDecoration() {
        try {
            baseView.displayMessageln("\n=== CREATE DECORATION ===");
            Decoration newDecoration = decorationView.getDecorationDetails(); // Collect decoration details from the user.
            if (newDecoration == null || newDecoration.getIdRoom() == 0) {
                baseView.displayErrorMessage("Invalid input. Operation canceled.");
                return;
            }
            Decoration createdDecoration = decorationDAO.create(newDecoration); // Save decoration to database.
            baseView.displayMessageln("Decoration successfully created:\n" + createdDecoration);
        } catch (Exception e) {
            baseView.displayErrorMessage("Error creating the decoration: " + e.getMessage());
        }
    }

    private void deleteDecorationById() {
        try {
            baseView.displayMessageln("\n=== DELETE DECORATION ===");
            Optional<Integer> id = decorationView.getDecorationId();
            if (id.isEmpty()) {
                return; // Cancel if invalid input
            }

            if (decorationDAO.isExistsById(id.get())) {
                decorationDAO.deleteById(id.get());
                baseView.displayMessageln("Decoration successfully deleted with ID: " + id.get());
            } else {
                baseView.displayMessageln("No decoration found with the provided ID.");
            }
        } catch (DAOException e) {
            baseView.displayErrorMessage("Error deleting the decoration: " + e.getMessage());
        }
    }

    private void updateDecoration() {
        try {
            baseView.displayMessageln("\n=== UPDATE DECORATION ===");
            Optional<Integer> id = decorationView.getDecorationId();
            if (id.isEmpty()) {
                baseView.displayMessageln("No ID provided. Canceling update operation.");
                return;
            }

            Optional<Decoration> optionalDecoration = decorationDAO.findById(id.get());
            if (optionalDecoration.isEmpty()) {
                baseView.displayMessageln("No decoration found with the provided ID.");
                return;
            }

            Decoration existingDecoration = optionalDecoration.get();
            baseView.displayMessageln("\n=== Current Decoration Data ===");
            decorationView.displayDecoration(existingDecoration);

            Decoration updatedDecoration = decorationView.editDecoration(existingDecoration); // Allow user to modify details.
            if (updatedDecoration == null) {
                return; // Cancel if user decides not to proceed.
            }

            decorationDAO.update(updatedDecoration);
            baseView.displayMessageln("Decoration successfully updated:\n" + updatedDecoration);

        } catch (DAOException e) {
            baseView.displayErrorMessage("Error updating the decoration: " + e.getMessage());
        } catch (Exception e) {
            baseView.displayErrorMessage("An unexpected error occurred: " + e.getMessage());
        }
    }

    private void listDecorationsByRoom() {
        try {
            baseView.displayMessageln("\n=== LIST DECORATIONS BY ROOM ===");
            Optional<Integer> idRoom = roomView.getRoomId();
            if (idRoom.isEmpty()) {
                return; // Cancel if invalid input
            }

            List<Decoration> decorations = decorationDAO.findDecorationsByRoomId(idRoom.get());
            if (decorations.isEmpty()) {
                baseView.displayMessageln("No decorations found for the provided Room ID.");
            } else {
                decorationView.displayDecorations(decorations);
            }
        } catch (DAOException e) {
            baseView.displayErrorMessage("Error retrieving decorations: " + e.getMessage());
        }
    }
}