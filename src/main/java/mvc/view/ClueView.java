package mvc.view;

import mvc.dto.*;
import mvc.enumsMenu.OptionsMenuItem;
import lombok.extern.slf4j.Slf4j;
import mvc.model.Certificate;
import mvc.model.Clue;
import mvc.model.Player;
import mvc.model.Room;
import utils.ConsoleUtils;
import utils.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ClueView {

    private static BaseView baseView =  new BaseView();
    
    public void displayClueMenu(String title) {
        OptionsMenuItem.viewMenuItem(title);
    }

    public Clue getClueDetailsCreate(int roomId) {
        try {
            return Clue.builder()
                    .name(getInputName())
                    .idRoom(roomId)
                    .price(getInputPrice())
                    .description(getInputDescription())
                    .isActive(true)
                    .build();
        } catch (Exception e) {
            baseView.displayErrorMessage("Error collecting clue details: " + e.getMessage());
            return null;
        }
    }

    private String getInputName() {
        return ConsoleUtils.readRequiredString("Enter name: ");
    }

    private String getInputDescription() {
        return ConsoleUtils.readRequiredString("Enter description: ");
    }

    private BigDecimal getInputPrice() {
        return ConsoleUtils.readRequiredBigDecimal("Enter price: ");
    }


    public Optional<Integer> getClueId() {
        try {
            int id = ConsoleUtils.readRequiredInt("Enter clue ID: ");
            return Optional.of(id);
        } catch (NumberFormatException e) {
            baseView.displayErrorMessage("Invalid ID. Please enter a valid number.");
            return Optional.empty();
        }
    }

    public void displayClue(Clue clue) {
        String message = "";
        if (clue != null) {
            message += baseView.LINE + "--- Clue Details ---" + baseView.LINE;
            message += "ID: " + clue.getId() + baseView.LINE;
            message += "Name: " + clue.getName() + baseView.LINE;
            message += "Price: $" + clue.getPrice() + baseView.LINE;
            message += "Room ID: " + clue.getIdRoom() + baseView.LINE;
            message += "Is Active: " + (clue.isActive() ? "Yes" : "No") + baseView.LINE;
            message += "-------------------------" + baseView.LINE;
        } else {
            message = "Clue not found.";
        }
        baseView.displayMessageln(message);
    }

    public Clue editClue(Clue currentClue) {
        try {
            baseView.displayMessageln(baseView.LINE + "=== EDIT CLUE ===");
            String name = ConsoleUtils.readOptionalString("Current Name: " + currentClue.getName() + ". New Name: ").orElse(currentClue.getName());
            BigDecimal price = ConsoleUtils.readOptionalBigDecimal("Current Price: " + currentClue.getPrice() + ". New Price: ").orElse(currentClue.getPrice());
            String description = ConsoleUtils.readOptionalString("Current Description: " + currentClue.getDescription() + ". New Description: ").orElse(currentClue.getDescription()); // Editar descripción
            boolean isActive = ConsoleUtils.readOptionalBoolean("Current Active Status: " + (currentClue.isActive() ? "Yes" : "No") + ". New Status (true/false): ").orElse(currentClue.isActive());

            return Clue.builder()
                    .id(currentClue.getId())
                    .name(name)
                    .idRoom(currentClue.getIdRoom())
                    .price(price)
                    .description(description) // Asignar descripción actualizada
                    .isActive(isActive)
                    .build();

        } catch (Exception e) {
            baseView.displayErrorMessage("Error editing clue: " + e.getMessage());
            return null;
        }
    }

    public void displayClueList(List<ClueDisplayDTO> clueDisplayDTOS) {
        if (clueDisplayDTOS.isEmpty()) {
            baseView.displayMessageln("No clues found.");
            return;
        }
        baseView.displayMessageln(
                StringUtils.makeLineToList(clueDisplayDTOS.getFirst().toListHead()));

        clueDisplayDTOS.forEach(rewardWins -> baseView.displayMessageln(
                StringUtils.makeLineToList(rewardWins.toList())));
        baseView.displayMessage2ln("-------------------");
    }

    public void displayClues(List<Clue> clues) {
        if (clues.isEmpty()) {
            baseView.displayMessageln("No Clues found.");
            return;
        }
        baseView.displayMessageln(
                StringUtils.makeLineToList(ClueMapper.toDisplayDTO(clues.getFirst()).toListHead()));

        clues.forEach(clue -> baseView.displayMessageln(
                StringUtils.makeLineToList(ClueMapper.toDisplayDTO(clue).toList())));
        baseView.displayMessage2ln("-------------------");
    }

}