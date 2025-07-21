package mvc.entities.player;
import java.util.List;

import dao.interfaces.BaseDAO;

public interface PlayerDAO extends BaseDAO<Player, Integer> {
    List<Player> findSubscribedPlayers();

}


