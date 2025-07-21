package mvc.entities.player;

import dao.exceptions.DAOException;
import mvc.entities.MessageMinMax;
import utils.StringUtils;

import java.util.List;
import java.util.Optional;

public class PlayerService {

    private static PlayerService instance;
    private PlayerDAO playerDAO;
    private static final String NAME_OBJECT = "Player";

    public PlayerService(PlayerDAO playerDAO) {
        this.playerDAO = playerDAO;
    }


    public Player createPlayer(Player newPlayer) throws DAOException, IllegalArgumentException {
        if (newPlayer == null) {
            throw new IllegalArgumentException(NAME_OBJECT + " cannot be null.");
        }
        return playerDAO.create(newPlayer);
    }

    public MessageMinMax getAllPlayers() throws DAOException {
        List<Player> players = playerDAO.findAll();
        return new MessageMinMax(displayListPlayers(players), players.size() > 0 ? 1: 0, players.size());
    }

    public Optional<Player> getPlayerById(int id) throws DAOException {
        if (id <= 0) {
            throw new IllegalArgumentException(NAME_OBJECT + " ID must be positive.");
        }
        return playerDAO.findById(id);
    }

    public String getPlayerNameById(int id) throws DAOException {
        if (id <= 0) {
            throw new IllegalArgumentException(NAME_OBJECT + " ID must be positive.");
        }
        return playerDAO.findById(id).get().getName();
    }

    public Player updatePlayer(Player updatedPlayer) throws DAOException, IllegalArgumentException {
        if (updatedPlayer == null || updatedPlayer.getId() <= 0) {
            throw new IllegalArgumentException(NAME_OBJECT + " and valid ID are required for update.");
        }
        return playerDAO.update(updatedPlayer);
    }

    public void deletePlayerById(int id) throws DAOException, IllegalArgumentException {
        if (id <= 0) {
            throw new IllegalArgumentException(NAME_OBJECT + " ID must be positive for deletion.");
        }
        playerDAO.deleteById(id);
    }

    public Player softDeletePlayer(int id) throws DAOException, IllegalArgumentException {
        if (id <= 0) {
            throw new IllegalArgumentException(NAME_OBJECT + " ID must be positive for soft deletion.");
        }
        Optional<Player> existingPlayerOpt = playerDAO.findById(id);
        if (existingPlayerOpt.isEmpty()) {
            throw new IllegalArgumentException(NAME_OBJECT + " with ID " + id + " not found for soft deletion.");
        }
        Player playerToUpdate = existingPlayerOpt.get();
        playerToUpdate.setActive(false);
        return playerDAO.update(playerToUpdate);
    }

    public List<Player> getAllSubscribedPlayers() throws DAOException {
        return playerDAO.findSubscribedPlayers();
    }


    private String displayListPlayers(List<Player> players) {
        StringBuilder message = new StringBuilder();
        if (players.isEmpty()) {
            return "No " + NAME_OBJECT + "s found.";
        }
        message.append(
                StringUtils.makeLineToList(PlayerMapper.toDisplayDTO(players.getFirst()).toListHead())).append(System.lineSeparator());

        players.forEach(player -> message.append(
                StringUtils.makeLineToList(PlayerMapper.toDisplayDTO(player).toList())).append(System.lineSeparator()));

        message.append("-------------------").append(System.lineSeparator());

        return message.toString();
    }
}
