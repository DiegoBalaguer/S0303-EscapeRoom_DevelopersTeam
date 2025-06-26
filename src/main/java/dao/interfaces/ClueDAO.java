package dao.interfaces;

import dao.exceptions.DAOException;
import model.Clue;

import java.util.List;

public interface ClueDAO extends BaseDAO<Clue, Integer> {
    List<Clue> findCluesByRoomId(Integer roomId) throws DAOException;

}
