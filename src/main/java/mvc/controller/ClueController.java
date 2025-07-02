package mvc.controller;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.*;

import mvc.dto.ClueDisplayDTO;
import mvc.enumsMenu.OptionsMenuItem;
import mvc.model.Clue;

import mvc.view.BaseView;
import mvc.view.ClueView;

import java.util.List;
import java.util.Optional;

public class ClueController {

    private static ClueController clueControllerInstance;
    private final ClueDAO CLUE_DAO;
    private BaseView baseView;
    private ClueView clueView;
    private static final String NAME_OBJECT = "Clue";

    private ClueController() {
        baseView = new BaseView();
        baseView.displayDebugMessage("Creation Class: " + this.getClass().getName());
        try {
            this.CLUE_DAO = DAOFactory.getDAOFactory().getClueDAO();
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException(e);
        }
        clueView = new ClueView();
    }

    public static ClueController getInstance() {
        if (clueControllerInstance == null) {
            synchronized (ClueController.class) {
                if (clueControllerInstance == null) {
                    clueControllerInstance = new ClueController();
                }
            }
        }
        return clueControllerInstance;
    }

    public void mainMenu() {
        do {
            baseView.displayMessageln(OptionsMenuItem.viewMenu(NAME_OBJECT.toUpperCase() + " MANAGEMENT"));
            int answer = baseView.getReadRequiredInt("Choose an option: ");
            OptionsMenuItem idMenu = OptionsMenuItem.getOptionByNumber(answer);
            try {
                switch (idMenu) {
                    case EXIT -> {
                        baseView.displayMessage2ln("Returning to Main Menu...");
                        return;
                    }
                    case ADD -> createClue();
                    case SHOW -> getClueById();
                    case LIST_ALL -> listAllClues();
                    case LIST_BY_ROOM -> listCluesByRoom();
                    case UPDATE -> updateClue();
                    case DELETE -> deleteClueById();

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


    private void createClue() {
        baseView.displayMessageln("#### CREATE " + NAME_OBJECT + "  #################");
        try {
            baseView.displayMessage2ln("List of Rooms:");
            int roomId = RoomController.getInstance().getRoomIdWithList();

            Clue newClue = clueView.getClueDetailsCreate(roomId);
            Clue savedClue = CLUE_DAO.create(newClue);
            baseView.displayMessage2ln(NAME_OBJECT + " created successfully: " + savedClue.getName() + " (ID: " + savedClue.getId() + ")");
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error creating " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void getClueById() {
        baseView.displayMessage2ln("####  GET " + NAME_OBJECT.toUpperCase() + " BY ID  #################");
        try {
            Optional<Clue> optionalClue = CLUE_DAO.findById(getClueIdWithList());

            clueView.displayRecordClue(optionalClue.get());
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error show " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void listAllClues() throws DAOException {
        baseView.displayMessage2ln("####  LIST ALL " + NAME_OBJECT.toUpperCase() + "S  #################");
        try {
            listAllClueDetail();
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error list all " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void listCluesByRoom() {
        baseView.displayMessage2ln("####  LIST " + NAME_OBJECT.toUpperCase() + "S BY ROOM  #################");
        try {
            baseView.displayMessage2ln("List of Rooms:");
            int roomId = RoomController.getInstance().getRoomIdWithList();

            List<ClueDisplayDTO> clues = CLUE_DAO.findCluesByRoomId(roomId);
            if (clues.isEmpty()) {
                baseView.displayMessageln("No " + NAME_OBJECT + " found for the provided Room ID.");
            } else {
                clueView.displayClueListDto(clues);
            }
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error retrieving " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void updateClue() {
        baseView.displayMessage2ln("####  UPDATE  " + NAME_OBJECT.toUpperCase() + "  #################");
        try {
            Optional<Clue> existClueOpt = CLUE_DAO.findById(getClueIdWithList());

            baseView.displayMessage2ln("Current " + NAME_OBJECT + " Details:");
            clueView.displayRecordClue(existClueOpt.get());

            baseView.displayMessage2ln("Enter new details:");
            baseView.displayMessageln("Enter new value or [INTRO] for not changes.");
            baseView.displayMessageln("Enter new Room:");
            int roomId = RoomController.getInstance().getRoomIdWithList();
            existClueOpt.get().setIdRoom(roomId);
            Clue updatedClue = clueView.getUpdateClueDetails(existClueOpt.get());

            Clue savedClue = CLUE_DAO.update(updatedClue);
            baseView.displayMessage2ln(NAME_OBJECT + " updated successfully: " + savedClue.getName() + " (ID: " + savedClue.getId() + ")");
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error updating " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void deleteClueById() {
        baseView.displayMessage2ln("####  DELETE " + NAME_OBJECT.toUpperCase() + "  #################");
        try {
            CLUE_DAO.deleteById(getClueIdWithList());
            baseView.displayMessage2ln(NAME_OBJECT + " deleted successfully (if existed).");
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error deleting the " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void softDeleteClueById() throws DAOException {
        baseView.displayMessage2ln("#### SOFT DELETE  " + NAME_OBJECT.toUpperCase() + "  #################");
        try {
            Optional<Clue> existClueOpt = CLUE_DAO.findById(getClueIdWithList());
            existClueOpt.get().setActive(false);

            CLUE_DAO.update(existClueOpt.get());
            baseView.displayMessage2ln(NAME_OBJECT + " soft deleted successfully: " + existClueOpt.get().getName() + " (ID: " + existClueOpt.get().getId() + ")");
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error soft deleting " + NAME_OBJECT +": " + e.getMessage());
        }
    }

    public int getClueIdWithList() {
        listAllClueDetailDto();
        Optional<Integer> searchID = baseView.getReadValueInt("Enter " + NAME_OBJECT + " ID: ");
        if (searchID.isEmpty() || CLUE_DAO.findById(searchID.get()).isEmpty()) {
            String message = NAME_OBJECT + " with ID required or not found.";
            baseView.displayErrorMessage(message);
            throw new IllegalArgumentException(message);
        }
        return searchID.get();
    }

    private void listAllClueDetailDto() throws DAOException {
        List<ClueDisplayDTO> clues = CLUE_DAO.findAllCluesCompleteInfo();
        clueView.displayClueListDto(clues);
    }

    private void listAllClueDetail() throws DAOException {
        List<Clue> clues = CLUE_DAO.findAll();
        clueView.displayClueList(clues);
    }
}