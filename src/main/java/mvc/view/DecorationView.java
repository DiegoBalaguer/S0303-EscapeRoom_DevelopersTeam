package mvc.view;

import mvc.enumsMenu.OptionsMenuItem;
import lombok.extern.slf4j.Slf4j;
import mvc.model.Decoration;
import utils.ConsoleUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
public class DecorationView {

    public void displayItemMenu(String title) {
        OptionsMenuItem.viewMenuItem(title);
    }

    public Decoration getDecorationDetails() {
        try {
            String name = ConsoleUtils.readRequiredString("Enter decoration name: ");
            int idRoom = ConsoleUtils.readRequiredInt("Enter Room ID (existing): ");
            BigDecimal price = ConsoleUtils.readRequiredBigDecimal("Enter decoration price: ");
            String description = ConsoleUtils.readRequiredString("Enter decoration description: ");
            boolean isActive = ConsoleUtils.readRequiredBoolean("Is the decoration active? (T/F, Y/N, S/N): ");

            return Decoration.builder()
                    .name(name)
                    .idRoom(idRoom)
                    .price(price)
                    .isActive(isActive)
                    .build();

        } catch (Exception e) {
            displayErrorMessage("Error collecting decoration details: " + e.getMessage());
            return null;
        }
    }

    public Optional<Integer> getDecorationId() {
        try {
            int id = ConsoleUtils.readRequiredInt("Enter decoration ID: ");
            return Optional.of(id);
        } catch (NumberFormatException e) {
            displayErrorMessage("Invalid ID. Please enter a valid number.");
            return Optional.empty();
        }
    }

    public void displayDecoration(Decoration decoration) {
        if (decoration != null) {
            System.out.println("\n--- Decoration Details ---");
            System.out.println("ID: " + decoration.getId());
            System.out.println("Name: " + decoration.getName());
            System.out.println("Price: $" + decoration.getPrice());
            System.out.println("Room ID: " + decoration.getIdRoom());
            System.out.println("Is Active: " + (decoration.isActive() ? "Yes" : "No"));
            System.out.println("-------------------------");
        } else {
            System.out.println("Decoration not found.");
        }
    }

    public void displayDecorations(List<Decoration> decorations) {
        System.out.println("\n--- List of Decorations ---");
        for (Decoration decoration : decorations) {
            displayDecoration(decoration);
        }
        System.out.println("-------------------------");
    }

    public void displayMessage(String message) {
        System.out.println("\n" + message + "\n");
    }

    public void displayErrorMessage(String message) {
        System.err.println("\nERROR: " + message + "\n");
    }

    public Decoration editDecoration(Decoration currentDecoration) {
        try {
            System.out.println("\n=== EDIT DECORATION ===");
            String name = ConsoleUtils.readOptionalString("Current Name: " + currentDecoration.getName() + ". New Name: ").orElse(currentDecoration.getName());
            BigDecimal price = ConsoleUtils.readOptionalBigDecimal("Current Price: " + currentDecoration.getPrice() + ". New Price: ").orElse(currentDecoration.getPrice());
            String description = ConsoleUtils.readOptionalString("Current Description: " + currentDecoration.getDescription() + ". New Description: ").orElse(currentDecoration.getDescription());
            boolean isActive = ConsoleUtils.readOptionalBoolean("Current Active Status: " + (currentDecoration.isActive() ? "Yes" : "No") + ". New Status (true/false): ").orElse(currentDecoration.isActive());

            return Decoration.builder()
                    .id(currentDecoration.getId())
                    .name(name)
                    .idRoom(currentDecoration.getIdRoom())
                    .price(price)
                    .description(description)
                    .isActive(isActive)
                    .build();

        } catch (Exception e) {
            displayErrorMessage("Error editing decoration: " + e.getMessage());
            return null;
        }
    }
}