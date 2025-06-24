package crud;

import interfaces.Command;
import model.Element;

import java.util.List;
import interfaces.Command;
import wrapperCrud.ElementListWrapper;

public class RemoveCommand<T> implements Command<T> {
    private final ElementListWrapper<T> listWrapper;

    public RemoveCommand(ElementListWrapper<T> listWrapper) {
        this.listWrapper = listWrapper;
    }

    @Override
    public void execute(T element) {
        if (element == null) {
            System.out.println("No element provided. Cannot remove.");
            return;
        }
        if (listWrapper.getAll().contains(element)) {
            listWrapper.remove(element);
            System.out.println("Element removed successfully: " + element);
        } else {
            System.out.println("Element not found. Cannot remove.");
        }
    }
}

