package app;

import model.Clue;
import model.Decoration;
import model.Player;
import model.Room;

import java.util.Scanner;

public class MainMenu {
    private final SubMenu<Room> roomSubMenu = new SubMenu<>();
    private final SubMenu<Clue> clueSubMenu = new SubMenu<>();
    private final SubMenu<Decoration> decorationSubMenu = new SubMenu<>();
    private final Scanner scanner = new Scanner(System.in);

    public void showMainMenu() {
        while (true) {
            try {
                System.out.println("\n=== Main SubMenu ===");
                System.out.println("1. Manage Rooms");
                System.out.println("2. Manage Clues");
                System.out.println("3. Manage Players"); // Nota: Player no es compatible con SubMenu debido a la restricción genérica
                System.out.println("4. Manage Decorations");
                System.out.println("0. Exit");
                System.out.print("Select an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Limpiar el buffer

                switch (choice) {
                    case 1 -> roomSubMenu.showSubMenu();
                    case 2 -> clueSubMenu.showSubMenu();
                    case 3 -> managePlayers(); // Opción separada para Player
                    case 4 -> decorationSubMenu.showSubMenu();
                    case 0 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }

    // Método separado para manejar Player, ya que no cumple con la restricción de SubMenu
    private void managePlayers() {
        System.out.println("Manage Players functionality is not implemented yet.");
    }
}