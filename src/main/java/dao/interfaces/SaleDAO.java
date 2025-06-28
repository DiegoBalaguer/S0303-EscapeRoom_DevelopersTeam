package dao.interfaces;

import dao.exceptions.DAOException;
import model.Player;
import model.Sale;
import model.Ticket;

import java.util.Optional;

public interface SaleDAO  extends BaseDAO<Sale, Integer> {
    Optional<Ticket> findTicketById(int idTicket) throws DAOException;
    Optional<Player> findPlayerById(int idPlayer) throws DAOException;
    int getTotalPlayers() throws DAOException;

}
