package crud;

import interfaces.Command;
import model.Element;
import model.Room;

import java.math.BigDecimal;
import java.util.List;

public class CalculateCommand<T extends Room> implements Command<T> {
    private final List<T> list;

    public CalculateCommand(List<T> list) {
        this.list = list;
    }

    @Override
    public void execute(T element) {
        if (list.isEmpty()) {
            System.out.println("No elements to calculate. Total price is: 0.00");

            return;
        }

        BigDecimal total = list.stream()
                .map(T::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        System.out.println("The total price of all elements is: " + total);
    }

}