package dao.impl.h2;

import dao.exceptions.DAOException;
import dao.interfaces.BaseDAO;
import dao.interfaces.ConnectionDAO;
import dao.interfaces.SaleDAO;
import lombok.extern.slf4j.Slf4j;
import model.Sale;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class SaleDAOH2Impl implements BaseDAO<Sale, Integer>, SaleDAO {

    private final ConnectionDAO connectionDAO;
    private static final String nameObject = "sale";

    public SaleDAOH2Impl(ConnectionDAO connectionDAO) {
        this.connectionDAO = connectionDAO;
    }

    @Override
    public Sale create(Sale sale) throws DAOException {
        String sql = "INSERT INTO" + nameObject + " (idTicket, idPlayer, idRoom, players, price, completion, dateSale, isActive) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
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
            String messageError = "Error creating " + nameObject + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public Optional<Sale> findById(Integer id) throws DAOException {
        String sql = "SELECT idSale, idTicket, idPlayer, idRoom, players, price, isActive FROM " + nameObject + " WHERE idSale = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToSale(rs));
            }
            return Optional.empty();
        } catch (Exception e) {
            String messageError = "Error finding " + nameObject + " by ID: ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public List<Sale> findAll() throws DAOException {
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT idSale, idTicket, idPlayer, idRoom, players, price, isActive FROM " + nameObject + ";";
        try (Connection connection = connectionDAO.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                sales.add(mapResultSetToSale(rs));
            }
            return sales;
        } catch (SQLException e) {
            String messageError = "Error retrieving all " + nameObject + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public Sale update(Sale sale) throws DAOException {
        String sql = "UPDATE " + nameObject + " SET idTicket = ?, idPlayer = ?, idRoom = ?, players = ?, price = ?, completion = ?, dateSale = ?, isActive = ? WHERE idSale = ?;";
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
                String messageError = "No " + nameObject + " found to update with ID: " + sale.getId();
                log.error(messageError);
                throw new DAOException(messageError);
            }
            return sale;
        } catch (Exception e) {
            String messageError = "Error updating " + nameObject + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public void deleteById(Integer id) throws DAOException {
        String sql = "DELETE FROM " + nameObject + " WHERE idSale = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affected = stmt.executeUpdate();
            if (affected == 0) {
                String messageError = "No " + nameObject + " found to delete with ID: " + id;
                log.error(messageError);
                throw new DAOException(messageError);
            }
        } catch (Exception e) {
            String messageError = "Error deleting " + nameObject + " by ID: " + id;
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public boolean isExistsById(Integer id) {
        String sql = "SELECT 1 FROM " + nameObject + " WHERE idSale = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            String messageError = "Error check if exist in " + nameObject + " the ID: " + id;
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
}

