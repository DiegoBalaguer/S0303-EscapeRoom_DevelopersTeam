package mvc.entities.ticket;

import dao.exceptions.DAOException;
import dao.interfaces.BaseDAO;

import java.util.List;

public interface TicketDAO extends BaseDAO<Ticket, Integer> {
    List<Ticket> findAllActiveTickets() throws DAOException;
}
