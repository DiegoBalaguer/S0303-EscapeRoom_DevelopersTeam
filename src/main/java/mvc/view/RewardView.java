package mvc.view;

import mvc.dto.RewardMapper;
import mvc.model.Reward;
import utils.StringUtils;

import java.util.List;

public class RewardView {

    private static BaseView baseView;
    private static final String NAME_OBJECT = "Reward";

    public RewardView(){
        baseView = new BaseView();
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
