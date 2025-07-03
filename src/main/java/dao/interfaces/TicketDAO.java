package dao.interfaces;

import dao.exceptions.DAOException;
import mvc.model.Ticket;

import java.util.List;

public interface TicketDAO extends BaseDAO<Ticket, Integer> {
    List<Ticket> findAllActiveTickets() throws DAOException;
}
