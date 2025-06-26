package view;

import enums.OptionsMenuPlayer;
import loadConfigApp.LoadConfigApp;
import model.Player;
import lombok.extern.slf4j.Slf4j;
import utils.ConsoleUtils;
import utils.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PlayerView {

    private final String LINE = System.lineSeparator();

    public void displayPlayerMenu(String title) {
        OptionsMenuPlayer.viewMenu(LoadConfigApp.getAppName());
    }

    public Player getPlayerDetailsCreate(Player player) {
        player.setName(
                getInputString("Enter player name: "));

        player.setEmail(
                getInputEmail("Enter player email: "));

        player.setPassword(
                getInputString("Enter player password: "));

        player.setSubscribed(
                getInputBoolean("Enter player subscribed: "));

        player.setActive(
                getInputBoolean("Enter player Active: "));

        player.setRegistrationDate(LocalDateTime.now());

        player.setActive(true);

        return player;
    }


    public Player getUpdatePlayerDetails(Player player) {
        player.setName(
                getUdateString(player.getName(), "Enter player name: "));

        player.setEmail(
                getUpdateEmail(player.getEmail(), "Enter player email: "));

        player.setPassword(
                getUdateString(player.getName(), "Enter player password: "));

        player.setSubscribed(
                getUpdateBoolean(player.isSubscribed(), "Enter player subscribed: "));

        player.setActive(
                getUpdateBoolean(player.isActive(), "Enter player Active: "));

        return player;
    }

    private String getInputString(String message) {
        return ConsoleUtils.readRequiredString(message);
    }

    private boolean getInputBoolean(String message) {
        return ConsoleUtils.readRequiredBoolean(message);
    }

    private String getInputEmail(String message) {
        String email = "";
        do {
            email = getInputString(message);
            if (!StringUtils.isValidEmail(email)) {
                displayErrorMessage("Invalid email format. Retype email.");
            }
        } while (!StringUtils.isValidEmail(email));
        return email;
    }

    private String getUdateString(String oldValue, String message) {
        return ConsoleUtils.readStringWithDefault(message, Optional.of(oldValue)).get();
    }

    private Boolean getUpdateBoolean(boolean oldValue, String message) {
        return ConsoleUtils.readBooleanWithDefault(message, Optional.of(oldValue)).get();
    }


    private String getUpdateEmail(String oldValue, String message) {
        String email = "";
        do {
            email = ConsoleUtils.readStringWithDefault(message, Optional.of(oldValue)).get();
            if (!StringUtils.isValidEmail(email)) {
                displayErrorMessage("Invalid email format. Retype email.");
            }
        } while (!StringUtils.isValidEmail(email));
        return email;
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


    public void displayPlayers(List<Player> players) {

        System.out.println(makeHeadLineToList());
        if (players.isEmpty()) {
            System.out.println("No players found.");
            return;
        }
        players.forEach(player -> System.out.println(
                makeLineToList(player.toList())
        ));
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

    private String makeHeadLineToList() {
        ArrayList<String> dataLine = new ArrayList<>();
        dataLine.add("ID");
        dataLine.add("NAME");
        dataLine.add("EMAIL");
        dataLine.add("PASSWORD");
        dataLine.add("REG.DATE");
        dataLine.add("SUBSCRIBE");
        dataLine.add("ACTIVE");
        return makeLineToList(dataLine);
    }

    private String makeLineToList(ArrayList<String> dataLine) {
        int i = 0;

        return StringUtils.formatToChars(dataLine.get(i++), 8) +
                StringUtils.formatToChars(dataLine.get(i++), 25) +
                StringUtils.formatToChars(dataLine.get(i++), 35) +
                StringUtils.formatToChars(dataLine.get(i++), 25) +
                StringUtils.formatToChars(dataLine.get(i++), 24) +
                StringUtils.formatToChars(dataLine.get(i++), 8) +
                StringUtils.formatToChars(dataLine.get(i), 8);
    }
}