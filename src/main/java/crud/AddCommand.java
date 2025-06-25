package crud;

import interfaces.Command;
import java.util.List;

public class AddCommand<T> implements Command<T> {
    private final List<T> list;

    public AddCommand(List<T> list) {
        this.list = list;
    }

    @Override
    public void execute(T element) {
        list.add(element);
        System.out.println("Element added successfully: " + element);
    }
}

