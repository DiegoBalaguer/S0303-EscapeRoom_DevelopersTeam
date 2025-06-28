package crud;
import interfaces.Command;
import model.Element;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public class UpdateCommand<T extends Element> implements Command<T> {
    private final List<T> list;
    private final BiConsumer<T, T> updateFunction; // Función que aplica las actualizaciones

    public UpdateCommand(List<T> list, BiConsumer<T, T> updateFunction) {
        this.list = list;
        this.updateFunction = updateFunction;
    }

    @Override
    public void execute(T element) {
        // Encuentra el elemento por ID
        Optional<T> existingElement = list.stream()
                .filter(e -> e.getId() == element.getId())
                .findFirst();

        if (existingElement.isPresent()) {
            // Aplica la función de actualización
            updateFunction.accept(existingElement.get(), element);
            System.out.println("Element updated successfully: " + existingElement.get());
        } else {
            System.out.println("Element with ID " + element.getId() + " not found.");
        }
    }

}