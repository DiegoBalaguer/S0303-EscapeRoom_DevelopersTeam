package mvc.view;

import mvc.dto.PlayerMapper;
import mvc.enumsMenu.OptionsMenuPlayerNotify;
import config.loadConfigApp.LoadConfigApp;
import lombok.extern.slf4j.Slf4j;
import mvc.model.Player;
import utils.ConsoleUtils;
import utils.StringUtils;

import java.util.List;

@Slf4j
public class PlayerNotifyView {

    private static BaseView baseView =  new BaseView();

    public void displayPlayerMenu(String title) {
        OptionsMenuPlayerNotify.viewMenu(LoadConfigApp.getAppName());
    }

    public int getPlayerId() {
        return ConsoleUtils.readRequiredInt("Enter player ID: ");
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
}
