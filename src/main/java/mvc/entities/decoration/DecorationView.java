package mvc.entities.decoration;

import mvc.entities.Inputs;
import mvc.entities.MessageMinMax;
import mvc.view.BaseView;
import utils.ConsoleUtils;

import java.math.BigDecimal;

import java.util.Optional;

public class DecorationView {

    private static DecorationView instance;
    private final BaseView BASE_VIEW;
    private static final String NAME_OBJECT = "Decoration";

    private DecorationView() {
        BASE_VIEW = BaseView.getInstance();
        BASE_VIEW.displayDebugMessage("Created Class: " + this.getClass().getName());
    }

    protected static DecorationView getInstance() {
        if (instance == null) {
            synchronized (DecorationView.class) {
                if (instance == null) {
                    instance = new DecorationView();
                }
            }
        }
        return instance;
    }

    protected Inputs getDecorationDetailsCreate(MessageMinMax roomsList) {

        Optional<String> name = getInputName();
        Optional<Integer> roomId = getEntityId("List of Rooms", "Input Room ID: ", roomsList);
        BigDecimal price = getInputPrice();
        Optional<String> description = getInputDescription();

        return new Inputs.Builder()
                .put("name", name.orElse(null))
                .put("roomId", roomId.orElse(null))
                .put("price", price)
                .put("description", description.orElse(null))
                .build();
    }

    protected Optional<Integer> getEntityId(String title, String inputText, MessageMinMax messageMinMax) {
        return BASE_VIEW.getReadValueIntMinMax(title, inputText, messageMinMax);
    }

    private Optional<String> getInputName() {
        return BASE_VIEW.getReadValueString("Enter name: ");
    }

    private Optional<String> getInputDescription() {
        return BASE_VIEW.getReadValueString("Enter description (30 chars): ");
    }

    private BigDecimal getInputPrice() {
        return ConsoleUtils.readRequiredBigDecimal("Enter price: ");
    }

    protected Decoration getUpdateDecorationDetails(Decoration decoration, MessageMinMax roomsList) {
        decoration.setName(getUpdateName(decoration.getName()));
        decoration.setIdRoom(getUpdateRoomId(roomsList, decoration.getIdRoom()));
        decoration.setDescription(getUpdateDescription(decoration.getDescription()));
        decoration.setPrice(getUpdatePrice(decoration.getPrice()));
        decoration.setActive(getUpdateIsActive(decoration.isActive()));
        return decoration;
    }

    private int getUpdateRoomId(MessageMinMax message, int oldValue) {
        Optional<Integer> roomId = BASE_VIEW.getReadValueIntMinMax("List of Rooms", "Input Room ID: ", message);
        return roomId.orElse(oldValue);
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

    protected void displayRecordDecoration(Decoration decoration) {
        String message = "";
        if (decoration != null) {
            message += BASE_VIEW.LINE + "--- " + NAME_OBJECT + " Details ---" + BASE_VIEW.LINE;
            message += "ID: " + decoration.getId() + BASE_VIEW.LINE;
            message += "Name: " + decoration.getName() + BASE_VIEW.LINE;
            message += "Price: $" + decoration.getPrice() + BASE_VIEW.LINE;
            message += "Room ID: " + decoration.getIdRoom() + BASE_VIEW.LINE;
            message += "Is Active: " + (decoration.isActive() ? "Yes" : "No") + BASE_VIEW.LINE;
            message += "-------------------------" + BASE_VIEW.LINE;
        } else {
            message = NAME_OBJECT + " not found.";
        }
        BASE_VIEW.displayMessageln(message);
    }
}