package crud;

import interfaces.Command;
import model.Element;

import java.util.List;

public class ShowCommand <T extends Element, User> implements Command<T> {
    List<T> list;

    public ShowCommand(List<T> list) {
        this.list = list;
    }

    @Override
    public void execute(T element) {
        System.out.println(element);

    }
}
