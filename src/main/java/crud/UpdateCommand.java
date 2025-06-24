package crud;

import interfaces.Command;
import model.Element;

import java.util.List;

import interfaces.Command;
import wrapperCrud.ElementListWrapper;

public class UpdateCommand<T> implements Command<T> {
    private final ElementListWrapper<T> listWrapper;

    public UpdateCommand(ElementListWrapper<T> listWrapper) {
        this.listWrapper = listWrapper;
    }

    @Override
    public void execute(T element) {
        if (element == null) {
            System.out.println("No element provided. Cannot update.");
            return;
        }

        int index = listWrapper.getAll().indexOf(element);
        if (index != -1) {
            listWrapper.remove(element);
            listWrapper.add(element); // Reemplazar con el nuevo elemento
            System.out.println("Element updated successfully: " + element);
        } else {
            System.out.println("Element not found. Cannot update.");
        }
    }
}
