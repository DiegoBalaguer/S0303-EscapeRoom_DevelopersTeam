package mvc.controller;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.DecorationDAO;
import mvc.dto.DecorationDisplayDTO;
import mvc.enumsMenu.OptionsMenuCLFUSDE;
import mvc.model.Decoration;
import mvc.view.BaseView;
import mvc.view.DecorationView;

import java.util.List;
import java.util.Optional;

public class DecorationController {

    private static DecorationController decorationControllerInstance;
    private final DecorationDAO DECORATION_DAO;
    private BaseView baseView;
    private DecorationView decorationView;
    private static final String NAME_OBJECT = "Decoration";

    private DecorationController() {
        baseView = BaseView.getInstance();
        baseView.displayDebugMessage("Creation Class: " + this.getClass().getName());
        try {
            this.DECORATION_DAO = DAOFactory.getDAOFactory().getDecorationDAO();
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException(e);
        }
        decorationView = new DecorationView();
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
            baseView.displayMessageln(OptionsMenuCLFUSDE.viewMenu(NAME_OBJECT.toUpperCase() + " MANAGEMENT"));
            int answer = baseView.getReadRequiredInt("Choose an option: ");
            OptionsMenuCLFUSDE idMenu = OptionsMenuCLFUSDE.getOptionByNumber(answer);
            try {
                switch (idMenu) {
                    case EXIT -> {
                        baseView.displayMessage2ln("Returning to Main Menu...");
                        return;
                    }
                    case CREATE -> createDecoration();
                    case LIST_ALL -> listAllDecorations();
                    case FIND_BY_ID -> getDecorationById();
                    case UPDATE -> updateDecoration();
                    case SOFT_DELETE -> softDeleteDecorationById();
                    case DELETE -> deleteDecorationById();

                    default -> baseView.displayErrorMessage("Error: The value in menu is wrong: " + idMenu);
                }
            } catch (IllegalArgumentException | NullPointerException e) {
                baseView.displayErrorMessage("Error: The value in menu is wrong." + e.getMessage());
            } catch (DAOException e) {
                baseView.displayErrorMessage("Error: Database operation failed: " + e.getMessage());
            } catch (Exception e) {
                baseView.displayErrorMessage("Error: An unexpected error occurred: " + e.getMessage());
            }
        } while (true);
    }

    private void createDecoration() {
        baseView.displayMessageln("#### CREATE " + NAME_OBJECT + "  #################");
        try {
            baseView.displayMessage2ln("List of Rooms:");
            int roomId = RoomController.getInstance().getRoomIdWithList();

            Decoration newDecoration = decorationView.getDecorationDetailsCreate(roomId);
            Decoration savedDecoration = DECORATION_DAO.create(newDecoration);
            baseView.displayMessage2ln(NAME_OBJECT + " created successfully: " + savedDecoration.getName() + " (ID: " + savedDecoration.getId() + ")");
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error creating " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void getDecorationById() {
        baseView.displayMessage2ln("####  GET " + NAME_OBJECT.toUpperCase() + " BY ID  #################");
        try {
            Optional<Decoration> optionalDecoration = DECORATION_DAO.findById(getDecorationIdWithList());
            decorationView.displayRecordDecoration(optionalDecoration.get());
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error showing " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void listAllDecorations() {
        baseView.displayMessage2ln("####  LIST ALL " + NAME_OBJECT.toUpperCase() + "S  #################");
        try {
            listAllDecorationDetail();
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error listing all " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void listDecorationsByRoom() {
        baseView.displayMessage2ln("####  LIST " + NAME_OBJECT.toUpperCase() + "S BY ROOM  #################");
        try {
            baseView.displayMessage2ln("List of Rooms:");
            int roomId = RoomController.getInstance().getRoomIdWithList();

            List<DecorationDisplayDTO> decorations = DECORATION_DAO.findDecorationsByRoomId(roomId);
            if (decorations.isEmpty()) {
                baseView.displayMessageln("No " + NAME_OBJECT + " found for the provided Room ID.");
            } else {
                decorationView.displayDecorationListDto(decorations);
            }
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error retrieving " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void updateDecoration() {
        baseView.displayMessage2ln("####  UPDATE  " + NAME_OBJECT.toUpperCase() + "  #################");
        try {
            Optional<Decoration> existingDecorationOpt = DECORATION_DAO.findById(getDecorationIdWithList());

            baseView.displayMessage2ln("Current " + NAME_OBJECT + " Details:");
            decorationView.displayRecordDecoration(existingDecorationOpt.get());

            baseView.displayMessageln("Enter new details:");
            baseView.displayMessageln("Enter new value or [INTRO] for no changes.");
            baseView.displayMessageln("Enter new Room:");
            int roomId = RoomController.getInstance().getRoomIdWithList();
            existingDecorationOpt.get().setIdRoom(roomId);
            Decoration updatedDecoration = decorationView.getUpdateDecorationDetails(existingDecorationOpt.get());

            Decoration savedDecoration = DECORATION_DAO.update(updatedDecoration);
            baseView.displayMessage2ln(NAME_OBJECT + " updated successfully: " + savedDecoration.getName() + " (ID: " + savedDecoration.getId() + ")");
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error updating " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void deleteDecorationById() {
        baseView.displayMessage2ln("####  DELETE " + NAME_OBJECT.toUpperCase() + "  #################");
        try {
            DECORATION_DAO.deleteById(getDecorationIdWithList());
            baseView.displayMessage2ln(NAME_OBJECT + " deleted successfully (if existed).");
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error deleting the " + NAME_OBJECT + ": " + e.getMessage());
        }
    }
    private void softDeleteDecorationById() throws DAOException {
        baseView.displayMessage2ln("#### SOFT DELETE  " + NAME_OBJECT.toUpperCase() + "  #################");
        try {
            Optional<Decoration> existDecorationOpt = DECORATION_DAO.findById(getDecorationIdWithList());
            existDecorationOpt.get().setActive(false);

            DECORATION_DAO.update(existDecorationOpt.get());
            baseView.displayMessage2ln(NAME_OBJECT + " soft deleted successfully: " + existDecorationOpt.get().getName() + " (ID: " + existDecorationOpt.get().getId() + ")");
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error soft deleting " + NAME_OBJECT +": " + e.getMessage());
        }
    }

    private int getDecorationIdWithList() {
        listAllDecorationDetail();
        Optional<Integer> searchID = baseView.getReadValueInt("Enter " + NAME_OBJECT + " ID: ");
        if (searchID.isEmpty() || DECORATION_DAO.findById(searchID.get()).isEmpty()) {
            String message = NAME_OBJECT + " with ID required or not found.";
            baseView.displayErrorMessage(message);
            throw new IllegalArgumentException(message);
        }
        return searchID.get();
    }
    private void listAllDecorationDetailDto() throws DAOException {
        List<DecorationDisplayDTO> decorations = DECORATION_DAO.findAllCompleteInfo();
        decorationView.displayDecorationListDto(decorations);
    }


    private void listAllDecorationDetail() {
            List<Decoration> decorations = DECORATION_DAO.findAll();
            decorationView.displayDecorationList(decorations);

    }
}