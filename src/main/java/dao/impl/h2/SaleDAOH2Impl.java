package dao.impl.h2;

import dao.exceptions.DAOException;
import dao.interfaces.BaseDAO;
import dao.interfaces.ConnectionDAO;
import dao.interfaces.SaleDAO;
import lombok.extern.slf4j.Slf4j;
import mvc.model.Player;
import mvc.model.Sale;
import mvc.model.Ticket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class SaleDAOH2Impl implements BaseDAO<Sale, Integer>, SaleDAO {

    private final ConnectionDAO connectionDAO;
    private static final String NAME_OBJECT = "sale";

    public SaleDAOH2Impl(ConnectionDAO connectionDAO) {
        this.connectionDAO = connectionDAO;
    }

    @Override
    public Sale create(Sale sale) throws DAOException {
        String sql = "INSERT INTO " + NAME_OBJECT + " (idTicket, idPlayer, idRoom, players, price, completion, dateSale, isActive) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, sale.getIdTicket());
            stmt.setInt(2, sale.getIdPlayer());
            stmt.setInt(3, sale.getIdRoom());
            stmt.setInt(4, sale.getPlayers());
            stmt.setBigDecimal(5, sale.getPrice());
            stmt.setInt(6, sale.getCompletion());
            stmt.setDate(7, Date.valueOf(sale.getDateSale()));
            stmt.setBoolean(8, sale.isActive());
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                sale.setId(keys.getInt(1));
            }
            return sale;
        } catch (Exception e) {
            String messageError = "Error creating " + NAME_OBJECT + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public Optional<Sale> findById(Integer id) throws DAOException {
        String sql = "SELECT idSale, idTicket, idPlayer, idRoom, players, price, isActive FROM " + NAME_OBJECT + " WHERE idSale = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToSale(rs));
            }
            return Optional.empty();
        } catch (Exception e) {
            String messageError = "Error finding " + NAME_OBJECT + " by ID: ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public List<Sale> findAll() throws DAOException {
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT idSale, idTicket, idPlayer, idRoom, players, price, isActive FROM " + NAME_OBJECT + ";";
        try (Connection connection = connectionDAO.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                sales.add(mapResultSetToSale(rs));
            }
            return sales;
        } catch (SQLException e) {
            String messageError = "Error retrieving all " + NAME_OBJECT + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public Sale update(Sale sale) throws DAOException {
        String sql = "UPDATE " + NAME_OBJECT + " SET idTicket = ?, idPlayer = ?, idRoom = ?, players = ?, price = ?, completion = ?, dateSale = ?, isActive = ? WHERE idSale = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, sale.getIdTicket());
            stmt.setInt(2, sale.getIdPlayer());
            stmt.setInt(3, sale.getIdRoom());
            stmt.setInt(4, sale.getPlayers());
            stmt.setBigDecimal(5, sale.getPrice());
            stmt.setInt(6, sale.getCompletion());
            stmt.setDate(7, Date.valueOf(sale.getDateSale()));
            stmt.setBoolean(8, sale.isActive());
            stmt.setInt(9, sale.getId());
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                String messageError = "No " + NAME_OBJECT + " found to update with ID: " + sale.getId();
                log.error(messageError);
                throw new DAOException(messageError);
            }
            return sale;
        } catch (Exception e) {
            String messageError = "Error updating " + NAME_OBJECT + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public void deleteById(Integer id) throws DAOException {
        String sql = "DELETE FROM " + NAME_OBJECT + " WHERE idSale = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affected = stmt.executeUpdate();
            if (affected == 0) {
                String messageError = "No " + NAME_OBJECT + " found to delete with ID: " + id;
                log.error(messageError);
                throw new DAOException(messageError);
            }
        } catch (Exception e) {
            String messageError = "Error deleting " + NAME_OBJECT + " by ID: " + id;
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public boolean isExistsById(Integer id) {
        String sql = "SELECT 1 FROM " + NAME_OBJECT + " WHERE idSale = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            String messageError = "Error check if exist in " + NAME_OBJECT + " the ID: " + id;
            log.error(messageError, e);
            return false;
        }
    }

    private Sale mapResultSetToSale(ResultSet rs) throws SQLException {
        return Sale.builder()
                .id(rs.getInt("idSale"))
                .idTicket(rs.getInt("idTicket"))
                .idPlayer(rs.getInt("idPlayer"))
                .idRoom(rs.getInt("idRoom"))
                .players(rs.getInt("players"))
                .price(rs.getBigDecimal("price"))
                .isActive(rs.getBoolean("isActive"))
                .build();
    }
    @Override
    public Optional<Ticket> findTicketById(int idTicket) {
        String query = "SELECT * FROM ticket WHERE idTicket = ?"; // Ajusta el nombre de la tabla y columna si es necesario

        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, idTicket);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Ticket ticket = Ticket.builder().build();
                ticket.setId(rs.getInt("idTicket"));
                ticket.setName(rs.getString("name"));       // Ajusta los campos según tu modelo
                ticket.setPrice(rs.getBigDecimal("price")); // Ajusta los campos según tu modelo
                return Optional.of(ticket);
            }
        } catch (SQLException e) {
            log.error("Error al buscar Player por ID: {}", e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Player> findPlayerById(int idPlayer) throws DAOException {
        String query = "SELECT * FROM player WHERE idPlayer = ?"; // Ajusta el nombre de la tabla y columna si es necesario

        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, idPlayer);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Player player = Player.builder().build();
                player.setId(rs.getInt("idPlayer"));
                player.setName(rs.getString("name"));
                player.setEmail(rs.getString("email"));
                player.setPassword(rs.getString("password"));
                player.setSubscribed(rs.getBoolean("isSubscribed"));
                player.setActive(rs.getBoolean("isActive"));
                return Optional.of(player);
            }
                 // Ajusta los campos según tu model

        } catch (SQLException e) {
            log.error("Error al buscar Ticket por ID: {}", e.getMessage(), e);
        }
        return Optional.empty();


    }
    @Override
    public int getTotalPlayers() throws DAOException {
        String sql = "SELECT SUM(players) AS totalPlayers FROM sale;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("totalPlayers");
            }
            return 0; // Si no hay ventas, devolver 0
        } catch (SQLException e) {
            String messageError = "Error calculating players: ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

}

