package mvc.entities.reward;

import dao.exceptions.DAOException;
import dao.exceptions.NotFoundException;
import mvc.entities.Inputs;
import mvc.entities.MessageMinMax;
import utils.StringUtils;

import java.util.List;
import java.util.Optional;

public class RewardService {


    private final RewardDAO REWARD_DAO;
    private static final String NAME_OBJECT = "Reward";

    public RewardService(RewardDAO rewardDAO) {
        this.REWARD_DAO = rewardDAO;
    }

    protected Reward createReward(Inputs inputsValue) throws IllegalArgumentException, DAOException, NotFoundException {

        Optional<String> name = inputsValue.getFieldAs("name", String.class);
        Optional<String> description = inputsValue.getFieldAs("description", String.class);

        if (name.isEmpty()) {
            throw new IllegalArgumentException("The " + NAME_OBJECT + " name cannot be empty.");
        }

        Reward reward = Reward.builder()
                .name(name.get())
                .description(description.get())
                .isActive(true)
                .build();
        return REWARD_DAO.create(reward);
    }

    public Optional<Reward> getRewardById(int id) throws DAOException {
        if (id <= 0) {
            throw new IllegalArgumentException(NAME_OBJECT + " ID must be positive.");
        }
        return REWARD_DAO.findById(id);
    }

    public MessageMinMax getAllRewards() throws DAOException {
        List<Reward> rewards = REWARD_DAO.findAll();
        return new MessageMinMax(displayListRewards(rewards), rewards.size() > 0 ? 1 : 0, rewards.size());
    }

    public Reward updateReward(Reward reward) throws IllegalArgumentException, NotFoundException, DAOException {
        if (reward == null || reward.getId() <= 0) {
            throw new IllegalArgumentException("The " + NAME_OBJECT + " or its ID is not valid for the update.");
        }
        if (REWARD_DAO.findById(reward.getId()).isEmpty()) {
            throw new NotFoundException(NAME_OBJECT + " with ID " + reward.getId() + " not found for update.");
        }
        return REWARD_DAO.update(reward);
    }

    public void deleteReward(int id) throws NotFoundException, DAOException {
        if (REWARD_DAO.findById(id).isEmpty()) {
            throw new NotFoundException(NAME_OBJECT + " with ID " + id + " not found for deletion.");
        }
        REWARD_DAO.deleteById(id);
    }

    public Reward softDeleteReward(int id) throws NotFoundException, DAOException {
        Optional<Reward> existingRewardOpt = REWARD_DAO.findById(id);
        if (existingRewardOpt.isEmpty()) {
            throw new NotFoundException(NAME_OBJECT + " with ID " + id + " not found for soft deletion.");
        }
        Reward rewardToSoftDelete = existingRewardOpt.get();
        rewardToSoftDelete.setActive(false);
        return REWARD_DAO.update(rewardToSoftDelete);
    }

    public String getRewardNameById(int rewardId) throws DAOException {
        return REWARD_DAO.findById(rewardId)
                .map(Reward::getName)
                .orElse("Unknown " + NAME_OBJECT);
    }

    private String displayListRewards(List<Reward> rewards) {
        StringBuilder message = new StringBuilder();
        if (rewards.isEmpty()) {
            return "No " + NAME_OBJECT + "s found.";
        }
        message.append(
                StringUtils.makeLineToList(RewardMapper.toDisplayDTO(rewards.getFirst()).toListHead())).append(System.lineSeparator());

        rewards.forEach(reward -> message.append(
                StringUtils.makeLineToList(RewardMapper.toDisplayDTO(reward).toList())).append(System.lineSeparator()));

        message.append("-------------------").append(System.lineSeparator());
        return message.toString();
    }
}
