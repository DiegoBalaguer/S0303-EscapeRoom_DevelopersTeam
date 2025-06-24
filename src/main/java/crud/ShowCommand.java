package crud;

import interfaces.Command;
import java.util.List;


public class ShowCommand<T> implements Command<T> {
    private final List<T> list;

    public ShowCommand(List<T> list) {
        this.list = list;
    }

    @Override
    public void execute(T element) {
        System.out.println("Current list of elements:");
        list.forEach(System.out::println);
    }
}

