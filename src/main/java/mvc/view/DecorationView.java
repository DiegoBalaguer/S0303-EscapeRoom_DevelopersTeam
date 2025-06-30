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

    private static BaseView baseView =  new BaseView();

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
            baseView.displayErrorMessage("Error collecting decoration details: " + e.getMessage());
            return null;
        }
    }

    public Optional<Integer> getDecorationId() {
        try {
            int id = ConsoleUtils.readRequiredInt("Enter decoration ID: ");
            return Optional.of(id);
        } catch (NumberFormatException e) {
            baseView.displayErrorMessage("Invalid ID. Please enter a valid number.");
            return Optional.empty();
        }
    }

    public void displayDecoration(Decoration decoration) {
        String message = "";
        if (decoration != null) {
            message += baseView.LINE + "--- Decoration Details ---" + baseView.LINE;
            message += "ID: " + decoration.getId() + baseView.LINE;
            message += "Name: " + decoration.getName() + baseView.LINE;
            message += "Price: $" + decoration.getPrice() + baseView.LINE;
            message += "Room ID: " + decoration.getIdRoom() + baseView.LINE;
            message += "Is Active: " + (decoration.isActive() ? "Yes" : "No") + baseView.LINE;
            message += "-------------------------" + baseView.LINE;
        } else {
            message = "Decoration not found.";
        }
        baseView.displayMessageln(message);
    }

    public void displayDecorations(List<Decoration> decorations) {
        baseView.displayMessageln(baseView.LINE + "--- List of Decorations ---");
        for (Decoration decoration : decorations) {
            displayDecoration(decoration);
        }
        baseView.displayMessage2ln("-------------------------");
    }

    public Decoration editDecoration(Decoration currentDecoration) {
        try {
            baseView.displayMessageln(baseView.LINE + "=== EDIT DECORATION ===");
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
            baseView.displayErrorMessage("Error editing decoration: " + e.getMessage());
            return null;
        }
    }
}