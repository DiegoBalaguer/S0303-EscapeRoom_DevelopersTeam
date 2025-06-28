package abstractEscapeRoom;

import enums.Difficulty;
import interfaces.AbstractFactory;
import mvc.model.Clue;
import mvc.model.Decoration;
import mvc.model.Element;
import mvc.model.Room;

import java.math.BigDecimal;

public class AbstractEscapeRoom implements AbstractFactory {

    public Element createElement(String name, Object... args) {
        if (args.length < 1 || !(args[0] instanceof String type)) {
            throw new IllegalArgumentException("Invalid arguments: type is required.");
        }

        String typeLower = type.toLowerCase();
        BigDecimal price = extractPrice(args);

        return switch (typeLower) {
            case "room" -> createRoom(name, price, args);
            case "clue" -> createClue(name, price, args);
            case "decoration" -> createDecoration(name, price, args);
            default -> throw new IllegalArgumentException("Unknown type: " + typeLower);
        };
    }


    private BigDecimal extractPrice(Object... args) {
        return args.length >= 3 && args[2] instanceof BigDecimal ? (BigDecimal) args[2] : BigDecimal.ZERO;
    }

    private Element createRoom(String name, BigDecimal price, Object... args) {
        if (args.length < 2 || !(args[1] instanceof Difficulty difficulty)) {
            throw new IllegalArgumentException("Missing or invalid difficulty for Room.");
        }
        return Room.builder()
                .name(name)
                .price(price)
                .difficulty(difficulty)
                .build();

    }

    private Element createClue(String name, BigDecimal price, Object... args) {
        if (args.length < 2 || !(args[1] instanceof String theme)) {
            throw new IllegalArgumentException("Missing or invalid theme for Clue.");
        }
        return Clue.builder()
                .name(name)
                .price(price)
                .description(theme)
                .build();
    }

    private Element createDecoration(String name, BigDecimal price, Object... args) {
        if (args.length < 2 || !(args[1] instanceof String material)) {
            throw new IllegalArgumentException("Missing or invalid material for Decoration.");
        }
        return Decoration.builder()
                .name(name)
                .price(price)
                .description(material)
                .build();
    }


}