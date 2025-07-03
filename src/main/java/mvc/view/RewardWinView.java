package mvc.view;

import mvc.dto.RewardWinDisplayDTO;
import utils.StringUtils;

import java.util.List;

public class RewardWinView {

    private static BaseView baseView = BaseView.getInstance();
    private static final String NAME_OBJECT = "Reward Win";

    public void displayListRewardWinDTO(List<RewardWinDisplayDTO> rewardWinDisplayDTOS) {
        baseView.displayMessageln(
                StringUtils.makeLineToList(rewardWinDisplayDTOS.getFirst().toListHead()));

        rewardWinDisplayDTOS.forEach(certificateWins -> baseView.displayMessageln(
                StringUtils.makeLineToList(certificateWins.toList())));
        baseView.displayMessage2ln("-------------------");
    }
}
