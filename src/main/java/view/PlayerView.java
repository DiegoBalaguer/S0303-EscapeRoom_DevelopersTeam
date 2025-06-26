package view;


import enums.OptionsMenuPlayer;
import loadConfigApp.LoadConfigApp;
import model.Player;
import lombok.extern.slf4j.Slf4j;
import utils.ConsoleUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j // Para logging
public class PlayerView {

    public void displayPlayerMenu(String title) {
        OptionsMenuPlayer.viewMenu(LoadConfigApp.getAppName());
    }

    public Player getPlayerDetails(boolean forUpdate) {
        String name = ConsoleUtils.readRequiredString("Enter player name: ");
        if (name == null || name.isEmpty()) {
            displayErrorMessage("Player name cannot be empty. Operation canceled.");
            return null;
        }

        String email = ConsoleUtils.readRequiredString("Enter player email: ");
        if (email == null || !isValidEmail(email)) {
            displayErrorMessage("Invalid email format. Operation canceled.");
            return null;
        }

        String password = ConsoleUtils.readRequiredString("Enter player password: ");

        boolean subscribed = ConsoleUtils.readRequiredBoolean("Enter player subscribed: ");


        // Si es para crear, asignamos la fecha de registro actual
        LocalDate registrationDate = forUpdate ? null : LocalDate.now(); // null si es update, para no sobrescribir

        return Player.builder()
                .name(name)
                .email(email)
                .password(password)
                .registrationDate(registrationDate)
                .build();
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
            System.out.println("\n--- Player Details ---");
            System.out.println("ID: " + player.getId());
            System.out.println("Name: " + player.getName());
            System.out.println("Email: " + player.getEmail());
            System.out.println("Registration Date: " + player.getRegistrationDate());
            System.out.println("----------------------");
        } else {
            System.out.println("Player not found.");
        }
    }

    public void displayPlayers(List<Player> players) {
        if (players.isEmpty()) {
            System.out.println("No players found.");
            return;
        }
        System.out.println("\n--- All Players ---");
        players.forEach(player -> System.out.println(
                "ID: " + player.getId() +
                        ", Name: " + player.getName() +
                        ", Email: " + player.getEmail() +
                        ", Reg. Date: " + player.getRegistrationDate()
        ));
        System.out.println("-------------------");
    }

    public void displayMessage(String message) {
        System.out.println(System.lineSeparator() + message + System.lineSeparator());
    }

    public void displayErrorMessage(String message) {
        System.err.println(System.lineSeparator() + "ERROR: " + message + System.lineSeparator());
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }
}