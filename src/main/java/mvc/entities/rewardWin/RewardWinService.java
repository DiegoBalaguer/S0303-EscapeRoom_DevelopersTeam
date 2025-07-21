package mvc.entities.rewardWin;

import dao.exceptions.DAOException;
import dao.exceptions.NotFoundException;
import mvc.entities.Inputs;
import mvc.entities.MessageMinMax;
import mvc.entities.reward.RewardService;
import mvc.entities.notification.NotificationService;
import mvc.entities.player.PlayerService;
import utils.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class RewardWinService {

    private final RewardWinDAO REWARD_WIN_DAO;
    private static final String NAME_OBJECT = "Reward Win";

    private final PlayerService PLAYER_SERVICE;
    private final RewardService REWARD_SERVICE;
    private final NotificationService NOTIFICATION_SERVICE;

    public RewardWinService(
            RewardWinDAO rewardWinDAO,
            PlayerService playerService,
            RewardService rewardService,
            NotificationService notificationService) {
        this.REWARD_WIN_DAO = rewardWinDAO;
        this.PLAYER_SERVICE = playerService;
        this.REWARD_SERVICE = rewardService;
        this.NOTIFICATION_SERVICE = notificationService;
    }

    protected RewardWin createRewardWin(Inputs inputsValue) throws IllegalArgumentException, DAOException, NotFoundException {

        Optional<Integer> playerId = inputsValue.getFieldAs("playerId", Integer.class);
        Optional<Integer> rewardId = inputsValue.getFieldAs("rewardId", Integer.class);
        Optional<String> description = inputsValue.getFieldAs("description", String.class);
        Optional<LocalDateTime> dateDelivery = inputsValue.getFieldAs("dateDelivery", LocalDateTime.class);

        if (playerId.isEmpty() || PLAYER_SERVICE.getPlayerById(playerId.get()).isEmpty()) {
            throw new NotFoundException("Player with ID " + playerId.get() + " not found.");
        }
        if (rewardId.isEmpty() || REWARD_SERVICE.getRewardById(rewardId.get()).isEmpty()) {
            throw new NotFoundException("Reward with ID " + rewardId.get() + " not found.");
        }

        RewardWin newRewardWin = RewardWin.builder()
                .idPlayer(playerId.get())
                .idReward(rewardId.get())
                .dateDelivery(dateDelivery.get())
                .description(description.get())
                .isActive(true)
                .build();

        RewardWin savedRewardWin = REWARD_WIN_DAO.create(newRewardWin);

        String playerName = PLAYER_SERVICE.getPlayerNameById(playerId.get());
        String rewardName = REWARD_SERVICE.getRewardNameById(rewardId.get());
        Optional<String> message = ("Congratulations " + playerName + "! You have won the " + NAME_OBJECT + ": " + rewardName).describeConstable();
        NOTIFICATION_SERVICE.createNotification(playerId, message);

        return savedRewardWin;
    }

    public MessageMinMax getAllRewardsWinForPlayer(int playerId) throws NotFoundException {
        List<RewardWinDisplayDTO> rewardWinList = REWARD_WIN_DAO.findByPlayerId(playerId);

        if (rewardWinList.isEmpty()) {
            throw new NotFoundException("No " + NAME_OBJECT + "s found.");
        }
        return new MessageMinMax(getDisplayListRewardsWinDTO(rewardWinList), rewardWinList.size() > 0 ? 1 : 0, rewardWinList.size());
    }

    public String getDisplayListRewardsWinDTO(List<RewardWinDisplayDTO> rewardWinDisplayDTOS) {
        StringBuilder message = new StringBuilder();
        if (rewardWinDisplayDTOS.isEmpty()) {
            return "No " + NAME_OBJECT + "s found.";
        }
        message.append(
                StringUtils.makeLineToList(rewardWinDisplayDTOS.getFirst().toListHead())).append(System.lineSeparator());

        rewardWinDisplayDTOS.forEach(rewardWins -> message.append(
                StringUtils.makeLineToList(rewardWins.toList())).append(System.lineSeparator()));
        message.append("-------------------").append(System.lineSeparator());

        return message.toString();
    }

    public Optional<RewardWin> getRewardWinById(int id) throws DAOException, IllegalArgumentException {
        if (id <= 0) {
            throw new IllegalArgumentException(NAME_OBJECT + " ID must be positive.");
        }
        return REWARD_WIN_DAO.findById(id);
    }

    protected RewardWin softDeleteRewardWin(int id) throws NotFoundException, DAOException {
        Optional<RewardWin> existingRewardWinOpt = REWARD_WIN_DAO.findById(id);
        if (existingRewardWinOpt.isEmpty()) {
            throw new NotFoundException(NAME_OBJECT + " with ID " + id + " not found for soft deletion.");
        }
        RewardWin rewardWinToSoftDelete = existingRewardWinOpt.get();
        rewardWinToSoftDelete.setActive(false);
        return REWARD_WIN_DAO.update(rewardWinToSoftDelete);
    }

    protected MessageMinMax getAllPlayers() {
        return PLAYER_SERVICE.getAllPlayers();
    }

    protected MessageMinMax getAllRewards() {
        return REWARD_SERVICE.getAllRewards();
    }

}