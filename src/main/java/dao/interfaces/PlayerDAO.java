package dao.interfaces;

import dao.exceptions.DAOException;
import model.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerDAO extends BaseDAO<Player, Integer> {
    List<Player> findTopPlayers(int limit) throws DAOException;
    Optional<Player> findById(int id) throws DAOException;
    Optional<Player> findByName(String name) throws DAOException;
    List<Player> findAll() throws DAOException;
    Player save(Player player) throws DAOException;
    void deleteById(int id) throws DAOException;
}


