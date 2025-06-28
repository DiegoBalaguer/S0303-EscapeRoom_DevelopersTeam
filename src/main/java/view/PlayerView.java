package view;

import dto.CertificateWinDisplayDTO;
import dto.PlayerDisplayDTO;
import dto.PlayerMapper;
import dto.RewardWinDisplayDTO;
import enums.OptionsMenuPlayer;
import loadConfigApp.LoadConfigApp;
import model.Player;
import lombok.extern.slf4j.Slf4j;
import utils.ConsoleUtils;
import utils.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Slf4j
public class PlayerView {

    private final String LINE = System.lineSeparator();

    public void displayPlayerMenu(String title) {
        OptionsMenuPlayer.viewMenu(LoadConfigApp.getAppName());
    }

    public int getInputOptionMenu() {
        return ConsoleUtils.readRequiredInt("");
    }

    public Optional<Integer> getPlayerId() {
        try {
            int id = ConsoleUtils.readRequiredInt("Enter player ID: ");
            return Optional.of(id);
        } catch (NumberFormatException e) {
            displayErrorMessage("Invalid ID. Please enter a number.");
            return Optional.empty();
        }
    }

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
        return ConsoleUtils.readRequiredString("Enter player name: ");
    }

    private String getInputPassword() {
        return ConsoleUtils.readRequiredString("Enter player password: ");
    }

    private boolean getInputIsSubscribed() {
        return ConsoleUtils.readRequiredBoolean("Enter player subscribed ('Y' or 'N'): ");
    }

    private String getInputEmail() {
        String email = "";
        do {
            email = ConsoleUtils.readRequiredString("Enter player email: ");
            if (!StringUtils.isValidEmail(email)) {
                displayErrorMessage("Invalid email format. Retype email.");
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
        return ConsoleUtils.readStringWithDefault("Enter player name: ", Optional.of(oldValue)).get();
    }

    private String getUpdatePassword(String oldValue) {
        return ConsoleUtils.readStringWithDefault("Enter player password: ", Optional.of(oldValue)).get();
    }

    private Boolean getUpdateIsSubscribed(boolean oldValue) {
        return ConsoleUtils.readBooleanWithDefault("Enter player subscribed ('Y' or 'N'): ", Optional.of(oldValue)).get();
    }

    private Boolean getUpdateIsActive(boolean oldValue) {
        return ConsoleUtils.readBooleanWithDefault("Enter player is active ('Y' or 'N'): ", Optional.of(oldValue)).get();
    }

    private String getUpdateEmail(String oldValue) {
        String email = "";
        do {
            email = ConsoleUtils.readStringWithDefault("Enter player email: ", Optional.of(oldValue)).get();
            if (!StringUtils.isValidEmail(email)) {
                displayErrorMessage("Invalid email format. Retype email.");
            }
        } while (!StringUtils.isValidEmail(email));
        return email;
    }

    public void displayPlayer(Player player) {
        if (player != null) {
            System.out.println("--- Player Details ---");
            System.out.println("ID: " + player.getId());
            System.out.println("Name: " + player.getName());
            System.out.println("Email: " + player.getEmail());
            System.out.println("Password: " + player.getPassword());
            System.out.println("Registration Date: " + player.getRegistrationDate());
            System.out.println("Subscribed: " + player.isSubscribed());
            System.out.println("Active: " + player.isActive());
            System.out.println("----------------------");
        } else {
            System.out.println("Player not found.");
        }
    }

    public void displayRewardWinDTOs(List<RewardWinDisplayDTO> rewardWinsData) {
        if (rewardWinsData.isEmpty()) {
            System.out.println("No reward wins found for this player.");
            return;
        }
        System.out.println("\n--- Player Reward Wins ---");
        System.out.println(StringUtils.makeLineToList(rewardWinsData.getFirst().toListHead()));
        rewardWinsData.forEach(dto -> System.out.println(
                StringUtils.makeLineToList(dto.toList())
        ));
        System.out.println("--------------------------");
    }

    public void displayCertificateWinDTOs(List<CertificateWinDisplayDTO> certificateWinsData) {
        if (certificateWinsData.isEmpty()) {
            System.out.println("No certificate wins found for this player.");
            return;
        }
        System.out.println("\n--- Player Certificate Wins ---");
        System.out.println(StringUtils.makeLineToList(certificateWinsData.getFirst().toListHead()));
        certificateWinsData.forEach(dto -> System.out.println(
                StringUtils.makeLineToList(dto.toList())
        ));
        System.out.println("-----------------------------");
    }

    public void displayPlayers(List<Player> players) {
        if (players.isEmpty()) {
            System.out.println("No players found.");
            return;
        }
        System.out.println(StringUtils.makeLineToList(PlayerMapper.toDisplayDTO(players.getFirst()).toListHead()));

        players.stream()
                .map(PlayerMapper::toDisplayDTO)
                .map(PlayerDisplayDTO::toList)
                .map(StringUtils::makeLineToList)
                .forEach(System.out::println);

        System.out.println("-------------------");
    }

    public void displayMessageln(String message) {
        System.out.println(LINE + message + LINE);
    }

    public void displayMessage(String message) {
        System.out.print(message);
    }

    public void displayErrorMessage(String message) {
        System.err.println(LINE + "ERROR: " + message + LINE);
    }
}