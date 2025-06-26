package app;

import dao.exceptions.DAOException; // Importar para manejar excepciones del DAO
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory; // Importar para obtener el PlayerDAO
import dao.interfaces.PlayerDAO; // Importar la interfaz DAO
import enums.OptionsMenuPlayer; // Tu enum para el menú de jugadores
import loadConfigApp.LoadConfigApp;
import lombok.extern.slf4j.Slf4j;
import model.Player; // Importar la clase Player
import utils.ConsoleUtils; // Para leer inputs
import view.PlayerView; // Importar la vista de jugadores

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PlayerWorkers {

    private static PlayerWorkers appWorkersInstance;
    private final PlayerView playerView; // Instancia de la vista
    private final PlayerDAO playerDAO;   // Instancia del DAO de jugadores

    private PlayerWorkers() {
        this.playerView = new PlayerView(); // Inicializa la vista
        try {
            this.playerDAO = DAOFactory.getDAOFactory().getPlayerDAO(); // Obtiene el DAO de la fábrica
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    public static PlayerWorkers getInstance() {
        if (appWorkersInstance == null) {
            synchronized (PlayerWorkers.class) {
                if (appWorkersInstance == null) {
                    appWorkersInstance = new PlayerWorkers();
                }
            }
        }
        log.debug("Created PlayerWorkers Singleton"); // Corregido el log
        return appWorkersInstance;
    }

    public void mainMenu() {
        do {
            playerView.displayPlayerMenu("PLAYER MANAGEMENT"); // Usamos la vista para mostrar el menú
            int answer = ConsoleUtils.readRequiredInt("Choose an option: ");
            OptionsMenuPlayer selectedOption = OptionsMenuPlayer.getOptionByNumber(answer);

            if (selectedOption != null) {
                try {
                    switch (selectedOption) {
                        case EXIT -> {
                            playerView.displayMessage("Returning to Main Menu...");
                            return;
                        }
                        case CREATE -> createPlayer();
                        case LIST_ALL -> listAllPlayers();
                        case READ -> findPlayerById();
                        case UPDATE -> updatePlayer();
                        case DELETE -> deletePlayer();

                        // No se necesita `default` si `getOptionByNumber` ya devuelve `null` para inválidos
                    }
                } catch (DAOException e) {
                    playerView.displayErrorMessage("Database operation failed: " + e.getMessage());
                    log.error("DAO Error in PlayerWorkers: {}", e.getMessage(), e);
                } catch (Exception e) {
                    playerView.displayErrorMessage("An unexpected error occurred: " + e.getMessage());
                    log.error("Unexpected error in PlayerWorkers: {}", e.getMessage(), e);
                }
            } else {
                playerView.displayErrorMessage("Invalid option. Please choose a valid number from the menu.");
                log.warn("Error: The value {} is wrong in player management menu.", answer);
            }
        } while (true);
    }

    // --- Métodos CRUD que actúan como la lógica del controlador ---
    private void createPlayer() throws DAOException {
        Player newPlayer = playerView.getPlayerDetails(false); // false indica que es un nuevo jugador
        if (newPlayer != null) {
            Player savedPlayer = playerDAO.create(newPlayer);
            playerView.displayMessage("Player created successfully: " + savedPlayer.getName() + " (ID: " + savedPlayer.getId() + ")");
        }
    }

    private void listAllPlayers() throws DAOException {
        List<Player> players = playerDAO.findAll();
        playerView.displayPlayers(players);
    }

    private void findPlayerById() throws DAOException {
        Optional<Integer> idOpt = playerView.getPlayerId();
        if (idOpt.isPresent()) {
            Optional<Player> player = playerDAO.findById(idOpt.get());
            playerView.displayPlayer(player.orElse(null)); // Pasa null si no se encontró
        }
    }

    private void updatePlayer() throws DAOException {
        Optional<Integer> idOpt = playerView.getPlayerId();
        if (idOpt.isPresent()) {
            Optional<Player> existingPlayerOpt = playerDAO.findById(idOpt.get());
            if (existingPlayerOpt.isPresent()) {
                Player existingPlayer = existingPlayerOpt.get();
                playerView.displayMessage("Current Player Details:");
                playerView.displayPlayer(existingPlayer);

                playerView.displayMessage("Enter new details:");
                Player updatedDetails = playerView.getPlayerDetails(true); // true indica que es para actualización

                if (updatedDetails != null) {
                    // Actualiza solo los campos que quieras permitir actualizar desde el input del usuario
                    existingPlayer.setName(updatedDetails.getName());
                    existingPlayer.setEmail(updatedDetails.getEmail());
                    // La fecha de registro normalmente no se actualiza, se mantiene la original.

                    Player updatedPlayer = playerDAO.update(existingPlayer);
                    playerView.displayMessage("Player updated successfully: " + updatedPlayer.getName() + " (ID: " + updatedPlayer.getId() + ")");
                }
            } else {
                playerView.displayMessage("Player with ID " + idOpt.get() + " not found. Cannot update.");
            }
        }
    }

    private void deletePlayer() throws DAOException {
        Optional<Integer> idOpt = playerView.getPlayerId();
        if (idOpt.isPresent()) {
            playerDAO.deleteById(idOpt.get());
            playerView.displayMessage("Player with ID " + idOpt.get() + " deleted successfully (if existed).");
        }
    }
}
