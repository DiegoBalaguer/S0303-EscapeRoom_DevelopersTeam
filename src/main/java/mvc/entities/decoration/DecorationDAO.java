package mvc.entities.decoration;

import dao.exceptions.DAOException;
import dao.interfaces.BaseDAO;

import java.math.BigDecimal;
import java.util.List;

public interface DecorationDAO extends BaseDAO<Decoration, Integer> {
    List<DecorationDisplayDTO> findAllCompleteInfo() throws DAOException;
    List<DecorationDisplayDTO> findDecorationsByRoomId(Integer roomId) throws DAOException;
    BigDecimal findPriceByRoomId(Integer roomId) throws DAOException;

}
