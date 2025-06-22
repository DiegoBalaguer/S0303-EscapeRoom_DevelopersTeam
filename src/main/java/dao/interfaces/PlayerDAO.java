package dao.interfaces;

import dao.exceptions.DAOException;
import model.Player;

import java.util.List;

public interface PlayerDAO extends GenericDAO<Player, Integer> {
    List<Player> findByName(String name) throws DAOException;
    List<Player> findTopPlayers(int limit) throws DAOException;
}
