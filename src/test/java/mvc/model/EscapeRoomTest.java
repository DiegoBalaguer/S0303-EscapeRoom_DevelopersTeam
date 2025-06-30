/*
package mvc.model;

import dao.impl.h2.ConnectionDAOH2Impl;
import dao.impl.h2.PlayerDAOH2Impl;
import dao.interfaces.PlayerDAO;
import enums.Difficulty;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EscapeRoomTest {

    private EscapeRoom escapeRoom;

    @BeforeAll
    static void setupDatabase() {
        try (Connection connection = ConnectionDAOH2Impl.getInstance().getConnection();
             Statement statement = connection.createStatement()) {
            // Inicializa la base de datos en memoria y crea la tabla "PLAYERS"
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS PLAYERS (" +
                            "ID INT AUTO_INCREMENT PRIMARY KEY, " +
                            "NAME VARCHAR(255), " +
                            "SUBSCRIBED BOOLEAN)"
            );
        } catch (Exception e) {
            fail("Failed to initialize in-memory database: " + e.getMessage());
        }
    }

    @BeforeEach
    void setUp() {
        escapeRoom = EscapeRoom.getInstance();
    }

    @AfterEach
    void cleanUp() {
        escapeRoom = null;
        cleanDatabase();
    }

    @AfterAll
    static void tearDownDatabase() {
        try {
            ConnectionDAOH2Impl.getInstance().closeConnection();
        } catch (Exception e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }

    private void cleanDatabase() {
        try (Connection connection = ConnectionDAOH2Impl.getInstance().getConnection();
             Statement statement = connection.createStatement()) {
            // Limpia los datos de la tabla "PLAYERS"
            statement.execute("TRUNCATE TABLE PLAYERS");
        } catch (Exception e) {
            System.err.println("Failed to clean up the database: " + e.getMessage());
        }
    }

    @Test
    void testSingletonInstance() {
        // Arrange & Act
        EscapeRoom firstInstance = EscapeRoom.getInstance();
        EscapeRoom secondInstance = EscapeRoom.getInstance();

        // Assert
        assertNotNull(firstInstance);
        assertNotNull(secondInstance);
        assertSame(firstInstance, secondInstance, "EscapeRoom should follow Singleton pattern");
    }

    @Test
    void testIsEmptyRoomsTrueWhenNoRoomsExist() {
        // Arrange & Act
        boolean result = escapeRoom.isEmptyRooms();

        // Assert
        assertTrue(result, "isEmptyRooms should return true when no rooms are available");
    }

    @Test
    void testIsEmptyRoomsFalseWhenRoomsExist() {
        // Arrange
        Room room = Room.builder()
                .id(1)
                .name("Test Room")
                .price(BigDecimal.TEN)
                .difficulty(Difficulty.MEDIUM)
                .build();
        escapeRoom.addRoom(room); // Uso del método público para agregar una sala

        // Act
        boolean result = escapeRoom.isEmptyRooms();

        // Assert
        assertFalse(result, "isEmptyRooms should return false when rooms are available");
    }

    @Test
    void testNotifyObserversWithSubscribedPlayers() {
        // Arrange
        PlayerDAO playerDAO = new PlayerDAOH2Impl(ConnectionDAOH2Impl.getInstance());
        try {
            playerDAO.create(new Player(1, "Player1", true));
            playerDAO.create(new Player(2, "Player2", true));
        } catch (Exception e) {
            fail("Failed to create players: " + e.getMessage());
        }

        // Act & Assert
        assertDoesNotThrow(() -> escapeRoom.notifyObservers("Test Notification"),
                "notifyObservers should not throw any exception");
    }

    @Test
    void testNotifyObserversWithNoSubscribedPlayers() {
        // Arrange
        PlayerDAO playerDAO = new PlayerDAOH2Impl(ConnectionDAOH2Impl.getInstance());
        try {
            playerDAO.deleteAll(); // Elimina todos los jugadores
        } catch (Exception e) {
            fail("Failed to clean players: " + e.getMessage());
        }

        // Act & Assert
        assertDoesNotThrow(() -> escapeRoom.notifyObservers("Test Notification with no players"),
                "notifyObservers should not throw exceptions even if there are no players");
    }

    @Test
    void testNotifyRoomCreated() {
        // Arrange
        Room room = Room.builder()
                .id(1)
                .name("Test Room")
                .price(BigDecimal.TEN)
                .difficulty(Difficulty.MEDIUM)
                .build();

        // Act & Assert
        assertDoesNotThrow(() -> escapeRoom.notifyRoomCreated(room),
                "notifyRoomCreated should not throw exceptions");
    }

    @Test
    void testNotifyItemAdded() {
        // Act & Assert
        assertDoesNotThrow(() -> escapeRoom.notifyItemAdded("item", "Test Room"),
                "notifyItemAdded should not throw exceptions");
    }
}
*/
