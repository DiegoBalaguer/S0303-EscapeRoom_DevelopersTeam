package dao.interfaces;

import dao.exceptions.DAOException;
import dto.RewardWinDisplayDTO;
import model.RewardWin;

import java.util.List;

public interface RewardWinDAO  extends BaseDAO<RewardWin, Integer> {
    List<RewardWinDisplayDTO> findByPlayerId(Integer playerId) throws DAOException;
}
