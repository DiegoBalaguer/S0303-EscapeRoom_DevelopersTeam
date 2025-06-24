package crud;
import interfaces.Command;
import model.Element;
import java.math.BigDecimal;
import java.util.List;


public class CalculateCommand implements Command<Element> {
    private final List<Element> elements;

    public CalculateCommand(List<Element> elements) {
        this.elements = elements;
    }

    @Override
    public void execute(Element element) {
        BigDecimal totalValue = elements.stream()
                .filter(Element::isActive) // Solo elementos activos
                .map(Element::getValue)   // Obtener los valores
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Sumar los valores

        System.out.println("Total value of all active elements: " + totalValue);
    }
}