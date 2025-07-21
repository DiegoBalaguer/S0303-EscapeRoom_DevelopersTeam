package abstractEscapeRoom.concreteRooms;

import interfaces.AbstractFactory;
import mvc.entities.clue.Clue;
import mvc.entities.decoration.Decoration;
import mvc.model.Element;

import java.math.BigDecimal;

public class EgyptRoom implements AbstractFactory {
    @Override
    public <T extends Element> T createElement(String type, Object... args) {
        String name = (String) args[0];
        BigDecimal price = args.length >= 2 ? (BigDecimal) args[1] : BigDecimal.ZERO;

        return switch (type.toLowerCase()) {
            case "mvc/entities/clue" -> (T) Clue.builder()
                    .name(name)
                    .description("Ancient Egyptian-themed clue.")
                    .build();
            case "mvc/entities/decoration" -> (T) Decoration.builder()
                    .name(name)
                    .description("Gold-plated Egyptian material.")
                    .build();
            default -> throw new IllegalArgumentException("Unknown element type: " + type);
        };
    }


}