package app;

import crud.*;
import enums.OptionsMenuCrud;
import interfaces.Command;
import model.Element;
import utils.ConsoleUtils;

import java.util.List;
import java.util.function.Supplier;

public class AppWorker<T> {
    private final String title; // Título del submenú
    private final List<Command<T>> commands; // Lista de comandos para CRUD
    private final T defaultElement;

    public AppWorker(String title, Supplier<T> defaultElementSupplier, List<T> dataList) {
        this.title = title;
        this.defaultElement = defaultElementSupplier.get(); // Crear el elemento predeterminado dinámicamente

        // Crear los comandos genéricos automáticamente
        this.commands = List.of(
                new AddCommand<>(dataList),
                new ShowCommand<>(dataList),
                new RemoveCommand<>(dataList),
                new UpdateCommand<>(dataList)
        );
    }

    public void display() {
        do {
            OptionsMenuCrud.viewMenuCrud(title);
            int choice = ConsoleUtils.readRequiredInt("");

            try {
                OptionsMenuCrud menuOption = OptionsMenuCrud.values()[choice - 1];
                switch (menuOption) {
                    case ADD -> executeCommand(0); // Ejecutar AddCommand
                    case SHOW -> executeCommand(1); // Ejecutar ShowCommand
                    case REMOVE -> executeCommand(2); // Ejecutar RemoveCommand
                    case UPDATE -> executeCommand(3); // Ejecutar UpdateCommand
                    case CALCULATE -> executeCommand(4); // Ejecutar CalculateCommand (si está presente)
                    case RETURN -> {
                        System.out.println("Returning to the main menu...");
                        return;
                    }
                }
            } catch (Exception e) {
                System.out.println("Invalid option or action. Please try again.");
            }
        } while (true);
    }
    private void executeCommand(int commandIndex) {
        try {
            Command<T> command = commands.get(commandIndex); // Obtener comando basado en índice
            if (command == null) {
                System.out.println("This action is not implemented yet.");
            } else {
                command.execute(defaultElement); // Usa el elemento predeterminado
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("This action is not available.");
        }
    }
}