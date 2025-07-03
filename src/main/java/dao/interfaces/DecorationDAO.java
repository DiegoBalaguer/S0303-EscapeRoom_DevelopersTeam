package dao.interfaces;

import dao.exceptions.DAOException;
import mvc.dto.DecorationDisplayDTO;
import mvc.model.Decoration;

import java.math.BigDecimal;
import java.util.List;

public interface DecorationDAO extends BaseDAO<Decoration, Integer> {
    List<DecorationDisplayDTO> findAllCompleteInfo() throws DAOException;
    List<DecorationDisplayDTO> findDecorationsByRoomId(Integer roomId) throws DAOException;
    BigDecimal findPriceByRoomId(Integer roomId) throws DAOException;

}
