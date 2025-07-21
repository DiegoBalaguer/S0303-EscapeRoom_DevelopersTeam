package mvc.entities.clue;

import dao.exceptions.DAOException;
import dao.interfaces.BaseDAO;

import java.math.BigDecimal;
import java.util.List;

public interface ClueDAO extends BaseDAO<Clue, Integer> {
    List<ClueDisplayDTO> findAllCompleteInfo() throws DAOException;
    List<ClueDisplayDTO> findCluesByRoomId(Integer roomId) throws DAOException;
    BigDecimal findPriceByRoomId(Integer roomId) throws DAOException;
}
