package crud;

import interfaces.Command;
import model.Element;

import java.util.List;

import interfaces.Command;
import wrapperCrud.ElementListWrapper;

public class ShowCommand<T> implements Command<T> {
    private final ElementListWrapper<T> listWrapper;

    public ShowCommand(ElementListWrapper<T> listWrapper) {
        this.listWrapper = listWrapper;
    }

    @Override
    public void execute(T element) {
        System.out.println("Showing all elements:");
        if (listWrapper.getAll().isEmpty()) {
            System.out.println("No elements found.");
        } else {
            listWrapper.getAll().forEach(System.out::println);
        }
    }
}
