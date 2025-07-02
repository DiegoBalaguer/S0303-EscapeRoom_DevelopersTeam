package abstractEscapeRoom.controller;

import abstractEscapeRoom.concreteRooms.EgyptRoom;
import abstractEscapeRoom.concreteRooms.GangsterRoom;
import abstractEscapeRoom.concreteRooms.SpaceRoom;
import dao.exceptions.DAOException;
import dao.impl.h2.ClueDAOH2Impl;
import dao.impl.h2.DecorationDAOH2Impl;
import dao.impl.h2.RoomDAOH2Impl;
import dao.interfaces.ClueDAO;
import dao.interfaces.ConnectionDAO;
import dao.interfaces.DecorationDAO;
import dao.interfaces.RoomDAO;
import enums.Difficulty;
import enums.Theme;
import interfaces.AbstractClue;
import interfaces.AbstractDecoration;
import interfaces.AbstractFactory;
import mvc.model.Clue;
import mvc.model.Decoration;
import mvc.model.Room;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class RoomFactoryController {
    private final ConnectionDAO connectionDAO;

    public RoomFactoryController(ConnectionDAO connectionDAO) {
        this.connectionDAO = connectionDAO;
    }


    public Room createRoom(String theme, String name, BigDecimal price, Difficulty difficulty) throws DAOException {
        AbstractFactory factory = selectFactory(theme);

        ClueDAO clueDAO = new ClueDAOH2Impl(connectionDAO);
        DecorationDAO decorationDAO = new DecorationDAOH2Impl(connectionDAO);

        Clue clue = factory.createElement("clue", name, price);
        Decoration decoration = factory.createElement("decoration", name, price);

        Objects.requireNonNull(clue, "Clue cannot be null");
        Objects.requireNonNull(decoration, "Decoration cannot be null");

        clueDAO.create(clue);
        decorationDAO.create(decoration);

        List<AbstractClue> clues = List.of((AbstractClue) clue);
        List<AbstractDecoration> decorations = List.of((AbstractDecoration)decoration);

        int id = generateEscapeRoomId();
        Theme parsedTheme = Theme.valueOf(theme.toUpperCase());

        Room room = Room.builder()
                .id(id)
                .name(name)
                .description("")
                .difficulty(difficulty)
                .theme(parsedTheme)
                .price(price)
                .clues(clues)
                .decorations(decorations)
                .build();

        // Persistir Room en la base de datos
        RoomDAO roomDAO = new RoomDAOH2Impl(connectionDAO);
        return roomDAO.create(room);

    }

    private AbstractFactory selectFactory(String theme) {
        return switch (theme.toLowerCase()) {
            case "egypt" -> new EgyptRoom();
            case "space" -> new SpaceRoom();
            case "gangster" -> new GangsterRoom();
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