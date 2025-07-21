package mvc.entities.reward;

import mvc.entities.Inputs;
import mvc.view.BaseView;
import utils.ConsoleUtils;

import java.util.Optional;

public class RewardView {

    private static RewardView instance;
    private final BaseView BASE_VIEW;
    private static final String NAME_OBJECT = "Reward";


    private RewardView() {
        BASE_VIEW = BaseView.getInstance();
        BASE_VIEW.displayDebugMessage("Created Class: " + this.getClass().getName());
    }

    protected static RewardView getInstance() {
        if (instance == null) {
            synchronized (RewardView.class) {
                if (instance == null) {
                    instance = new RewardView();
                }
            }
        }
        return instance;
    }

    protected Inputs getRewardDetailsCreate() {
        return new Inputs.Builder()
                .put("name", getInputName().orElse(null))
                .put("description", getInputDescription().orElse(null))
                .build();
    }

    private Optional<String> getInputName() {
        return BASE_VIEW.getReadValueString("Enter name: ");
    }

    private Optional<String> getInputDescription() {
        return BASE_VIEW.getReadValueString("Enter description (30 chars): ");
    }

    protected Reward getUpdateRewardDetails(Reward reward) {
        reward.setName(getUpdateName(reward.getName()));
        reward.setDescription(getUpdateDescription(reward.getDescription()));
        reward.setActive(getUpdateIsActive(reward.isActive()));
        return reward;
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

    protected void displayRecordReward(Reward reward) {
        String message = "";
        if (reward != null) {
            message += BASE_VIEW.LINE + "--- " + NAME_OBJECT + " Details ---" + BASE_VIEW.LINE;
            message += "ID: " + reward.getId() + BASE_VIEW.LINE;
            message += "Name: " + reward.getName() + BASE_VIEW.LINE;
            message += "Description: " + reward.getDescription() + BASE_VIEW.LINE;
            message += "Is Active: " + (reward.isActive() ? "Yes" : "No") + BASE_VIEW.LINE;
            message += "-------------------------" + BASE_VIEW.LINE;
        } else {
            message = NAME_OBJECT + " not found.";
        }
        BASE_VIEW.displayMessageln(message);
    }
}
