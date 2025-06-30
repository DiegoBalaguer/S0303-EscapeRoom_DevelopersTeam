package mvc.view;

import mvc.dto.PlayerMapper;
import mvc.model.Player;
import utils.ConsoleUtils;
import utils.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class PlayerView {

    private final BaseView baseView = new BaseView();
    private static final String NAME_OBJECT = "Player";

    public Player getPlayerDetailsCreate() {
        return Player.builder()
                .name(getInputName())
                .email(getInputEmail())
                .password(getInputPassword())
                .registrationDate(LocalDateTime.now())
                .isSubscribed(getInputIsSubscribed())
                .isActive(true)
                .build();
    }

    private String getInputName() {
        return ConsoleUtils.readRequiredString("Enter name: ");
    }

    private String getInputPassword() {
        return ConsoleUtils.readRequiredString("Enter password: ");
    }

    private boolean getInputIsSubscribed() {
        return ConsoleUtils.readRequiredBoolean("Enter is subscribed ('Y' or 'N'): ");
    }

    private String getInputEmail() {
        String email = "";
        do {
            email = ConsoleUtils.readRequiredString("Enter email: ");
            if (!StringUtils.isValidEmail(email)) {
                baseView.displayErrorMessage("Invalid email format. Retype email.");
            }
        } while (!StringUtils.isValidEmail(email));
        return email;
    }

    public Player getUpdatePlayerDetails(Player player) {
        player.setName(getUpdateName(player.getName()));
        player.setEmail(getUpdateEmail(player.getEmail()));
        player.setPassword(getUpdatePassword(player.getName()));
        player.setSubscribed(getUpdateIsSubscribed(player.isSubscribed()));
        player.setActive(getUpdateIsActive(player.isActive()));
        return player;
    }

    private String getUpdateName(String oldValue) {
        return ConsoleUtils.readStringWithDefault("Enter name: ", Optional.of(oldValue)).get();
    }

    private String getUpdatePassword(String oldValue) {
        return ConsoleUtils.readStringWithDefault("Enter password: ", Optional.of(oldValue)).get();
    }

    private Boolean getUpdateIsSubscribed(boolean oldValue) {
        return ConsoleUtils.readBooleanWithDefault("Enter subscribed ('Y' or 'N'): ", Optional.of(oldValue)).get();
    }

    private Boolean getUpdateIsActive(boolean oldValue) {
        return ConsoleUtils.readBooleanWithDefault("Enter is active ('Y' or 'N'): ", Optional.of(oldValue)).get();
    }

    private String getUpdateEmail(String oldValue) {
        String email = "";
        do {
            email = ConsoleUtils.readStringWithDefault("Enter email: ", Optional.of(oldValue)).get();
            if (!StringUtils.isValidEmail(email)) {
                baseView.displayErrorMessage("Invalid email format. Retype email.");
            }
        } while (!StringUtils.isValidEmail(email));
        return email;
    }

    public void displayRecordPlayer(Player player) {
        String message = "";
        if (player != null) {
            message += "--- Player Details ---" + baseView.LINE;
            message += "ID: " + player.getId() + baseView.LINE;
            message += "Name: " + player.getName() + baseView.LINE;
            message += "Email: " + player.getEmail() + baseView.LINE;
            message += "Password: " + player.getPassword() + baseView.LINE;
            message += "Registration Date: " + player.getRegistrationDate() + baseView.LINE;
            message += "Subscribed: " + player.isSubscribed() + baseView.LINE;
            message += "Active: " + player.isActive() + baseView.LINE;
            message += "----------------------" + baseView.LINE;
        } else {
            message = "Player not found.";
        }
        baseView.displayMessageln(message);
    }

    public void displayListPlayers(List<Player> players) {
        if (players.isEmpty()) {
            System.out.println("No " + NAME_OBJECT + "s found.");
            return;
        }
        baseView.displayMessageln(
                StringUtils.makeLineToList(PlayerMapper.toDisplayDTO(players.getFirst()).toListHead()));

        players.forEach(player -> baseView.displayMessageln(
                StringUtils.makeLineToList(PlayerMapper.toDisplayDTO(player).toList())));
        baseView.displayMessage2ln("-------------------");
    }
}