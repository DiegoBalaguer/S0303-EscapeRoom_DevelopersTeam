package app;

import crud.*;
import enums.OptionsMenuCrud;
import interfaces.Command;
import model.Element;
import utils.ConsoleUtils;
import wrapperCrud.ElementListWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class AppWorker<T> {
    private final String title; // Título del submenú
    private final List<Command<T>> commands; // Lista de comandos para CRUD
    private final T defaultElement;

    public AppWorker(String title, Supplier<T> defaultElementSupplier, List<T> dataList) {
        this.title = title;
        this.defaultElement = defaultElementSupplier.get();

        ElementListWrapper<T> wrapper = new ElementListWrapper<>(dataList);
        List<Command<T>> commandList = new ArrayList<>(List.of(
                new AddCommand<>(wrapper),
                new ShowCommand<>(wrapper),
                new RemoveCommand<>(wrapper),
                new UpdateCommand<>(wrapper)
        ));

        // Solo agregar el CalculateCommand si T extiende Element
        if (Element.class.isAssignableFrom(defaultElement.getClass())) {
            if (dataList.stream().allMatch(Element.class::isInstance)) {
                commandList.add((Command<T>) new CalculateCommand((List<Element>) dataList));
            } else {
                throw new IllegalArgumentException("Data list does not contain only Element instances");
            }
        }

        this.commands = Collections.unmodifiableList(commandList); // Hacer inmutable la lista de comandos
    }

    public void display() {
        do {
            OptionsMenuCrud.viewMenuCrud(title);
            int choice = ConsoleUtils.readRequiredInt("");

            try {
                // Validar rango de opciones del menú
                if (choice < 1 || choice > commands.size()) {
                    System.out.println("Invalid option. Please try again.");
                    continue;
                }

                OptionsMenuCrud menuOption = OptionsMenuCrud.values()[choice - 1];
                switch (menuOption) {
                    case ADD -> executeCommand(0); // Ejecutar AddCommand
                    case SHOW -> executeCommand(1); // Ejecutar ShowCommand
                    case REMOVE -> executeCommand(2); // Ejecutar RemoveCommand
                    case UPDATE -> executeCommand(3); // Ejecutar UpdateCommand
                    case CALCULATE -> executeCommand(4); // Ejecutar CalculateCommand (si aplica)
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
        if (commandIndex < 0 || commandIndex >= commands.size()) {
            System.out.println("Invalid action. Command not available.");
            return;
        }
        try {
            Command<T> command = commands.get(commandIndex);
            command.execute(defaultElement); // Pasar el elemento por defecto como argumento de ejecución
        } catch (Exception e) {
            System.out.println("An error occurred during command execution: " + e.getMessage());
        }
    }
}