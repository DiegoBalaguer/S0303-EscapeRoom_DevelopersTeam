package abstractEscapeRoom;

import enums.Difficulty;
import mvc.model.Clue;
import mvc.model.Decoration;
import mvc.model.Element;
import mvc.model.Room;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AbstractEscapeRoomTest {

    private final AbstractEscapeRoom abstractEscapeRoom = new AbstractEscapeRoom();

    @Test
    void createRoom_ValidParameters_ShouldCreateRoom() {
        String name = "Mysterious Room";
        Difficulty difficulty = Difficulty.MEDIUM;
        BigDecimal price = new BigDecimal("100.50");

        Element room = abstractEscapeRoom.createElement(name, "room", difficulty, price);

        assertTrue(room instanceof Room);
        Room createdRoom = (Room) room;
        assertEquals(name, createdRoom.getName());
        assertEquals(difficulty, createdRoom.getDifficulty());
        assertEquals(price, createdRoom.getPrice());
    }

    @Test
    void createClue_ValidParameters_ShouldCreateClue() {
        String name = "Ancient Scroll";
        String theme = "Mystery";
        BigDecimal price = new BigDecimal("50.00");

        Element clue = abstractEscapeRoom.createElement(name, "clue", theme, price);

        assertTrue(clue instanceof Clue);
        Clue createdClue = (Clue) clue;
        assertEquals(name, createdClue.getName());
        assertEquals(theme, createdClue.getDescription());
        assertEquals(price, createdClue.getPrice());
    }

    @Test
    void createDecoration_ValidParameters_ShouldCreateDecoration() {
        String name = "Golden Vase";
        String material = "Gold";
        BigDecimal price = new BigDecimal("200.75");

        Element decoration = abstractEscapeRoom.createElement(name, "decoration", material, price);

        assertTrue(decoration instanceof Decoration);
        Decoration createdDecoration = (Decoration) decoration;
        assertEquals(name, createdDecoration.getName());
        assertEquals(material, createdDecoration.getDescription());
        assertEquals(price, createdDecoration.getPrice());
    }

    @Test
    void createElement_InvalidType_ShouldThrowException() {
        String name = "Unknown";
        String invalidType = "invalid";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> abstractEscapeRoom.createElement(name, invalidType));
        assertEquals("Unknown type: " + invalidType, exception.getMessage());
    }

    @Test
    void createElement_InvalidParametersForRoom_ShouldThrowException() {
        String name = "Room without Difficulty";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> abstractEscapeRoom.createElement(name, "room"));
        assertEquals("Missing or invalid difficulty for Room.", exception.getMessage());
    }

    @Test
    void createElement_InvalidParametersForClue_ShouldThrowException() {
        String name = "Clue without Theme";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> abstractEscapeRoom.createElement(name, "clue"));
        assertEquals("Missing or invalid theme for Clue.", exception.getMessage());
    }

    @Test
    void createElement_InvalidParametersForDecoration_ShouldThrowException() {
        String name = "Decoration without Material";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> abstractEscapeRoom.createElement(name, "decoration", null));
        assertEquals("Missing or invalid material for Decoration.", exception.getMessage());
    }

    @Test
    void createElement_WithoutType_ShouldThrowException() {
        String name = "No Type Defined";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> abstractEscapeRoom.createElement(name));
        assertEquals("Invalid arguments: type is required.", exception.getMessage());
    }
}