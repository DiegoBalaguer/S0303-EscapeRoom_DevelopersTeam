package mvc.entities.rewardWin;

import dao.exceptions.DAOException;
import dao.interfaces.BaseDAO;

import java.util.List;

public interface RewardWinDAO  extends BaseDAO<RewardWin, Integer> {
    List<RewardWinDisplayDTO> findByPlayerId(Integer playerId) throws DAOException;
}
