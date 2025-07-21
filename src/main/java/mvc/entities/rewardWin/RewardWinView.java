package mvc.entities.rewardWin;

import mvc.entities.Inputs;
import mvc.entities.MessageMinMax;
import mvc.view.BaseView;

import java.time.LocalDateTime;
import java.util.Optional;

public class RewardWinView {

    private static RewardWinView instance;
    private final BaseView BASE_VIEW;
    private static final String NAME_OBJECT = "Reward Win";

    private RewardWinView() {
        BASE_VIEW = BaseView.getInstance();
        BASE_VIEW.displayDebugMessage("Created Class: " + this.getClass().getName());
    }

    protected static RewardWinView getInstance() {
        if (instance == null) {
            synchronized (RewardWinView.class) {
                if (instance == null) {
                    instance = new RewardWinView();
                }
            }
        }
        return instance;
    }

    protected Inputs getRewardWinDetailsCreate(MessageMinMax playersList, MessageMinMax rewardsList) {

        Optional<Integer> playerId = getEntityId("List of Players", "Input Player ID: ", playersList);
        Optional<Integer> rewardId = getEntityId("List of Rewards", "Input Reward ID: ", rewardsList);
           Optional<String> description = getInputDescription();

        return new Inputs.Builder()
                .put("playerId", playerId.orElse(null))
                .put("rewardId", rewardId.orElse(null))
                .put("description", description.orElse(null))
                .put("dateDelivery", LocalDateTime.now())
                .build();
    }

    protected Optional<Integer> getEntityId(String title, String inputText, MessageMinMax messageMinMax) {
        return BASE_VIEW.getReadValueIntMinMax(title, inputText, messageMinMax);
    }

    private Optional<String> getInputDescription() {
        return BASE_VIEW.getReadValueString("Enter description (30 chars): ");
    }
}

