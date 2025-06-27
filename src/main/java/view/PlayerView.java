package view;

import dto.CertificateWinDisplayDTO;
import dto.PlayerDisplayDTO;
import dto.PlayerMapper;
import dto.RewardWinDisplayDTO;
import enums.OptionsMenuPlayer;
import loadConfigApp.LoadConfigApp;
import model.Player;
import lombok.extern.slf4j.Slf4j;
import model.CertificateWin;
import model.RewardWin;
import utils.ConsoleUtils;
import utils.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Slf4j
public class PlayerView {

    private final String LINE = System.lineSeparator();

    public void displayPlayerMenu(String title) {
        OptionsMenuPlayer.viewMenu(LoadConfigApp.getAppName());
    }

    public Player getPlayerDetailsCreate(Player player) {
        player.setName(
                getInputString("Enter player name: "));

        player.setEmail(
                getInputEmail("Enter player email: "));

        player.setPassword(
                getInputString("Enter player password: "));

        player.setSubscribed(
                getInputBoolean("Enter player subscribed: "));

        player.setActive(
                getInputBoolean("Enter player Active: "));

        player.setRegistrationDate(LocalDateTime.now());

        player.setActive(true);

        return player;
    }


    public Player getUpdatePlayerDetails(Player player) {
        player.setName(
                getUdateString(player.getName(), "Enter player name: "));

        player.setEmail(
                getUpdateEmail(player.getEmail(), "Enter player email: "));

        player.setPassword(
                getUdateString(player.getName(), "Enter player password: "));

        player.setSubscribed(
                getUpdateBoolean(player.isSubscribed(), "Enter player subscribed: "));

        player.setActive(
                getUpdateBoolean(player.isActive(), "Enter player Active: "));

        return player;
    }

    private String getInputString(String message) {
        return ConsoleUtils.readRequiredString(message);
    }

    private boolean getInputBoolean(String message) {
        return ConsoleUtils.readRequiredBoolean(message);
    }

    private String getInputEmail(String message) {
        String email = "";
        do {
            email = getInputString(message);
            if (!StringUtils.isValidEmail(email)) {
                displayErrorMessage("Invalid email format. Retype email.");
            }
        } while (!StringUtils.isValidEmail(email));
        return email;
    }

    private String getUdateString(String oldValue, String message) {
        return ConsoleUtils.readStringWithDefault(message, Optional.of(oldValue)).get();
    }

    private Boolean getUpdateBoolean(boolean oldValue, String message) {
        return ConsoleUtils.readBooleanWithDefault(message, Optional.of(oldValue)).get();
    }


    private String getUpdateEmail(String oldValue, String message) {
        String email = "";
        do {
            email = ConsoleUtils.readStringWithDefault(message, Optional.of(oldValue)).get();
            if (!StringUtils.isValidEmail(email)) {
                displayErrorMessage("Invalid email format. Retype email.");
            }
        } while (!StringUtils.isValidEmail(email));
        return email;
    }


    public Optional<Integer> getPlayerId() {
        try {
            int id = ConsoleUtils.readRequiredInt("Enter player ID: ");
            return Optional.of(id);
        } catch (NumberFormatException e) {
            displayErrorMessage("Invalid ID. Please enter a number.");
            return Optional.empty();
        }
    }

    public void displayPlayer(Player player) {
        if (player != null) {
            System.out.println("--- Player Details ---");
            System.out.println("ID: " + player.getId());
            System.out.println("Name: " + player.getName());
            System.out.println("Email: " + player.getEmail());
            System.out.println("Password: " + player.getPassword());
            System.out.println("Registration Date: " + player.getRegistrationDate());
            System.out.println("Subscribed: " + player.isSubscribed());
            System.out.println("Active: " + player.isActive());
            System.out.println("----------------------");
        } else {
            System.out.println("Player not found.");
        }
    }

    public void displayRewardWins(List<RewardWin> rewardWins) {
        if (rewardWins.isEmpty()) {
            System.out.println("No reward wins found for this player.");
            return;
        }
        System.out.println("\n--- Player Reward Wins ---");
        rewardWins.forEach(rw -> System.out.println(
                "ID: " + rw.getId() +
                        ", Reward ID: " + rw.getIdReward() +
                        ", Date: " + rw.getDateDelivery()
        ));
        System.out.println("--------------------------");
    }

    public void displayCertificateWins(List<CertificateWin> certificateWins) {
        if (certificateWins.isEmpty()) {
            System.out.println("No certificate wins found for this player.");
            return;
        }
        System.out.println("\n--- Player Certificate Wins ---");
        certificateWins.forEach(cw -> System.out.println(
                "ID: " + cw.getId() +
                        ", Certificate ID: " + cw.getIdCertificate() +
                        ", Room ID: " + cw.getIdRoom() +
                        ", Description: " + cw.getDescription() +
                        ", Date: " + cw.getDateDelivery()
        ));
        System.out.println("-----------------------------");
    }


    public void displayRewardWinsEnhanced(List<Map<String, Object>> rewardWinsData) {
        if (rewardWinsData.isEmpty()) {
            System.out.println("No reward wins found for this player.");
            return;
        }
        System.out.println("\n--- Player Reward Wins ---");
        rewardWinsData.forEach(data -> {
            RewardWin rw = (RewardWin) data.get("rewardWin");
            String rewardName = (String) data.getOrDefault("rewardName", "N/A");
            System.out.println(
                    "ID: " + rw.getId() +
                            ", Reward: " + rewardName + " (ID: " + rw.getIdReward() + ")" +
                            ", Date: " + rw.getDateDelivery()

            );
        });
        System.out.println("--------------------------");
    }

    public void displayCertificateWinsEnhanced(List<Map<String, Object>> certificateWinsData) {
        if (certificateWinsData.isEmpty()) {
            System.out.println("No certificate wins found for this player.");
            return;
        }
        System.out.println("\n--- Player Certificate Wins ---");
        certificateWinsData.forEach(data -> {
            CertificateWin cw = (CertificateWin) data.get("certificateWin");
            String certificateName = (String) data.getOrDefault("certificateName", "N/A");
            String roomName = (String) data.getOrDefault("roomName", "N/A");
            System.out.println(
                    "ID: " + cw.getId() +
                            ", Certificate: " + certificateName + " (ID: " + cw.getIdCertificate() + ")" +
                            ", Room: " + roomName + " (ID: " + cw.getIdRoom() + ")" +
                            ", Description: " + cw.getDescription() +
                            ", Date: " + cw.getDateDelivery()
            );
        });
        System.out.println("-----------------------------");
    }


    public void displayRewardWinDTOs(List<RewardWinDisplayDTO> rewardWinsData) {
        if (rewardWinsData.isEmpty()) {
            System.out.println("No reward wins found for this player.");
            return;
        }
        System.out.println("\n--- Player Reward Wins ---");
        System.out.println(StringUtils.makeLineToList(rewardWinsData.getFirst().toListHead()));
        rewardWinsData.forEach(dto -> System.out.println(
                StringUtils.makeLineToList(dto.toList())
        ));
        System.out.println("--------------------------");
    }

    public void displayCertificateWinDTOs(List<CertificateWinDisplayDTO> certificateWinsData) {
        if (certificateWinsData.isEmpty()) {
            System.out.println("No certificate wins found for this player.");
            return;
        }
        System.out.println("\n--- Player Certificate Wins ---");
        System.out.println(StringUtils.makeLineToList(certificateWinsData.getFirst().toListHead()));
        certificateWinsData.forEach(dto -> System.out.println(
                StringUtils.makeLineToList(dto.toList())
        ));
        System.out.println("-----------------------------");
    }
/*
    public void displayPlayers(List<Player> players) {

        System.out.println(StringUtils.makeLineToList(players.getFirst().toListHead()));
        if (players.isEmpty()) {
            System.out.println("No players found.");
            return;
        }
        players.forEach(player -> System.out.println(
                StringUtils.makeLineToList(player.toList())
        ));
        System.out.println("-------------------");
    }

*/
    public void displayPlayers(List<Player> players) {
        if (players.isEmpty()) {
            System.out.println("No players found.");
            return;
        }

        PlayerDisplayDTO firstDTO = PlayerMapper.toDisplayDTO(players.getFirst());
        System.out.println(StringUtils.makeLineToList(firstDTO.toListHead()));

        players.stream()
                .map(PlayerMapper::toDisplayDTO)
                .map(PlayerDisplayDTO::toList)
                .map(StringUtils::makeLineToList)
                .forEach(System.out::println);

        System.out.println("-------------------");
    }

    private PlayerDisplayDTO convertToDisplayDTO(Player player) {
        return PlayerDisplayDTO.builder()
                .id(player.getId())
                .name(player.getName())
                .email(player.getEmail())
                .password(player.getPassword())
                .registrationDate(player.getRegistrationDate())
                .isSubscribed(player.isSubscribed())
                .isActive(player.isActive())
                .build();
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