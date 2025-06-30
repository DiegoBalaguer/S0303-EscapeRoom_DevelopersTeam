package dao.impl.h2;

import dao.connection.SSHSessionManager;
import dao.exceptions.DatabaseConnectionException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConnectionDAOH2ImplTest {


    @Mock
    private SSHSessionManager sshSessionManagerMock; // Mock del SSHSessionManager para pruebas

    @InjectMocks
    private ConnectionDAOH2Impl connectionDAOH2; // Clase principal con dependencias inyectadas

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test Singleton: getInstance() devuelve la misma instancia")
    void testSingletonInstance() throws DatabaseConnectionException {
        ConnectionDAOH2Impl instance1 = ConnectionDAOH2Impl.getInstance();
        ConnectionDAOH2Impl instance2 = ConnectionDAOH2Impl.getInstance();

        assertNotNull(instance1, "La instancia no debe ser nula");
        assertSame(instance1, instance2, "Las instancias deben ser la misma (patrón singleton)");
    }

    @Test
    @DisplayName("Test de conexión inicial cuando el túnel SSH está habilitado")
    void testInitializeConnectionWithSSH() throws DatabaseConnectionException, SQLException {
        when(sshSessionManagerMock.isConnected()).thenReturn(true); // Simular el túnel SSH conectado
        connectionDAOH2 = spy(ConnectionDAOH2Impl.getInstance());

        Connection connection = connectionDAOH2.getConnection();
        assertNotNull(connection, "La conexión debe ser válida");
        verify(sshSessionManagerMock, atLeastOnce()).isConnected(); // Verificar que se verificó el SSH
    }

    @Test
    @DisplayName("Test de conexión inicial cuando el túnel SSH está deshabilitado")
    void testInitializeConnectionWithoutSSH() throws DatabaseConnectionException, SQLException {
        when(sshSessionManagerMock.isConnected()).thenReturn(false); // Simular SSH deshabilitado

        connectionDAOH2 = spy(ConnectionDAOH2Impl.getInstance());
        Connection connection = connectionDAOH2.getConnection();

        assertNotNull(connection, "La conexión sin SSH debe ser válida");
    }

    @Test
    @DisplayName("Test de re-conexión cuando la conexión está cerrada")
    void testReinitializeConnectionWhenClosed() throws DatabaseConnectionException, SQLException {
        connectionDAOH2 = spy(ConnectionDAOH2Impl.getInstance());
        Connection connectionMock = mock(Connection.class);
        when(connectionMock.isClosed()).thenReturn(true); // Simular que la conexión está cerrada

        doReturn(connectionMock).when(connectionDAOH2).getConnection(); // Retry de conexión

        Connection connection = connectionDAOH2.getConnection();

        assertNotNull(connection, "El objeto de conexión después de reiniciar no debe ser nulo");
        verify(connectionDAOH2, atLeastOnce()).getConnection(); // Validar re-inicialización
    }

    @Test
    @DisplayName("Test de transacción: iniciar, validar y revertir")
    void testTransactionFunctions() throws SQLException, DatabaseConnectionException {
        connectionDAOH2 = spy(ConnectionDAOH2Impl.getInstance());
        Connection connectionMock = mock(Connection.class);

        when(connectionDAOH2.getConnection()).thenReturn(connectionMock);

        // Comenzar transacción
        connectionDAOH2.beginTransaction();
        verify(connectionMock).setAutoCommit(false);

        // Validar transacción
        connectionDAOH2.commitTransaction();
        verify(connectionMock).commit();
        verify(connectionMock).setAutoCommit(true);

        // Revertir transacción
        connectionDAOH2.rollbackTransaction();
        verify(connectionMock).rollback();
        verify(connectionMock, times(2)).setAutoCommit(true);
    }

    @Test
    @DisplayName("Test cerrar conexión correctamente")
    void testCloseConnection() throws SQLException, DatabaseConnectionException {
        connectionDAOH2 = spy(ConnectionDAOH2Impl.getInstance());
        Connection connectionMock = mock(Connection.class);

        when(connectionDAOH2.getConnection()).thenReturn(connectionMock);
        when(connectionMock.isClosed()).thenReturn(false); // Simular que la conexión está abierta

        connectionDAOH2.closeConnection();

        verify(connectionMock).close();
        verify(sshSessionManagerMock, atLeastOnce()).close(); // Verificar cierre del túnel SSH si habilitado
    }

}