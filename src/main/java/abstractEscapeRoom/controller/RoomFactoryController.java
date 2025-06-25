package abstractEscapeRoom.controller;

import abstractEscapeRoom.concreteRooms.ConcreteEgypt;
import abstractEscapeRoom.concreteRooms.ConcreteGangster;
import abstractEscapeRoom.concreteRooms.ConcreteSpace;
import enums.Difficulty;
import enums.Theme;
import interfaces.AbstractClue;
import interfaces.AbstractDecoration;
import interfaces.AbstractFactory;
import model.Room;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class RoomFactoryController {
    public Room createRoom(String theme, String name, BigDecimal price, Difficulty difficulty) {
        AbstractFactory factory = selectFactory(theme);

        // Crear pistas y decoraciones usando la fábrica
        AbstractClue clue = factory.createElement("clue", name, price);
        AbstractDecoration decoration = factory.createElement("decoration", name, price);
        Objects.requireNonNull(clue, "Clue cannot be null");
        Objects.requireNonNull(decoration, "Decoration cannot be null");

        List<AbstractClue> clues = List.of(clue);
        List<AbstractDecoration> decorations = List.of(decoration);

        int id = generateEscapeRoomId();
        Theme parsedTheme = Theme.valueOf(theme.toUpperCase());

        Room.RoomBuilder<?, ?> elementBuilder = getElementBuilder(name);

        return new Room(
                elementBuilder,
                id,
                price,
                difficulty,
                parsedTheme,
                clues,
                decorations

        );
    }

    private AbstractFactory selectFactory(String theme) {
        return switch (theme.toLowerCase()) {
            case "egypt" -> new ConcreteEgypt();
            case "space" -> new ConcreteSpace();
            case "gangster" -> new ConcreteGangster();
            default -> throw new IllegalArgumentException("Unsupported theme: " + theme);
        };
    }

    private Room.RoomBuilder<?, ?> getElementBuilder(String name) {
        return Room.builder()
                .name(name)
                .description("Default Description");
    }



    private int generateEscapeRoomId() {
        return (int) (Math.random() * 1000);
    }
}