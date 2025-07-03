package mvc.view;

import mvc.dto.RewardMapper;
import mvc.model.Reward;
import utils.ConsoleUtils;
import utils.StringUtils;

import java.util.List;
import java.util.Optional;

public class RewardView {

    private static BaseView baseView =  BaseView.getInstance();
    private static final String NAME_OBJECT = "Reward";

    public Reward getRewardDetailsCreate() {
        try {
            return Reward.builder()
                    .name(getInputName())
                    .description(getInputDescription())
                    .isActive(true)
                    .build();
        } catch (Exception e) {
            baseView.displayErrorMessage("Error collecting " + NAME_OBJECT + " details: " + e.getMessage());
            return null;
        }
    }

    private String getInputName() {
        return ConsoleUtils.readRequiredString("Enter name: ");
    }

    private String getInputDescription() {
        return ConsoleUtils.readRequiredString("Enter description: ");
    }

    public Reward getUpdateRewardDetails(Reward reward) {
        try {
            reward.setName(getUpdateName(reward.getName()));
            reward.setDescription(getUpdateDescription(reward.getDescription()));
            reward.setActive(getUpdateIsActive(reward.isActive()));
            return reward;
        } catch (Exception e) {
            baseView.displayErrorMessage("Error editing " + NAME_OBJECT + ": " + e.getMessage());
            throw new IllegalArgumentException("Error editing " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private String getUpdateName(String oldValue) {
        return ConsoleUtils.readStringWithDefault("Enter name: ", Optional.of(oldValue)).get();
    }

    private String getUpdateDescription(String oldValue) {
        return ConsoleUtils.readStringWithDefault("Enter description: ", Optional.of(oldValue)).get();
    }

    private Boolean getUpdateIsActive(boolean oldValue) {
        return ConsoleUtils.readBooleanWithDefault("Enter is active ('Y' or 'N'): ", Optional.of(oldValue)).get();
    }

    public void displayRecordReward(Reward reward) {
        String message = "";
        if (reward != null) {
            message += baseView.LINE + "--- " + NAME_OBJECT + " Details ---" + baseView.LINE;
            message += "ID: " + reward.getId() + baseView.LINE;
            message += "Name: " + reward.getName() + baseView.LINE;
            message += "Description: " + reward.getDescription() + baseView.LINE;
            message += "Is Active: " + (reward.isActive() ? "Yes" : "No") + baseView.LINE;
            message += "-------------------------" + baseView.LINE;
        } else {
            message = NAME_OBJECT + " not found.";
        }
        baseView.displayMessageln(message);
    }

    public void displayListRewards(List<Reward> rewards) {
        if (rewards.isEmpty()) {
            baseView.displayMessageln("No " + NAME_OBJECT + "s found.");
            return;
        }
        baseView.displayMessageln(
                StringUtils.makeLineToList(RewardMapper.toDisplayDTO(rewards.getFirst()).toListHead()));

        rewards.forEach(reward -> baseView.displayMessageln(
                StringUtils.makeLineToList(RewardMapper.toDisplayDTO(reward).toList())));
        baseView.displayMessage2ln("-------------------");
    }
}
