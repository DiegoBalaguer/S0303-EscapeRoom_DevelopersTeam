package view;
import enums.Theme;
import enums.Difficulty;
import enums.OptionsMenuCrud;
import lombok.extern.slf4j.Slf4j;
import model.Room;
import utils.ConsoleUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j // Para logging
public class RoomView {

    public void displayRoomMenu(String title) {
        OptionsMenuCrud.viewMenuCrud(title); // Menú de CRUD adaptado para gestionar Rooms
    }

    public Room getRoomDetails(boolean forUpdate) {
        try {
            String name = ConsoleUtils.readRequiredString("Enter room name: ");
            if (name == null || name.isEmpty()) {
                displayErrorMessage("Room name cannot be empty. Operation canceled.");
                return null;
            }

            String description = ConsoleUtils.readRequiredString("Enter room description: ");
            if (description == null || description.isEmpty()) {
                displayErrorMessage("Room description cannot be empty. Operation canceled.");
                return null;
            }

            BigDecimal price = ConsoleUtils.readRequiredBigDecimal("Enter room price: ");
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                displayErrorMessage("Invalid price. Operation canceled.");
                return null;
            }

            String difficultyInput = ConsoleUtils.readRequiredString("Enter room difficulty (EASY, MEDIUM, HARD): ");
            Difficulty difficulty;
            try {
                difficulty = Difficulty.valueOf(difficultyInput.toUpperCase());
            } catch (IllegalArgumentException e) {
                displayErrorMessage("Invalid difficulty. Allowed values: EASY, MEDIUM, HARD.");
                return null;
            }
            String themeInput = ConsoleUtils.readRequiredString("Enter room theme (EGYPT, SPACE, GANGSTERS): ");
            Theme theme;
            try {
                theme = Theme.valueOf(themeInput.toUpperCase()); // Validar input contra el enum Theme
            } catch (IllegalArgumentException e) {
                displayErrorMessage("Invalid theme. Allowed values: EGYPT, SPACE, GANGSTERS.");
                return null;
            }

            // Uso del método booleano de ConsoleUtils
            boolean isActive = ConsoleUtils.readRequiredBoolean("Is the room active? (T/F, Y/N, S/N): ");

            return Room.builder()
                    .idEscapeRoom(forUpdate ? 0 : 1) // Uso de ID ficticio si no se está actualizando
                    .name(name)
                    .description(description)
                    .price(price)
                    .difficulty(difficulty)
                    .theme(theme)
                    .isActive(isActive)
                    .build();

        } catch (Exception e) {
            displayErrorMessage("Error collecting room details: " + e.getMessage());
            return null;
        }

    }

    public Optional<Integer> getRoomId() {
        try {
            int id = ConsoleUtils.readRequiredInt("Enter room ID: ");
            return Optional.of(id);
        } catch (NumberFormatException e) {
            displayErrorMessage("Invalid ID. Please enter a number.");
            return Optional.empty();
        }
    }

    public void displayRoom(Room room) {
        if (room != null) {
            System.out.println("\n--- Room Details ---");
            System.out.println("ID: " + room.getId());
            System.out.println("Name: " + room.getName());
            System.out.println("Description: " + room.getDescription());
            System.out.println("Price: $" + room.getPrice());
            System.out.println("Difficulty: " + room.getDifficulty());
            System.out.println("Is Active: " + (room.isActive() ? "Yes" : "No"));
            System.out.println("-------------------------");
        } else {
            System.out.println("Room not found.");
        }
    }


    public void displayMessage(String message) {
        System.out.println(System.lineSeparator() + message + System.lineSeparator());
    }

    public void displayErrorMessage(String message) {
        System.err.println(System.lineSeparator() + "ERROR: " + message + System.lineSeparator());
    }
    public Room getRoomDetailsWithDefaults(Room currentRoom) {
        try {
            // Instrucciones para el usuario
            displayMessage("=== UPDATE ROOM DETAILS ===");
            displayMessage("Press Enter to keep the current value for each field.\n");

            // Campo: Nombre
            displayMessage("Current Name: " + currentRoom.getName());
            Optional<String> nameInput = ConsoleUtils.readOptionalString("New Name: ");
            String updatedName = nameInput.orElse(currentRoom.getName());

            // Campo: Descripción
            displayMessage("Current Description: " + currentRoom.getDescription());
            Optional<String> descriptionInput = ConsoleUtils.readOptionalString("New Description: ");
            String updatedDescription = descriptionInput.orElse(currentRoom.getDescription());

            // Campo: Precio
            displayMessage("Current Price: " + currentRoom.getPrice());
            Optional<String> priceInput = ConsoleUtils.readOptionalString("New Price (leave empty to keep): ");
            BigDecimal updatedPrice = priceInput.isEmpty()
                    ? currentRoom.getPrice()
                    : new BigDecimal(priceInput.get());

            // Campo: Dificultad
            displayMessage("Current Difficulty: " + currentRoom.getDifficulty());
            Optional<String> difficultyInput = ConsoleUtils.readOptionalString("New Difficulty (0: EASY, 1: MEDIUM, 2: HARD): ");
            Difficulty updatedDifficulty = difficultyInput.isEmpty()
                    ? currentRoom.getDifficulty()
                    : Difficulty.values()[Integer.parseInt(difficultyInput.get())];

            // Campo: Tema
            displayMessage("Current Theme: " + currentRoom.getTheme());
            Optional<String> themeInput = ConsoleUtils.readOptionalString("New Theme (0: HORROR, 1: ADVENTURE, 2: MYSTERY): ");
            Theme updatedTheme = themeInput.isEmpty()
                    ? currentRoom.getTheme()
                    : Theme.values()[Integer.parseInt(themeInput.get())];

            // Campo: Activo
            displayMessage("Current Active Status: " + currentRoom.isActive());
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
            displayErrorMessage("Error updating room: " + e.getMessage());
            return null; // Si ocurre un error, devolver null
        }
    }
}