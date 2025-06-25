package dao.impl.h2;

import dao.exceptions.DAOException;
import dao.interfaces.BaseDAO;
import dao.interfaces.ConnectionDAO;
import dao.interfaces.TicketDAO;
import lombok.extern.slf4j.Slf4j;
import model.Ticket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class TicketDAOH2Impl implements BaseDAO<Ticket, Integer>, TicketDAO {

    private final ConnectionDAO connectionDAO;
    private static final String nameObject = "ticket";

    public TicketDAOH2Impl(ConnectionDAO connectionDAO) {
        this.connectionDAO = connectionDAO;
    }

    @Override
    public Ticket create(Ticket ticket) throws DAOException {
        String sql = "INSERT INTO " + nameObject + " (name, description, price, isActive) VALUES (?, ?, ?, ?);";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, ticket.getName());
            stmt.setString(2, ticket.getDescription());
            stmt.setBigDecimal(3, ticket.getPrice());
            stmt.setBoolean(4, ticket.isActive());
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                ticket.setId(keys.getInt(1));
            }
            return ticket;
        } catch (Exception e) {
            String messageError = "Error creating " + nameObject + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public Optional<Ticket> findById(Integer id) throws DAOException {
        String sql = "SELECT idTicket, name, price, isActive FROM " + nameObject + " WHERE idTicket = ?;";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToTicket(rs));
            }
            return Optional.empty();
        } catch (Exception e) {
            String messageError = "Error finding " + nameObject + " by ID: ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public List<Ticket> findAll() throws DAOException {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT idTicket, name, price, isActive FROM " + nameObject + ";";
        try (Connection connection = connectionDAO.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tickets.add(mapResultSetToTicket(rs));
            }
            return tickets;
        } catch (SQLException e) {
            String messageError = "Error retrieving all " + nameObject + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public Ticket update(Ticket ticket) throws DAOException {
        String sql = "UPDATE " + nameObject + " SET name = ?, description = ?, price = ?, isActive = ? WHERE idTicket = ?";
        try (Connection connection = connectionDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, ticket.getName());
            stmt.setString(2, ticket.getDescription());
            stmt.setBigDecimal(3, ticket.getPrice());
            stmt.setBoolean(4, ticket.isActive());
            stmt.setInt(5, ticket.getId());

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                String messageError = "No " + nameObject + " found to update with ID: " + ticket.getId();
                log.error(messageError);
                throw new DAOException(messageError);
            }
            return ticket;
        } catch (Exception e) {
            String messageError = "Error updating " + nameObject + ": ";
            log.error(messageError, e);
            throw new DAOException(messageError, e);
        }
    }

    @Override
    public void deleteById(Integer id) throws DAOException {
        String sql = "DELETE FROM " + nameObject + " WHERE idTicket = ?;";
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
        String sql = "SELECT 1 FROM " + nameObject + " WHERE idTicket = ?;";
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

    private Ticket mapResultSetToTicket(ResultSet rs) throws SQLException {
        return Ticket.builder()
                .id(rs.getInt("idTicket"))
                .name(rs.getString("name"))
                .price(rs.getBigDecimal("price"))
                .isActive(rs.getBoolean("isActive"))
                .build();
    }
}
