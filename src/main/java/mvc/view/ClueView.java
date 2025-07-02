package mvc.view;

import mvc.dto.*;
import mvc.model.Clue;

import utils.ConsoleUtils;
import utils.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ClueView {

    private static BaseView baseView = new BaseView();
    private static final String NAME_OBJECT = "Clue";

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
            baseView.displayErrorMessage("Error collecting " + NAME_OBJECT + " details: " + e.getMessage());
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

    public Clue getUpdateClueDetails(Clue clue) {
        try {
            clue.setName(getUpdateName(clue.getName()));
            clue.setDescription(getUpdateDescription(clue.getDescription()));
            clue.setPrice(getUpdatePrice(getUpdatePrice(clue.getPrice())));
            clue.setActive(getUpdateIsActive(clue.isActive()));
            return clue;
        } catch (Exception e) {
            baseView.displayErrorMessage("Error editing " + NAME_OBJECT + ": " + e.getMessage());
            throw new IllegalArgumentException("Error editing " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private String getUpdateName(String oldValue) {
        return ConsoleUtils.readStringWithDefault("Enter name: ", Optional.of(oldValue)).get();
    }

    private String getUpdateDescription(String oldValue) {
        return ConsoleUtils.readStringWithDefault("Enter description: ", Optional.of(oldValue)).get();
    }

    private BigDecimal getUpdatePrice(BigDecimal oldValue) {
        return ConsoleUtils.readBigDecimalWithDefault("Enter price: ", Optional.of(oldValue)).get();
    }

    private Boolean getUpdateIsActive(boolean oldValue) {
        return ConsoleUtils.readBooleanWithDefault("Enter is active ('Y' or 'N'): ", Optional.of(oldValue)).get();
    }

    public void displayRecordClue(Clue clue) {
        String message = "";
        if (clue != null) {
            message += baseView.LINE + "--- " + NAME_OBJECT + " Details ---" + baseView.LINE;
            message += "ID: " + clue.getId() + baseView.LINE;
            message += "Name: " + clue.getName() + baseView.LINE;
            message += "Price: $" + clue.getPrice() + baseView.LINE;
            message += "Room ID: " + clue.getIdRoom() + baseView.LINE;
            message += "Is Active: " + (clue.isActive() ? "Yes" : "No") + baseView.LINE;
            message += "-------------------------" + baseView.LINE;
        } else {
            message = NAME_OBJECT + " not found.";
        }
        baseView.displayMessageln(message);
    }

    public void displayClueListDto(List<ClueDisplayDTO> clueDisplayDTOS) {
        if (clueDisplayDTOS.isEmpty()) {
            baseView.displayMessageln("No " + NAME_OBJECT + " found.");
            return;
        }
        baseView.displayMessageln(
                StringUtils.makeLineToList(clueDisplayDTOS.getFirst().toListHead()));

        clueDisplayDTOS.forEach(rewardWins -> baseView.displayMessageln(
                StringUtils.makeLineToList(rewardWins.toList())));
        baseView.displayMessage2ln("-------------------");
    }

    public void displayClueList(List<Clue> clues) {
        if (clues.isEmpty()) {
            baseView.displayMessageln("No "+ NAME_OBJECT + " found.");
            return;
        }
        baseView.displayMessageln(
                StringUtils.makeLineToList(ClueMapper.toDisplayDTO(clues.getFirst()).toListHead()));

        clues.forEach(clue -> baseView.displayMessageln(
                StringUtils.makeLineToList(ClueMapper.toDisplayDTO(clue).toList())));
        baseView.displayMessage2ln("-------------------");
    }
}