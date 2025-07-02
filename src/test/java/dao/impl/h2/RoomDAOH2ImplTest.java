package dao.impl.h2;

import dao.exceptions.DAOException;
import dao.interfaces.ConnectionDAO;
import enums.Difficulty;
import enums.Theme;
import mvc.dto.InventoryDisplayDTO;
import mvc.model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomDAOH2ImplTest {

    private RoomDAOH2Impl roomDAO;
    private ConnectionDAO mockConnectionDAO;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws Exception {
        mockConnectionDAO = mock(ConnectionDAO.class);
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnectionDAO.getConnection()).thenReturn(mockConnection);
        roomDAO = new RoomDAOH2Impl(mockConnectionDAO);
    }

    @Test
    void testCreate() throws Exception {
        Room room = Room.builder()
                .idEscapeRoom(1)
                .name("Haunted Room")
                .description("A scary haunted room")
                .theme(Theme.EGYPT)
                .difficulty(Difficulty.EXPERT)
                .price(BigDecimal.valueOf(200.00))
                .active(true)
                .build();

        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockStatement);
        when(mockStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1001); // ID generado

        Room createdRoom = roomDAO.create(room);

        assertNotNull(createdRoom);
        assertEquals(1001, createdRoom.getId());
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    void testFindById() throws Exception {
        int roomId = 1001;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("idRoom")).thenReturn(roomId);
        when(mockResultSet.getInt("idEscapeRoom")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("Haunted Room");
        when(mockResultSet.getInt("idTheme")).thenReturn(2);
        when(mockResultSet.getInt("idDifficulty")).thenReturn(1);
        when(mockResultSet.getBigDecimal("price")).thenReturn(BigDecimal.valueOf(200.00));
        when(mockResultSet.getBoolean("isActive")).thenReturn(true);
        when(mockResultSet.getString("description")).thenReturn("A scary haunted room");

        Optional<Room> result = roomDAO.findById(roomId);

        assertTrue(result.isPresent());
        assertEquals(roomId, result.get().getId());
        assertEquals("Haunted Room", result.get().getName());
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    void testUpdate() throws Exception {
        Room existingRoom = Room.builder()
                .id(1001)
                .name("Old Room")
                .theme(Theme.EGYPT)
                .difficulty(Difficulty.MEDIUM)
                .price(BigDecimal.valueOf(150.00))
                .active(true)
                .description("Old description")
                .build();

        Room updatedRoom = Room.builder()
                .id(1001)
                .name("Updated Room")
                .theme(Theme.SPACE)
                .difficulty(Difficulty.EXPERT)
                .price(BigDecimal.valueOf(250.00))
                .active(false)
                .description("Updated description")
                .build();

        // Mock para findById
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("idRoom")).thenReturn(existingRoom.getId());
        when(mockResultSet.getString("name")).thenReturn(existingRoom.getName());
        when(mockResultSet.getInt("idTheme")).thenReturn(existingRoom.getTheme().ordinal());
        when(mockResultSet.getInt("idDifficulty")).thenReturn(existingRoom.getDifficulty().ordinal());
        when(mockResultSet.getBigDecimal("price")).thenReturn(existingRoom.getPrice());
        when(mockResultSet.getBoolean("isActive")).thenReturn(existingRoom.isActive());
        when(mockResultSet.getString("description")).thenReturn(existingRoom.getDescription());

        // Mock para executeUpdate
        when(mockStatement.executeUpdate()).thenReturn(1);

        Room result = roomDAO.update(updatedRoom);

        assertNotNull(result);
        assertEquals("Updated Room", result.getName());
        assertEquals(Theme.SPACE, result.getTheme());
        assertEquals(BigDecimal.valueOf(250.00), result.getPrice());
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    void testDeleteById() throws Exception {
        int roomId = 1001;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1); // Eliminó una fila

        assertDoesNotThrow(() -> roomDAO.deleteById(roomId));
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    void testIsExistsById() throws Exception {
        int roomId = 1001;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true); // Existe

        boolean exists = roomDAO.isExistsById(roomId);

        assertTrue(exists);
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    void testCalculateTotalRoomValue() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getDouble("total")).thenReturn(350.00);

        double totalValue = roomDAO.calculateTotalRoomValue();

        assertEquals(350.00, totalValue, 0.01);
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    void testFindInventory() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, true, true, false);
        when(mockResultSet.getString("inventory")).thenReturn("Item1", "Item2", "TOTAL");
        when(mockResultSet.getInt("id")).thenReturn(1, 2, 0);
        when(mockResultSet.getString("name")).thenReturn("Lamp", "Chair", "");
        when(mockResultSet.getBigDecimal("price"))
                .thenReturn(BigDecimal.valueOf(20.00), BigDecimal.valueOf(30.00), BigDecimal.valueOf(50.00));

        List<InventoryDisplayDTO> inventoryItems = roomDAO.findInventory();

        assertEquals(3, inventoryItems.size());
        assertEquals("TOTAL", inventoryItems.get(2).getInventory());
        verify(mockStatement, times(1)).executeQuery();
    }
}