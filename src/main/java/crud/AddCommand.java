package crud;

import interfaces.Command;
import wrapperCrud.ElementListWrapper;

public class AddCommand<T> implements Command<T> {
    private final ElementListWrapper<T> listWrapper;

    public AddCommand(ElementListWrapper<T> listWrapper) {
        this.listWrapper = listWrapper;
    }

    @Override
    public void execute(T element) {
        if (element == null) {
            System.out.println("No element provided. Cannot add.");
            return;
        }
        listWrapper.add(element);
        System.out.println("Element added successfully: " + element);
    }
}

