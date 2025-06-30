package mvc.view;

import mvc.dto.RewardWinDisplayDTO;
import utils.StringUtils;

import java.util.List;

public class RewardWinView {

    private static BaseView baseView;
    private static final String NAME_OBJECT = "Reward Win";

    public RewardWinView(){
        baseView = new BaseView();
    }

    public void displayListRewardWinDTO(List<RewardWinDisplayDTO> rewardWinDisplayDTOS) {
        if (rewardWinDisplayDTOS.isEmpty()) {
            baseView.displayMessageln("No " + NAME_OBJECT + "s found.");
            return;
        }
        baseView.displayMessageln(
                StringUtils.makeLineToList(rewardWinDisplayDTOS.getFirst().toListHead()));

        rewardWinDisplayDTOS.forEach(certificateWins -> baseView.displayMessageln(
                StringUtils.makeLineToList(certificateWins.toList())));
        baseView.displayMessage2ln("-------------------");
    }
}
