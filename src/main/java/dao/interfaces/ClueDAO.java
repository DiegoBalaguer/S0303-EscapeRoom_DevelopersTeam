package dao.interfaces;

import dao.exceptions.DAOException;
import mvc.dto.ClueDisplayDTO;
import mvc.model.Clue;

import java.math.BigDecimal;
import java.util.List;

public interface ClueDAO extends BaseDAO<Clue, Integer> {
    List<ClueDisplayDTO> findAllCluesCompleteInfo() throws DAOException;
    List<Clue> findCluesByRoomId(Integer roomId) throws DAOException;
    BigDecimal findPriceByRoomId(Integer roomId) throws DAOException;

}
