package view;

import dto.*;
import enums.OptionsMenuPlayerAward;
import loadConfigApp.LoadConfigApp;
import lombok.extern.slf4j.Slf4j;
import model.*;
import utils.ConsoleUtils;
import utils.StringUtils;

import java.util.List;

@Slf4j
public class PlayerAwardsView {

    private final String LINE = System.lineSeparator();

    public void displayPlayerMenu(String title) {
        OptionsMenuPlayerAward.viewMenu(LoadConfigApp.getAppName());
    }

    public int getInputOptionMenu() {
        return ConsoleUtils.readRequiredInt("");
    }

    public int getPlayerId() {
        return ConsoleUtils.readRequiredInt("Enter player ID: ");
    }

    public int getRewardId() {
        return ConsoleUtils.readRequiredInt("Enter Reward ID: ");
    }

    public int getCertificateId() {
        return ConsoleUtils.readRequiredInt("Enter Certificate ID: ");
    }

    public int getRoomId() {
        return ConsoleUtils.readRequiredInt("Enter room ID: ");
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

    public void displayRewards(List<Reward> rewards) {
        if (rewards.isEmpty()) {
            System.out.println("No Rewards found.");
            return;
        }
        System.out.println(
                StringUtils.makeLineToList(RewardMapper.toDisplayDTO(rewards.getFirst()).toListHead()));

        rewards.forEach(reward -> System.out.println(
                StringUtils.makeLineToList(RewardMapper.toDisplayDTO(reward).toList())));
        System.out.println("-------------------");
    }

    public void displayCertificates(List<Certificate> certificates) {
        if (certificates.isEmpty()) {
            System.out.println("No Certificate found.");
            return;
        }
        System.out.println(
                StringUtils.makeLineToList(CertificateMapper.toDisplayDTO(certificates.getFirst()).toListHead()));

        certificates.forEach(certificate -> System.out.println(
                StringUtils.makeLineToList(CertificateMapper.toDisplayDTO(certificate).toList())));
        System.out.println("-------------------");
    }

    public void displayRooms(List<Room> rooms) {
        if (rooms.isEmpty()) {
            System.out.println("No room found.");
            return;
        }
        System.out.println(
                StringUtils.makeLineToList(RoomMapper.toDisplayDTO(rooms.getFirst()).toListHead()));

        rooms.forEach(room -> System.out.println(
                StringUtils.makeLineToList(RoomMapper.toDisplayDTO(room).toList())));
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
