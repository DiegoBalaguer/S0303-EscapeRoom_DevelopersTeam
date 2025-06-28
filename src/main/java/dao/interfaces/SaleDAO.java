package dao.interfaces;

import dao.exceptions.DAOException;
import mvc.model.Player;
import mvc.model.Sale;
import mvc.model.Ticket;

import java.util.Optional;

public interface SaleDAO  extends BaseDAO<Sale, Integer> {
    Optional<Ticket> findTicketById(int idTicket) throws DAOException;
    Optional<Player> findPlayerById(int idPlayer) throws DAOException;
    int getTotalPlayers() throws DAOException;

}
