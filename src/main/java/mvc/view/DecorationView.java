package mvc.view;

import mvc.dto.DecorationDisplayDTO;
import mvc.dto.DecorationMapper;
import mvc.model.Decoration;
import utils.ConsoleUtils;
import utils.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class DecorationView {

    private static BaseView baseView = new BaseView();
    private static final String NAME_OBJECT = "Decoration";

    public Decoration getDecorationDetailsCreate(int roomId) {
        try {
            return Decoration.builder()
                    .name(getInputName())
                    .idRoom(roomId)
                    .price(getInputPrice())
                    .description(getInputDescription())
                    .isActive(true)
                    .build();
        } catch (Exception e) {
            baseView.displayErrorMessage("Error collecting decoration details: " + e.getMessage());
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

    public Decoration getUpdateDecorationDetails(Decoration decoration) {
        try {
            decoration.setName(getUpdateName(decoration.getName()));
            decoration.setDescription(getUpdateDescription(decoration.getDescription()));
            decoration.setPrice(getUpdatePrice(decoration.getPrice()));
            decoration.setActive(getUpdateIsActive(decoration.isActive()));
            return decoration;
        } catch (Exception e) {
            baseView.displayErrorMessage("Error editing decoration: " + e.getMessage());
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

    public void displayRecordDecoration(Decoration decoration) {
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

    public void displayDecorationListDto(List<DecorationDisplayDTO> decorationDisplayDTOS) {
        if (decorationDisplayDTOS.isEmpty()) {
            baseView.displayMessageln("No decorations found.");
            return;
        }
        baseView.displayMessageln(
                StringUtils.makeLineToList(decorationDisplayDTOS.get(0).toListHead()));

        decorationDisplayDTOS.forEach(dto -> baseView.displayMessageln(
                StringUtils.makeLineToList(dto.toList())));
        baseView.displayMessage2ln("-------------------");
    }

    public void displayDecorationList(List<Decoration> decorations) {
        if (decorations.isEmpty()) {
            baseView.displayMessageln("No Decorations found.");
            return;
        }
        baseView.displayMessageln(
                StringUtils.makeLineToList(DecorationMapper.toDisplayDTO(decorations.get(0)).toListHead()));

        decorations.forEach(decoration -> baseView.displayMessageln(
                StringUtils.makeLineToList(DecorationMapper.toDisplayDTO(decoration).toList())));
        baseView.displayMessage2ln("-------------------");
    }
}