package dao.interfaces;
import java.util.List;
import mvc.model.Player;

public interface PlayerDAO extends BaseDAO<Player, Integer> {
    List<Player> findSubscribedPlayers();

}


