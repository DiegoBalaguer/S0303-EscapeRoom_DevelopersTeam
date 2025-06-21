package crud;

import interfaces.Command;
import model.Element;

import java.util.List;

public class CalculateCommand <T extends Element> implements Command<T> {
    List<T> list;

    public CalculateCommand(List<T> list) {
        this.list = list;
    }

    @Override
    public void execute(T element) {

    }
}

