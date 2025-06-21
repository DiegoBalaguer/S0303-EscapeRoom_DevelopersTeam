package crud;

import interfaces.Command;
import model.Element;

import java.util.List;

public class AddCommand <T extends Element, User> implements Command<T> {
    List<T> list;

    public AddCommand(List<T> list) {
        this.list = list;
    }

    @Override
    public void execute(T element) {
        System.out.println("Enter the details for the new element:");
        list.add(element);
        System.out.println("Element added successfully: " + element);

    }
}



