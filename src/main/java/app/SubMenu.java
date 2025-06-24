package app;

import abstractFactories.AbstractEscapeRoom;
import crud.*;
import enums.Difficulty;
import interfaces.AbstractFactory;
import interfaces.Command;
import model.Element;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SubMenu<T extends Element> {
    private final List<T> elements = new ArrayList<>();
    private final Actions<T> actions = new Actions<>();

    public void showSubMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Submenu ===");
            System.out.println("1. Add Element");
            System.out.println("2. Show Elements");
            System.out.println("3. Delete Element");
            System.out.println("4. Calculate Total Price");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> addElement(scanner);
                case 2 -> showElements();
                case 3 -> deleteElement(scanner);
                case 4 -> calculateTotalPrice();
                case 0 -> {
                    System.out.println("Returning to Main Menu...");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void addElement(Scanner scanner) {
        System.out.print("Enter element type (Room, Clue, Player, Decoration): ");
        String type = scanner.nextLine().trim();

        // Validar que el tipo no sea nulo o vacío
        if (type == null || type.isBlank()) {
            System.out.println("Element type cannot be null or empty. Operation canceled.");
            return;
        }

        System.out.print("Enter element name: ");
        String name = scanner.nextLine();

        System.out.print("Enter price: ");
        BigDecimal price;
        try {
            price = scanner.nextBigDecimal();
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Invalid price format. Operation canceled.");
            scanner.nextLine();
            return;
        }

        Object[] args;

        try {
            switch (type.toLowerCase()) {
                case "room" -> {
                    System.out.print("Enter difficulty [1: EASY, 2: MEDIUM, 3: EXPERT]: ");
                    int difficultyInput = scanner.nextInt();
                    scanner.nextLine();
                    Difficulty difficulty = switch (difficultyInput) {
                        case 1 -> Difficulty.EASY;
                        case 2 -> Difficulty.MEDIUM;
                        case 3 -> Difficulty.EXPERT;
                        default -> throw new IllegalArgumentException("Invalid difficulty value. Operation canceled.");
                    };
                    args = new Object[]{type.toLowerCase(), difficulty};

                }
                case "clue" -> {
                    System.out.print("Enter theme: ");
                    String theme = scanner.nextLine().trim();
                    if (theme.isBlank()) {
                        throw new IllegalArgumentException("Theme cannot be empty. Operation canceled.");
                    }
                    args = new Object[]{"clue", theme};
                }
                case "decoration" -> {
                    System.out.print("Enter material: ");
                    String material = scanner.nextLine().trim();
                    if (material.isBlank()) {
                        throw new IllegalArgumentException("Material cannot be empty. Operation canceled.");
                    }
                    args = new Object[]{"decoration", material};
                }
                case "player" -> {
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine().trim();
                    if (email.isBlank() || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                        throw new IllegalArgumentException("Invalid email format. Operation canceled.");
                    }
                    args = new Object[]{"player", email};
                }
                default -> throw new IllegalArgumentException("Invalid type. Valid types: Room, Clue, Decoration, Player. Operation canceled.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            return;
        }

        try {
            AbstractFactory factory = new AbstractEscapeRoom();
            Element element = factory.createElement(name, price, args);
            Command<T> command = new AddCommand<>(elements);
            actions.executeCommand(command, (T) element);
        } catch (Exception e) {
            System.out.println("Failed to add the element: " + e.getMessage());
        }
    }

    private void showElements() {
        Command<T> command = new ShowCommand<>(elements);
        actions.executeCommand(command, null);
    }

    private void deleteElement(Scanner scanner) {
        System.out.print("Enter element index to remove: ");
        int index = scanner.nextInt();

        if (index >= 0 && index < elements.size()) {
            T element = elements.get(index);
            Command<T> command = new RemoveCommand<>(elements);
            actions.executeCommand(command, element);
        } else {
            System.out.println("Invalid index.");
        }
    }

    private void calculateTotalPrice() {
        Command<T> command = new CalculateCommand<>(elements);
        actions.executeCommand(command, null);
    }
}

