package dao.interfaces;

import dao.exceptions.DAOException;
import model.Player;

import java.util.List;

public interface PlayerDAO extends BaseDAO<Player, Integer> {
    List<Player> findTopPlayers(int limit) throws DAOException;
}
