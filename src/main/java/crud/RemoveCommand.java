package crud;

import interfaces.Command;
import model.Element;

import java.util.List;

public class RemoveCommand<T> implements Command<T> {
    private final List<T> list;

    public RemoveCommand(List<T> list) {
        this.list = list;
    }

    @Override
    public void execute(T element) {
        if (list.remove(element)) {
            System.out.println("Element removed successfully: " + element);
        } else {
            System.out.println("Element not found: " + element);
        }
    }
}

