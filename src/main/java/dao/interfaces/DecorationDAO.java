package dao.interfaces;

import dao.exceptions.DAOException;
import model.Decoration;

import java.util.List;

public interface DecorationDAO extends BaseDAO<Decoration, Integer> {
    List<Decoration> findDecorationsByRoomId(Integer roomId) throws DAOException;

}
