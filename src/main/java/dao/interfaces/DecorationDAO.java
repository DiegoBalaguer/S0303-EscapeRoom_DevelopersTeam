package dao.interfaces;

import dao.exceptions.DAOException;
import mvc.model.Decoration;

import java.math.BigDecimal;
import java.util.List;

public interface DecorationDAO extends BaseDAO<Decoration, Integer> {
    List<Decoration> findDecorationsByRoomId(Integer roomId) throws DAOException;
    BigDecimal findPriceByRoomId(Integer roomId) throws DAOException;

}
