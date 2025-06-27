package view;

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

        // Validar precio
        BigDecimal price = ConsoleUtils.readRequiredBigDecimal("Enter room price: ");
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            displayErrorMessage("Invalid price. Operation canceled.");
            return null;
        }

        String difficultyInput = ConsoleUtils.readRequiredString("Enter room difficulty (EASY, MEDIUM, HARD): ");
        Difficulty difficulty;
        try {
            difficulty = Difficulty.valueOf(difficultyInput.toUpperCase());
        } catch (IllegalArgumentException e) {
            displayErrorMessage("Invalid difficulty. Operation canceled.");
            return null;
        }

        // Se puede asignar un ID de escape room predeterminado en función de la lógica de negocio
        int escapeRoomId = 1;
        boolean isActive = ConsoleUtils.readRequiredBoolean("Is the room active? (yes/no): ");

        return Room.builder()
                .idEscapeRoom(escapeRoomId)
                .name(name)
                .description(description)
                .price(price)
                .difficulty(difficulty)
                .isActive(isActive)
                .build();
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

    public void displayRooms(List<Room> rooms) {
        if (rooms.isEmpty()) {
            System.out.println("No rooms found.");
            return;
        }
        System.out.println("\n--- All Rooms ---");
        rooms.forEach(room -> System.out.println(
                "ID: " + room.getId() +
                        ", Name: " + room.getName() +
                        ", Description: " + room.getDescription() +
                        ", Price: $" + room.getPrice() +
                        ", Difficulty: " + room.getDifficulty() +
                        ", Is Active: " + (room.isActive() ? "Yes" : "No")
        ));
        System.out.println("---------------------------");
    }

    public void displayMessage(String message) {
        System.out.println(System.lineSeparator() + message + System.lineSeparator());
    }

    public void displayErrorMessage(String message) {
        System.err.println(System.lineSeparator() + "ERROR: " + message + System.lineSeparator());
    }
}