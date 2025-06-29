

package dao.impl.h2;

import dao.exceptions.DAOException;
import dao.interfaces.ConnectionDAO;
import mvc.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerDAOH2ImplTest {

    private PlayerDAOH2Impl playerDAO;
    private ConnectionDAO mockConnectionDAO;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws Exception {
        mockConnectionDAO = Mockito.mock(ConnectionDAO.class);
        mockConnection = Mockito.mock(Connection.class);
        mockStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);

        when(mockConnectionDAO.getConnection()).thenReturn(mockConnection);
        playerDAO = new PlayerDAOH2Impl(mockConnectionDAO);
    }

    @Test
    void testCreate() throws Exception {
        Player player = Player.builder()
                .name("John Doe")
                .email("johndoe@example.com")
                .password("securePassword")
                .isSubscribed(true)
                .registrationDate(LocalDateTime.now())
                .build();

        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockStatement);
        when(mockStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1); // ID generado

        Player createdPlayer = playerDAO.create(player);

        assertNotNull(createdPlayer);
        assertEquals(1, createdPlayer.getId());
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    void testFindById() throws Exception {
        int playerId = 1;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("idPlayer")).thenReturn(playerId);
        when(mockResultSet.getString("name")).thenReturn("John Doe");
        when(mockResultSet.getString("email")).thenReturn("johndoe@example.com");
        when(mockResultSet.getString("password")).thenReturn("securePassword");
        when(mockResultSet.getBoolean("isSubscribed")).thenReturn(true);
        when(mockResultSet.getTimestamp("registrationDate")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        when(mockResultSet.getBoolean("isActive")).thenReturn(true);

        Optional<Player> result = playerDAO.findById(playerId);

        assertTrue(result.isPresent());
        assertEquals(playerId, result.get().getId());
        assertEquals("John Doe", result.get().getName());
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    void testFindAll() throws Exception {
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, true, false); // Dos jugadores
        when(mockResultSet.getInt("idPlayer")).thenReturn(1, 2);
        when(mockResultSet.getString("name")).thenReturn("John Doe", "Jane Doe");
        when(mockResultSet.getString("email")).thenReturn("johndoe@example.com", "janedoe@example.com");
        when(mockResultSet.getString("password")).thenReturn("password1", "password2");
        when(mockResultSet.getBoolean("isSubscribed")).thenReturn(true, false);
        when(mockResultSet.getTimestamp("registrationDate"))
                .thenReturn(Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now()));
        when(mockResultSet.getBoolean("isActive")).thenReturn(true, false);

        List<Player> players = playerDAO.findAll();

        assertNotNull(players);
        assertEquals(2, players.size());
        verify(mockStatement, times(1)).executeQuery(anyString());
    }

    @Test
    void testUpdate() throws Exception {
        Player player = Player.builder()
                .id(1)
                .name("Updated Name")
                .email("updatedemail@example.com")
                .password("updatedPassword")
                .isSubscribed(false)
                .registrationDate(LocalDateTime.now())
                .isActive(true)
                .build();

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1); // Actualizó una fila

        Player updatedPlayer = playerDAO.update(player);

        assertNotNull(updatedPlayer);
        assertEquals("Updated Name", updatedPlayer.getName());
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    void testDeleteById() throws Exception {
        int playerId = 1;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1); // Eliminó una fila

        assertDoesNotThrow(() -> playerDAO.deleteById(playerId));
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    void testIsExistsById() throws Exception {
        int playerId = 1;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true); // Existe el usuario

        boolean exists = playerDAO.isExistsById(playerId);

        assertTrue(exists);
        verify(mockStatement, times(1)).executeQuery();
    }
}

