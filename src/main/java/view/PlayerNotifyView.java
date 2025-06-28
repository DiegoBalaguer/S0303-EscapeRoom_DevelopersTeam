package view;

import dto.PlayerMapper;
import enums.OptionsMenuPlayerNotify;
import loadConfigApp.LoadConfigApp;
import lombok.extern.slf4j.Slf4j;
import model.Player;
import utils.ConsoleUtils;
import utils.StringUtils;

import java.util.List;

@Slf4j
public class PlayerNotifyView {

    private final String LINE = System.lineSeparator();

    public void displayPlayerMenu(String title) {
        OptionsMenuPlayerNotify.viewMenu(LoadConfigApp.getAppName());
    }

    public int getInputOptionMenu() {
        return ConsoleUtils.readRequiredInt("");
    }

    public int getPlayerId() {
        return ConsoleUtils.readRequiredInt("Enter player ID: ");
    }

    public void displayPlayers(List<Player> players) {
        if (players.isEmpty()) {
            System.out.println("No players found.");
            return;
        }
        System.out.println(
                StringUtils.makeLineToList(PlayerMapper.toDisplayDTO(players.getFirst()).toListHead()));

        players.forEach(player -> System.out.println(
                StringUtils.makeLineToList(PlayerMapper.toDisplayDTO(player).toList())));
        System.out.println("-------------------");
    }

    public void displayMessageln(String message) {
        System.out.println(LINE + message + LINE);
    }

    public void displayMessage(String message) {
        System.out.print(message);
    }

    public void displayErrorMessage(String message) {
        System.err.println(LINE + "ERROR: " + message + LINE);
    }
}
