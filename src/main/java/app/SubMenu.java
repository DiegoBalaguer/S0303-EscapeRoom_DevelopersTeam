package app;

import abstractEscapeRoom.AbstractEscapeRoom;
import crud.*;
import enums.Difficulty;
import interfaces.AbstractFactory;
import interfaces.Command;
import mvc.model.Element;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SubMenu<T extends Element> {

    private static final String INVALID_OPTION_MESSAGE = "Invalid option. Try again.";
    private static final String EMPTY_FIELD_MESSAGE = "Field cannot be empty. Operation canceled.";
    private static final String SUBMENU_HEADER = "\n=== Submenu ===";

    private final List<T> elements = new ArrayList<>();
    private final Actions<T> actions = new Actions<>();

    public void showSubMenu() {
        Scanner userInput = new Scanner(System.in);

        while (true) {
            System.out.println(SUBMENU_HEADER);
            System.out.println("1. Add Element");
            System.out.println("2. Show Elements");
            System.out.println("3. Delete Element");
            System.out.println("4. Calculate Total Price");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select an option: ");

            int userChoice = userInput.nextInt();
            userInput.nextLine(); // Clear buffer

            switch (userChoice) {
                case 1 -> addElement(userInput);
                case 2 -> showElements();
                case 3 -> deleteElement(userInput);
                case 4 -> calculateTotalPrice();
                case 0 -> {
                    System.out.println("Returning to Main Menu...");
                    return;
                }
                default -> System.out.println(INVALID_OPTION_MESSAGE);
            }
        }
    }

    private void addElement(Scanner userInput) {
        String type = readInput(userInput, "Enter element type (Room, Clue, Player, Decoration): ");
        if (!validateType(type)) {
            System.out.println("Invalid type. Valid types: Room, Clue, Decoration, Player.");
            return;
        }

        String name = readInput(userInput, "Enter element name: ");
        BigDecimal price = parsePrice(userInput, "Enter price: ");
        if (price == null) return;

        try {
            Element element = createElementByType(type, name, price, userInput);
            if (element != null) {
                Command<T> command = new AddCommand<>(elements);
                actions.executeCommand(command, (T) element);
            }
        } catch (Exception e) {
            System.out.println("Failed to add the element: " + e.getMessage());
        }
    }

    private Element createElementByType(String type, String name, BigDecimal price, Scanner userInput) {
        return switch (type.toLowerCase()) {
            case "room" -> addRoom(name, price, userInput);
            case "clue" -> addClue(name, price, userInput);
            case "decoration" -> addDecoration(name, price, userInput);
            case "player" -> addPlayer(name, price, userInput);
            default -> null;
        };
    }

    private Element addRoom(String name, BigDecimal price, Scanner userInput) {
        String input = readInput(userInput, "Enter difficulty [1: EASY, 2: MEDIUM, 3: EXPERT]: ");
        Difficulty difficulty = switch (input) {
            case "1" -> Difficulty.EASY;
            case "2" -> Difficulty.MEDIUM;
            case "3" -> Difficulty.EXPERT;
            default -> null;
        };

        if (difficulty == null) {
            System.out.println("Invalid difficulty value. Choose from 1 to 3.");
            return null;
        }

        AbstractFactory factory = new AbstractEscapeRoom();
        return factory.createElement(name, price, "room", difficulty);
    }

    private Element addClue(String name, BigDecimal price, Scanner userInput) {
        String theme = readInput(userInput, "Enter theme: ");
        if (theme.isBlank()) {
            System.out.println(EMPTY_FIELD_MESSAGE);
            return null;
        }

        AbstractFactory factory = new AbstractEscapeRoom();
        return factory.createElement(name, price, "clue", theme);
    }

    private Element addDecoration(String name, BigDecimal price, Scanner userInput) {
        String material = readInput(userInput, "Enter material: ");
        if (material.isBlank()) {
            System.out.println(EMPTY_FIELD_MESSAGE);
            return null;
        }

        AbstractFactory factory = new AbstractEscapeRoom();
        return factory.createElement(name, price, "decoration", material);
    }

    private Element addPlayer(String name, BigDecimal price, Scanner userInput) {
        String email = readInput(userInput, "Enter email: ");
        if (email.isBlank() || !isValidEmail(email)) {
            System.out.println("Invalid email format. Operation canceled.");
            return null;
        }

        AbstractFactory factory = new AbstractEscapeRoom();
        return factory.createElement(name, price, "player", email);
    }

    private void showElements() {
        Command<T> command = new ShowCommand<>(elements);
        actions.executeCommand(command, null);
    }

    private void deleteElement(Scanner userInput) {
        System.out.print("Enter element index to remove: ");
        int index = userInput.nextInt();
        if (index >= 0 && index < elements.size()) {
            T element = elements.get(index);
            Command<T> command = new RemoveCommand<>(elements);
            actions.executeCommand(command, element);
        } else {
            System.out.println("Invalid index.");
        }
    }

    private void calculateTotalPrice() {
        Command<T> command = new CalculateCommand(elements);
        actions.executeCommand(command, null);
    }

    // Utility methods
    private String readInput(Scanner userInput, String message) {
        System.out.print(message);
        return userInput.nextLine().trim();
    }

    private BigDecimal parsePrice(Scanner userInput, String message) {
        String input = readInput(userInput, message);
        try {
            return new BigDecimal(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid price format. Operation canceled.");
            return null;
        }
    }

    private boolean validateType(String type) {
        return switch (type.toLowerCase()) {
            case "room", "clue", "decoration", "player" -> true;
            default -> false;
        };
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }
}