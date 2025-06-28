package view;

import enums.OptionsMenuItem;
import lombok.extern.slf4j.Slf4j;
import model.Clue;
import utils.ConsoleUtils;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ClueView {

    public void displayClueMenu(String title) {
        OptionsMenuItem.viewMenuItem(title);
    }

    public Clue getClueDetails() {
        try {
            String name = ConsoleUtils.readRequiredString("Enter clue name: ");
            int idRoom = ConsoleUtils.readRequiredInt("Enter Room ID (existing): ");
            BigDecimal price = ConsoleUtils.readRequiredBigDecimal("Enter clue price: ");
            boolean isActive = ConsoleUtils.readRequiredBoolean("Is the clue active? (T/F, Y/N, S/N): ");

            return Clue.builder()
                    .name(name)
                    .idRoom(idRoom)
                    .price(price)
                    .isActive(isActive)
                    .build();

        } catch (Exception e) {
            displayErrorMessage("Error collecting clue details: " + e.getMessage());
            return null;
        }
    }

    public Optional<Integer> getClueId() {
        try {
            int id = ConsoleUtils.readRequiredInt("Enter clue ID: ");
            return Optional.of(id);
        } catch (NumberFormatException e) {
            displayErrorMessage("Invalid ID. Please enter a valid number.");
            return Optional.empty();
        }
    }

    public void displayClue(Clue clue) {
        if (clue != null) {
            System.out.println("\n--- Clue Details ---");
            System.out.println("ID: " + clue.getId());
            System.out.println("Name: " + clue.getName());
            System.out.println("Price: $" + clue.getPrice());
            System.out.println("Room ID: " + clue.getIdRoom());
            System.out.println("Is Active: " + (clue.isActive() ? "Yes" : "No"));
            System.out.println("-------------------------");
        } else {
            System.out.println("Clue not found.");
        }
    }

    public void displayClues(List<Clue> clues) {
        System.out.println("\n--- List of Clues ---");
        for (Clue clue : clues) {
            displayClue(clue);
        }
        System.out.println("-------------------------");
    }

    public void displayMessage(String message) {
        System.out.println("\n" + message + "\n");
    }

    public void displayErrorMessage(String message) {
        System.err.println("\nERROR: " + message + "\n");
    }

    public Clue editClue(Clue currentClue) {
        try {
            System.out.println("\n=== EDIT CLUE ===");
            String name = ConsoleUtils.readOptionalString("Current Name: " + currentClue.getName() + ". New Name: ").orElse(currentClue.getName());
            BigDecimal price = ConsoleUtils.readOptionalBigDecimal("Current Price: " + currentClue.getPrice() + ". New Price: ").orElse(currentClue.getPrice());
            boolean isActive = ConsoleUtils.readOptionalBoolean("Current Active Status: " + (currentClue.isActive() ? "Yes" : "No") + ". New Status (true/false): ").orElse(currentClue.isActive());

            return Clue.builder()
                    .id(currentClue.getId())
                    .name(name)
                    .idRoom(currentClue.getIdRoom())
                    .price(price)
                    .isActive(isActive)
                    .build();

        } catch (Exception e) {
            displayErrorMessage("Error editing clue: " + e.getMessage());
            return null;
        }
    }

}