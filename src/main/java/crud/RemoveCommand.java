package crud;

import interfaces.Command;
import model.Element;

import java.util.List;

public class RemoveCommand <T extends Element, User> implements Command<T> {
    List<T> list;

    public RemoveCommand(List<T> list) {
        this.list = list;
    }

    @Override
    public void execute(T element) {
        list.remove(element);
        System.out.println("Element removed successfully: " + element);

    }
}

