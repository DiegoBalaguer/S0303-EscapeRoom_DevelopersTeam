package mvc.view;
import enums.Theme;
import enums.Difficulty;
import mvc.dto.RoomMapper;
import lombok.extern.slf4j.Slf4j;
import mvc.model.Room;
import utils.ConsoleUtils;
import utils.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j // Para logging
public class RoomView {

    private final BaseView baseView = new BaseView();
    private static final String NAME_OBJECT = "Room";

    public Room getRoomDetails(boolean forUpdate) {
        try {
            String name = ConsoleUtils.readRequiredString("Enter room name: ");
            if (name == null || name.isEmpty()) {
                baseView.displayErrorMessage("Room name cannot be empty. Operation canceled.");
                return null;
            }

            String description = ConsoleUtils.readRequiredString("Enter room description: ");
            if (description == null || description.isEmpty()) {
                baseView.displayErrorMessage("Room description cannot be empty. Operation canceled.");
                return null;
            }

            BigDecimal price = ConsoleUtils.readRequiredBigDecimal("Enter room price: ");
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                baseView.displayErrorMessage("Invalid price. Operation canceled.");
                return null;
            }

            String difficultyInput = ConsoleUtils.readRequiredString("Enter room difficulty (EASY, MEDIUM, HARD): ");
            Difficulty difficulty;
            try {
                difficulty = Difficulty.valueOf(difficultyInput.toUpperCase());
            } catch (IllegalArgumentException e) {
                baseView.displayErrorMessage("Invalid difficulty. Allowed values: EASY, MEDIUM, HARD.");
                return null;
            }
            String themeInput = ConsoleUtils.readRequiredString("Enter room theme (EGYPT, SPACE, GANGSTERS): ");
            Theme theme;
            try {
                theme = Theme.valueOf(themeInput.toUpperCase());
            } catch (IllegalArgumentException e) {
                baseView.displayErrorMessage("Invalid theme. Allowed values: EGYPT, SPACE, GANGSTERS.");
                return null;
            }

            boolean isActive = ConsoleUtils.readRequiredBoolean("Enter is active ('Y' or 'N'): ");

            return Room.builder()
                    .idEscapeRoom(forUpdate ? 0 : 1)
                    .name(name)
                    .description(description)
                    .price(price)
                    .difficulty(difficulty)
                    .theme(theme)
                    .isActive(isActive)
                    .build();

        } catch (Exception e) {
            baseView.displayErrorMessage("Error collecting room details: " + e.getMessage());
            return null;
        }

    }

    public Optional<Integer> getRoomId() {
        try {
            int id = ConsoleUtils.readRequiredInt("Enter room ID: ");
            return Optional.of(id);
        } catch (NumberFormatException e) {
            baseView.displayErrorMessage("Invalid ID. Please enter a number.");
            return Optional.empty();
        }
    }

    public void displayRoom(Room room) {
        String message = "";
        if (room != null) {
            message += "--- Room Details ---" + baseView.LINE;
            message += "ID: " + room.getId() + baseView.LINE;
            message += "Name: " + room.getName() + baseView.LINE;
            message += "Description: " + room.getDescription() + baseView.LINE;
            message += "Price: $" + room.getPrice() + baseView.LINE;
            message += "Difficulty: " + room.getDifficulty() + baseView.LINE;
            message += "Is Active: " + (room.isActive() ? "Yes" : "No") + baseView.LINE;
            message += "-------------------------" + baseView.LINE;
        } else {
            message = "Room not found.";
        }
        baseView.displayMessageln(message);
    }


    public Room getRoomDetailsWithDefaults(Room currentRoom) {
        try {
            // Instrucciones para el usuario
            baseView.displayMessageln("=== UPDATE ROOM DETAILS ===");
            baseView.displayMessageln("Press Enter to keep the current value for each field.\n");

            // Campo: Nombre
            baseView.displayMessageln("Current Name: " + currentRoom.getName());
            Optional<String> nameInput = ConsoleUtils.readOptionalString("New Name: ");
            String updatedName = nameInput.orElse(currentRoom.getName());

            // Campo: Descripción
            baseView.displayMessageln("Current Description: " + currentRoom.getDescription());
            Optional<String> descriptionInput = ConsoleUtils.readOptionalString("New Description: ");
            String updatedDescription = descriptionInput.orElse(currentRoom.getDescription());

            // Campo: Precio
            baseView.displayMessageln("Current Price: " + currentRoom.getPrice());
            Optional<String> priceInput = ConsoleUtils.readOptionalString("New Price (leave empty to keep): ");
            BigDecimal updatedPrice = priceInput.isEmpty()
                    ? currentRoom.getPrice()
                    : new BigDecimal(priceInput.get());

            // Campo: Dificultad
            baseView.displayMessageln("Current Difficulty: " + currentRoom.getDifficulty());
            Optional<String> difficultyInput = ConsoleUtils.readOptionalString("New Difficulty (0: EASY, 1: MEDIUM, 2: HARD): ");
            Difficulty updatedDifficulty = difficultyInput.isEmpty()
                    ? currentRoom.getDifficulty()
                    : Difficulty.values()[Integer.parseInt(difficultyInput.get())];

            // Campo: Tema
            baseView.displayMessageln("Current Theme: " + currentRoom.getTheme());
            Optional<String> themeInput = ConsoleUtils.readOptionalString("New Theme (0: HORROR, 1: ADVENTURE, 2: MYSTERY): ");
            Theme updatedTheme = themeInput.isEmpty()
                    ? currentRoom.getTheme()
                    : Theme.values()[Integer.parseInt(themeInput.get())];

            // Campo: Activo
            baseView.displayMessageln("Current Active Status: " + currentRoom.isActive());
            Optional<String> activeInput = ConsoleUtils.readOptionalString("Is Active? (true/false, leave empty to keep): ");
            boolean updatedIsActive = activeInput.isEmpty()
                    ? currentRoom.isActive()
                    : Boolean.parseBoolean(activeInput.get());

            // Finalmente, construir el objeto actualizado con los nuevos o valores existentes
            return Room.builder()
                    .id(currentRoom.getId()) // El ID no cambia
                    .name(updatedName)
                    .description(updatedDescription)
                    .price(updatedPrice)
                    .difficulty(updatedDifficulty)
                    .theme(updatedTheme)
                    .active(updatedIsActive)
                    .build();

        } catch (Exception e) {
            baseView.displayErrorMessage("Error updating room: " + e.getMessage());
            return null;
        }
    }

    public void displayListRooms(List<Room> rooms) {
        if (rooms.isEmpty()) {
            baseView.displayMessageln("No " + NAME_OBJECT + " found.");
            return;
        }
        baseView.displayMessageln(
                StringUtils.makeLineToList(RoomMapper.toDisplayDTO(rooms.getFirst()).toListHead()));

        rooms.forEach(room -> baseView.displayMessageln(
                StringUtils.makeLineToList(RoomMapper.toDisplayDTO(room).toList())));
        baseView.displayMessage2ln("-------------------");
    }
}