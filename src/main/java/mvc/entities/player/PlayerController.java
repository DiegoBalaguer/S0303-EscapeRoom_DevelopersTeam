package mvc.entities.player;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.exceptions.NotFoundException;
import mvc.entities.ServiceManager;
import mvc.entities.player.playerAwars.PlayerAwardsController;
import mvc.entities.player.playerNotify.PlayerNotifyController;

import mvc.view.BaseView;

import java.util.Optional;

public class PlayerController {

    private static PlayerController instance;
    private final PlayerService PLAYER_SERVICE;
    private final PlayerView PLAYER_VIEW;
    private final BaseView BASE_VIEW;
    private static final String NAME_OBJECT = "Player";

    private PlayerController(PlayerService playerService) {
        BASE_VIEW = BaseView.getInstance();
        PLAYER_VIEW = PlayerView.getInstance();
        BASE_VIEW.displayDebugMessage("Creation Class: " + this.getClass().getName());
        this.PLAYER_SERVICE = playerService;
    }

    public static PlayerController getInstance() throws DatabaseConnectionException {
        if (instance == null) {
            synchronized (PlayerController.class) {
                if (instance == null) {
                    PlayerService service = ServiceManager.getInstance().getPlayerService();
                    instance = new PlayerController(service);
                }
            }
        }
        return instance;
    }

    public void mainMenu() throws DatabaseConnectionException {
        do {
            BASE_VIEW.displayMessageln(OptionsMenuPlayer.viewMenu(NAME_OBJECT.toUpperCase() + " MANAGEMENT"));
            int answer = BASE_VIEW.getReadRequiredInt("Choose an option: ");
            OptionsMenuPlayer selectedOption = OptionsMenuPlayer.getOptionByNumber(answer);
            try {
                switch (selectedOption) {
                    case EXIT -> {
                        BASE_VIEW.displayMessage2ln("Returning to Main Menu...");
                        return;
                    }
                    case CREATE -> createPlayer();
                    case LIST_ALL -> listAllPlayers();
                    case READ -> findPlayerById();
                    case UPDATE -> updatePlayer();
                    case DELETE -> deletePlayerById();
                    case SOFT_DELETE -> softDeletePlayer();
                    case AWARDS_MANAGEMENT -> PlayerAwardsController.getInstance().mainMenu();
                    case NOTIFY_MANAGEMENT -> PlayerNotifyController.getInstance().mainMenu();
                    default -> BASE_VIEW.displayErrorMessage("Unknown option selected.");
                }
            } catch (IllegalArgumentException e) {
                BASE_VIEW.displayErrorMessage("Error: Invalid data entered. " + e.getMessage());
            } catch (DAOException e) {
                BASE_VIEW.displayErrorMessage("Error: Database operation failed. " + e.getMessage());
            } catch (NullPointerException e) {
                BASE_VIEW.displayErrorMessage("An unexpected error occurred (Null Pointer): " + e.getMessage());
            } catch (NotFoundException e) {
                BASE_VIEW.displayErrorMessage("Entity not found: " + e.getMessage());
            }
        } while (true);
    }

    private void createPlayer() {
        BASE_VIEW.displayMessage2ln("####  CREATE " + NAME_OBJECT.toUpperCase() + "  #################");
        Player newPlayer = PLAYER_VIEW.getPlayerDetailsCreate();
        Player savedPlayer = PLAYER_SERVICE.createPlayer(newPlayer);
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " created successfully: " + savedPlayer.getName() + " (ID: " + savedPlayer.getId() + ")");
    }

    private void listAllPlayers() {
        BASE_VIEW.displayMessage2ln("####  LIST ALL " + NAME_OBJECT.toUpperCase() + "S  #################");
        BASE_VIEW.displayMessageln(PLAYER_SERVICE.getAllPlayers().getMessage());
    }

    private void findPlayerById() throws DatabaseConnectionException, NotFoundException {
        BASE_VIEW.displayMessage2ln("####  FIND " + NAME_OBJECT.toUpperCase() + " BY ID  #################");
        Optional<Integer> playerIdOpt = getPlayerIdWithList();
        Optional<Player> existPlayerOpt = PLAYER_SERVICE.getPlayerById(playerIdOpt.get());

        if (existPlayerOpt.isPresent()) {
            PLAYER_VIEW.displayRecordPlayer(existPlayerOpt.get());

            BASE_VIEW.displayMessage2ln("List of Rewards Wins");
            BASE_VIEW.displayMessageln(
                    ServiceManager.getInstance().getRewardWinService().getAllRewardsWinForPlayer(existPlayerOpt.get().getId()).getMessage()
            );

            BASE_VIEW.displayMessage2ln("List of Certificate Wins");
            BASE_VIEW.displayMessageln(
                    ServiceManager.getInstance().getCertificateWinService().getAllCertificatesWinForPlayer(existPlayerOpt.get().getId()).getMessage());
        } else {
            String message = NAME_OBJECT + " not found.";
            BASE_VIEW.displayErrorMessage(message);
            throw new NotFoundException(message);
        }
    }

    private void updatePlayer() throws NotFoundException {
        BASE_VIEW.displayMessage2ln("####  UPDATE  " + NAME_OBJECT.toUpperCase() + "  #################");
        Optional<Integer> searchIdOpt = getPlayerIdWithList();
        Optional<Player> existPlayerOpt = PLAYER_SERVICE.getPlayerById(searchIdOpt.get());
        if (existPlayerOpt.isEmpty()) {
            String message = NAME_OBJECT + " not found for update.";
            BASE_VIEW.displayErrorMessage(message);
            throw new IllegalArgumentException(message);
        }
        BASE_VIEW.displayMessage2ln("Current " + NAME_OBJECT + " Details:");
        PLAYER_VIEW.displayRecordPlayer(existPlayerOpt.get());

        BASE_VIEW.displayMessage2ln("Enter new details:");
        Player updatedPlayer = PLAYER_VIEW.getUpdatePlayerDetails(existPlayerOpt.get());

        PLAYER_SERVICE.updatePlayer(updatedPlayer);
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " updated successfully.");
    }

    private void deletePlayerById() throws NotFoundException {
        BASE_VIEW.displayMessage2ln("####  DELETE  " + NAME_OBJECT.toUpperCase() + "  #################");
        Optional<Integer> playerIdOpt = getPlayerIdWithList();
        PLAYER_SERVICE.deletePlayerById(playerIdOpt.get());
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " deleted successfully.");
    }

    private void softDeletePlayer() throws NotFoundException {
        BASE_VIEW.displayMessage2ln("#### SOFT DELETE  " + NAME_OBJECT.toUpperCase() + "  #################");
        Optional<Integer> playerIdOpt = getPlayerIdWithList();
        PLAYER_SERVICE.softDeletePlayer(playerIdOpt.get());
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " soft deleted successfully.");
    }

    private Optional<Integer> getPlayerIdWithList() throws NotFoundException {
        Optional<Integer> playerIdOpt = BASE_VIEW.getReadValueIntMinMax("List of Players", "Input Player ID: ", PLAYER_SERVICE.getAllPlayers());
        if (playerIdOpt.isEmpty()) {
            String message = NAME_OBJECT + " not found.";
            BASE_VIEW.displayErrorMessage(message);
            throw new NotFoundException(message);
        }
        return playerIdOpt;
    }
}
