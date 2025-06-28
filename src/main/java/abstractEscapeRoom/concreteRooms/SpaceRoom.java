package abstractEscapeRoom.concreteRooms;

import interfaces.AbstractFactory;
import mvc.model.Clue;
import mvc.model.Decoration;
import mvc.model.Element;

import java.math.BigDecimal;

public class SpaceRoom implements AbstractFactory {
    @Override
    public <T extends Element> T createElement(String type, Object... args) {
        String name = (String) args[0];
        BigDecimal price = args.length >= 2 ? (BigDecimal) args[1] : BigDecimal.ZERO;

        return switch (type.toLowerCase()) {
            case "clue" -> (T) Clue.builder()
                    .name(name)
                    .description("Space-themed clue.")
                    .price(price)
                    .build();
            case "decoration" -> (T) Decoration.builder()
                    .name(name)
                    .description("Futuristic space decoration.")
                    .price(price)
                    .build();
            default -> throw new IllegalArgumentException("Unknown element type: " + type);
        };
    }
}
