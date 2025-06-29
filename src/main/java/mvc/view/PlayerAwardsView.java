package mvc.view;

import mvc.dto.*;
import mvc.enumsMenu.OptionsMenuPlayerAward;
import lombok.extern.slf4j.Slf4j;
import mvc.model.Certificate;
import mvc.model.Player;
import mvc.model.Reward;
import mvc.model.Room;
import utils.StringUtils;

import java.util.List;

@Slf4j
public class PlayerAwardsView {

    private static BaseView baseView =  new BaseView();

    public void displayPlayerMenu(String title) {
        OptionsMenuPlayerAward.viewMenu(title);
    }

    public void displayPlayers(List<Player> players) {
        if (players.isEmpty()) {
            baseView.displayMessageln("No players found.");
            return;
        }
        baseView.displayMessageln(
                StringUtils.makeLineToList(PlayerMapper.toDisplayDTO(players.getFirst()).toListHead()));

        players.forEach(player -> baseView.displayMessageln(
                StringUtils.makeLineToList(PlayerMapper.toDisplayDTO(player).toList())));
        baseView.displayMessage2ln("-------------------");
    }

    public void displayRewards(List<Reward> rewards) {
        if (rewards.isEmpty()) {
            baseView.displayMessageln("No Rewards found.");
            return;
        }
        baseView.displayMessageln(
                StringUtils.makeLineToList(RewardMapper.toDisplayDTO(rewards.getFirst()).toListHead()));

        rewards.forEach(reward -> baseView.displayMessageln(
                StringUtils.makeLineToList(RewardMapper.toDisplayDTO(reward).toList())));
        baseView.displayMessage2ln("-------------------");
    }

    public void displayRewardWin(List<RewardWinDisplayDTO> rewardWinDisplayDTOS) {
        if (rewardWinDisplayDTOS.isEmpty()) {
            baseView.displayMessageln("No Reward wins found for player.");
            return;
        }
        baseView.displayMessageln(
                StringUtils.makeLineToList(rewardWinDisplayDTOS.getFirst().toListHead()));

        rewardWinDisplayDTOS.forEach(rewardWins -> baseView.displayMessageln(
                StringUtils.makeLineToList(rewardWins.toList())));
        baseView.displayMessage2ln("-------------------");
    }

    public void displayCertificates(List<Certificate> certificates) {
        if (certificates.isEmpty()) {
            baseView.displayMessageln("No Certificate found.");
            return;
        }
        baseView.displayMessageln(
                StringUtils.makeLineToList(CertificateMapper.toDisplayDTO(certificates.getFirst()).toListHead()));

        certificates.forEach(certificate -> baseView.displayMessageln(
                StringUtils.makeLineToList(CertificateMapper.toDisplayDTO(certificate).toList())));
        baseView.displayMessage2ln("-------------------");
    }

    public void displayCertificateWin(List<CertificateWinDisplayDTO> certificateWinDisplayDTOS) {
        if (certificateWinDisplayDTOS.isEmpty()) {
            baseView.displayMessageln("No Certificate found for player.");
            return;
        }
        baseView.displayMessageln(
                StringUtils.makeLineToList(certificateWinDisplayDTOS.getFirst().toListHead()));

        certificateWinDisplayDTOS.forEach(certificateWins -> baseView.displayMessageln(
                StringUtils.makeLineToList(certificateWins.toList())));
        baseView.displayMessage2ln("-------------------");
    }



}
