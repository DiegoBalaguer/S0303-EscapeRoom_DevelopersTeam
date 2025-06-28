package app;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.CertificateWinDAO;
import dao.interfaces.PlayerDAO;
import dao.interfaces.RewardDAO;
import dao.interfaces.RewardWinDAO;
import dto.CertificateWinDisplayDTO;
import dto.RewardWinDisplayDTO;
import enums.OptionsMenuPlayer;
import lombok.extern.slf4j.Slf4j;
import model.CertificateWin;
import model.Player;
import model.Reward;
import model.RewardWin;
import utils.ConsoleUtils;
import view.PlayerView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PlayerAwardsWorkers {

    private static PlayerAwardsWorkers playerAwardsWorkers;
    private final PlayerView playerView;
    private final PlayerDAO playerDAO;
    private final RewardDAO rewardDAO;
    private final RewardWinDAO rewardWinDAO;
    private final CertificateWinDAO certificateWinDAO;
    private final String LINE = System.lineSeparator();

    private PlayerAwardsWorkers() {
        this.playerView = new PlayerView();
        try {
            this.playerDAO = DAOFactory.getDAOFactory().getPlayerDAO();
            this.rewardDAO = DAOFactory.getDAOFactory().getRewardDAO();
            this.rewardWinDAO = DAOFactory.getDAOFactory().getRewardWinDAO();
            this.certificateWinDAO = DAOFactory.getDAOFactory().getCertificateWinDAO();
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    public static PlayerAwardsWorkers getInstance() {
        if (playerAwardsWorkers == null) {
            synchronized (PlayerAwardsWorkers.class) {
                if (playerAwardsWorkers == null) {
                    playerAwardsWorkers = new PlayerAwardsWorkers();
                }
            }
        }
        log.debug("Created PlayerWorkers Singleton");
        return playerAwardsWorkers;
    }

    public void mainMenu() {
        do {
            playerView.displayPlayerMenu("PLAYER MANAGEMENT");
            playerView.displayMessage("Choose an option: ");
            int answer = ConsoleUtils.readRequiredInt("");
            OptionsMenuPlayer selectedOption = OptionsMenuPlayer.getOptionByNumber(answer);

            if (selectedOption != null) {
                try {
                    switch (selectedOption) {
                        case EXIT -> {
                            playerView.displayMessageln("Returning to Main Menu...");
                            return;
                        }
                        case CREATE -> createPlayer();
                        case LIST_ALL -> listAllPlayers();
                        case READ -> findPlayerById();
                        case UPDATE -> updatePlayer();
                        case SOFT_DELETE -> softDeletePlayer();
                        case AWARD_REWARD_WIN -> addRewardWinToPlayer();
                        case AWARD_CERTIFICATE_WIN -> addCertificateWinToPlayer();
                        case DELETE -> deletePlayer();
                        case SUBSCRIBE -> subscribePlayer();
                        case UNSUBSCRIBE -> unSubscribePlayer();
                    }
                } catch (DAOException e) {
                    playerView.displayErrorMessage("Database operation failed: " + e.getMessage());
                } catch (Exception e) {
                    playerView.displayErrorMessage("An unexpected error occurred: " + e.getMessage());
                }
            } else {
                playerView.displayErrorMessage("Invalid option. Please choose a valid number from the menu.");
            }
        } while (true);
    }

    private void createPlayer() throws DAOException {
        Player newPlayer = new Player();
        newPlayer = playerView.getPlayerDetailsCreate(new Player());
        if (newPlayer != null) {
            Player savedPlayer = playerDAO.create(newPlayer);
            playerView.displayMessageln("Player created successfully: " + savedPlayer.getName() + " (ID: " + savedPlayer.getId() + ")");
        }
    }

    private void listAllPlayers() throws DAOException {
        playerView.displayMessageln("#### LIST ALL PLAYERS  #################");
        listAllPayersIntern();
    }

    private void listAllPayersIntern() throws DAOException {
        List<Player> players = playerDAO.findAll();
        playerView.displayPlayers(players);
    }


    private void findPlayerById() throws DAOException {
        playerView.displayMessageln("#### FIND PLAYER BY ID  #################");
        listAllPayersIntern();
        Optional<Integer> idOpt = playerView.getPlayerId();
        if (idOpt.isPresent()) {
            Integer playerId = idOpt.get();
            Optional<Player> player = playerDAO.findById(playerId);
            playerView.displayPlayer(player.orElse(null));

            List<RewardWinDisplayDTO> rewardWins = rewardWinDAO.findByPlayerId(playerId);
            List<RewardWinDisplayDTO> displayRewardWinsDTOs = new ArrayList<>();
            for (RewardWinDisplayDTO rw : rewardWins) {
                String rewardName = "N/A";
                try {
                    Optional<Reward> rewardOpt = rewardDAO.findById(rw.getIdReward());
                    if (rewardOpt.isPresent()) {
                        rewardName = rewardOpt.get().getName();
                    }
                } catch (DAOException e) {
                    log.error("Error finding Reward with ID " + rw.getIdReward(), e);
                    rewardName = "Error fetching Reward Name";
                }


                displayRewardWinsDTOs.add(RewardWinDisplayDTO.builder()
                        .id(rw.getId())
                        .idReward(rw.getIdReward())
                        .idPlayer(rw.getIdPlayer())
                        .dateDelivery(rw.getDateDelivery())
                        //.isActive(rw.isActive())
                        .isActive(true)
                        .rewardName(rewardName)
                        .build());
            }
            playerView.displayRewardWinDTOs(displayRewardWinsDTOs);

            List<CertificateWinDisplayDTO> certificateWinDisplayDTOs = certificateWinDAO.findByPlayerId(playerId);
            playerView.displayCertificateWinDTOs(certificateWinDisplayDTOs);

        }
    }

    private void updatePlayer() throws DAOException {
        playerView.displayMessageln("#### UPDATE PLAYER  #################");
        listAllPayersIntern();
        Optional<Integer> idOpt = playerView.getPlayerId();
        if (idOpt.isPresent()) {
            Optional<Player> existingPlayerOpt = playerDAO.findById(idOpt.get());
            if (existingPlayerOpt.isPresent()) {
                Player existingPlayer = existingPlayerOpt.get();
                playerView.displayMessageln("Current Player Details:");
                playerView.displayPlayer(existingPlayer);

                playerView.displayMessageln("Enter new details:");
                Player updatedDetails = playerView.getUpdatePlayerDetails(existingPlayer);

                Player savedPlayer = playerDAO.update(updatedDetails);
                playerView.displayMessageln("Player updated successfully: " + savedPlayer.getName() + " (ID: " + savedPlayer.getId() + ")");
            }
        } else {
            playerView.displayMessageln("Player with ID " + idOpt.get() + " not found. Cannot update.");
        }
    }

    private void deletePlayer() throws DAOException {
        playerView.displayMessageln("#### DELETE PLAYER  #################");
        listAllPayersIntern();
        Optional<Integer> idOpt = playerView.getPlayerId();
        if (idOpt.isPresent()) {
            playerDAO.deleteById(idOpt.get());
            playerView.displayMessageln("Player with ID " + idOpt.get() + " deleted successfully (if existed).");
        }
    }

    private void softDeletePlayer() throws DAOException {
        playerView.displayMessageln("#### SOFT DELETE PLAYER  #################");
        listAllPayersIntern();
        Optional<Integer> idOpt = playerView.getPlayerId();
        if (idOpt.isPresent()) {
            Optional<Player> existingPlayerOpt = playerDAO.findById(idOpt.get());
            if (existingPlayerOpt.isPresent()) {
                Player existingPlayer = existingPlayerOpt.get();
                existingPlayer.setActive(false);
                playerDAO.update(existingPlayer);
                playerView.displayMessageln("Player soft deleted successfully: " + existingPlayer.getName() + " (ID: " + existingPlayer.getId() + ")");
            }
        } else {
            playerView.displayMessageln("Player with ID " + idOpt.get() + " not found. Cannot soft deleted.");
        }
    }

    private void subscribePlayer() throws DAOException {
        playerView.displayMessageln("#### SUBSCRIBE PLAYER  #################");
        listAllPayersIntern();
        Optional<Integer> idOpt = playerView.getPlayerId();
        if (idOpt.isPresent()) {
            Optional<Player> existingPlayerOpt = playerDAO.findById(idOpt.get());
            if (existingPlayerOpt.isPresent()) {
                Player existingPlayer = existingPlayerOpt.get();
                existingPlayer.setSubscribed(true);
                playerDAO.update(existingPlayer);
                playerView.displayMessageln("Player subscribed successfully: " + existingPlayer.getName() + " (ID: " + existingPlayer.getId() + ")");
            }
        } else {
            playerView.displayMessageln("Player with ID " + idOpt.get() + " not found. Cannot subscribed.");
        }
    }

    private void unSubscribePlayer() throws DAOException {
        playerView.displayMessageln("#### SUBSCRIBE PLAYER  #################");
        listAllPayersIntern();
        Optional<Integer> idOpt = playerView.getPlayerId();
        if (idOpt.isPresent()) {
            Optional<Player> existingPlayerOpt = playerDAO.findById(idOpt.get());
            if (existingPlayerOpt.isPresent()) {
                Player existingPlayer = existingPlayerOpt.get();
                existingPlayer.setSubscribed(false);
                playerDAO.update(existingPlayer);
                playerView.displayMessageln("Player unsubscribed successfully: " + existingPlayer.getName() + " (ID: " + existingPlayer.getId() + ")");
            }
        } else {
            playerView.displayMessageln("Player with ID " + idOpt.get() + " not found. Cannot unsubscribed.");
        }
    }

    private void addRewardWinToPlayer() throws DAOException {
        playerView.displayMessageln("#### ADD REWARD WIN TO PLAYER #################");
        listAllPayersIntern();
        Optional<Integer> playerIdOpt = playerView.getPlayerId();
        if (playerIdOpt.isEmpty()) {
            playerView.displayErrorMessage("Player ID is required.");
            return;
        }

        Integer playerId = playerIdOpt.get();
        if (playerDAO.findById(playerId).isEmpty()) {
            playerView.displayErrorMessage("Player with ID " + playerId + " not found.");
            return;
        }

        try {
            int idReward = ConsoleUtils.readRequiredInt("Enter Reward ID: ");

            RewardWin newRewardWin = RewardWin.builder()
                    .idPlayer(playerId)
                    .idReward(idReward)
                    .dateDelivery(LocalDateTime.from(LocalDate.now()))
                    .isActive(true)
                    .build();

            RewardWin savedRewardWin = rewardWinDAO.create(newRewardWin);
            playerView.displayMessageln("Reward Win added successfully for Player ID " + playerId + " (Reward Win ID: " + savedRewardWin.getId() + ")");
        } catch (NumberFormatException e) {
            playerView.displayErrorMessage("Invalid input for numeric field. Please enter a valid number.");
        }
    }

    private void addCertificateWinToPlayer() throws DAOException {
        playerView.displayMessageln("#### ADD CERTIFICATE WIN TO PLAYER #################");
        listAllPayersIntern();

        Optional<Integer> playerIdOpt = playerView.getPlayerId();
        if (playerIdOpt.isEmpty()) {
            playerView.displayErrorMessage("Player ID is required.");
            return;
        }

        Integer playerId = playerIdOpt.get();
        if (playerDAO.findById(playerId).isEmpty()) {
            playerView.displayErrorMessage("Player with ID " + playerId + " not found.");
            return;
        }

        try {
            int idCertificate = ConsoleUtils.readRequiredInt("Enter Certificate ID: ");
            int idRoom = ConsoleUtils.readRequiredInt("Enter Room ID (0 if not applicable): ");
            String description = ConsoleUtils.readRequiredString("Enter Description for Certificate: ");

            CertificateWin newCertificateWin = CertificateWin.builder()
                    .idPlayer(playerId)
                    .idCertificate(idCertificate)
                    .idRoom(idRoom)
                    .description(description)
                    .dateDelivery(LocalDateTime.from(LocalDate.now()))
                    .isActive(true)
                    .build();

            CertificateWin savedCertificateWin = certificateWinDAO.create(newCertificateWin);
            playerView.displayMessageln("Certificate Win added successfully for Player ID " + playerId + " (Certificate Win ID: " + savedCertificateWin.getId() + ")");
        } catch (NumberFormatException e) {
            playerView.displayErrorMessage("Invalid input for numeric field. Please enter a valid number.");
        }
    }

}