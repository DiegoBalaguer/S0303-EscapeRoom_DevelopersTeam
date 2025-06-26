package dao.interfaces;

import dao.exceptions.DAOException;
import model.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerDAO extends BaseDAO<Player, Integer> {
    Optional<Player> findById(int id) throws DAOException;
    List<Player> findAll() throws DAOException;
    void deleteById(int id) throws DAOException;
}


