package app;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.PlayerDAO;
import dao.interfaces.SaleDAO;
import dao.factory.*;
import dto.SaleDetailsDTO;
import enums.OptionsMenuSale;
import lombok.extern.slf4j.Slf4j;
import model.Player;
import model.Sale;
import utils.ConsoleUtils;
import view.PlayerView;
import view.SaleView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
public class SaleWorkers {
    private static SaleWorkers appWorkersInstance;
    private final SaleView saleView; // Instancia de la vista para Sale
    private final SaleDAO saleDAO;
    private final PlayerDAO playerDAO;
// Instancia del DAO de Sale


    public SaleWorkers() {
        // Inicializa la vista
        this.saleView = new SaleView();

        try {
            // Obtiene el DAO de ventas desde la fábrica
            this.saleDAO = DAOFactory.getDAOFactory().getSaleDAO();

            // Obtiene el DAO de jugadores desde la fábrica
            this.playerDAO = DAOFactory.getDAOFactory().getPlayerDAO();
        } catch (DatabaseConnectionException e) {
            // Loguea el error y relanza una excepción específica
            log.error("Error al inicializar los DAOs debido a problemas de conexión con la base de datos: {}", e.getMessage(), e);
            throw new IllegalStateException("No se pudieron inicializar los DAOs.", e);
        } catch (Exception e) {
            // Controla cualquier otra excepción inesperada
            log.error("Ocurrió un error inesperado al inicializar los DAOs: {}", e.getMessage(), e);
            throw new IllegalStateException("Error inesperado al inicializar los DAOs.", e);
        }
    }


    public static SaleWorkers getInstance() {
        if (appWorkersInstance == null) {
            synchronized (SaleWorkers.class) {
                if (appWorkersInstance == null) {
                    appWorkersInstance = new SaleWorkers();
                }
            }
        }
        return appWorkersInstance;
    }

public void mainMenu() {
        do {
            saleView.displaySaleMenu("SALE MANAGEMENT"); // Usamos la vista para mostrar el menú
            int answer = ConsoleUtils.readRequiredInt("Choose an option: ");
            OptionsMenuSale selectedOption = OptionsMenuSale.getOptionByNumber(answer);

            if (selectedOption != null) {
                try {
                    switch (selectedOption) {
                        case EXIT -> {
                            saleView.displayMessage("Returning to Main Menu...");
                            return;
                        }
                        case SELL -> createSale();
                        case DELETE -> deleteSale();
                        case CALCULATE_TOTAL_BENEFITS -> calculateTotalBenefits();
                        case SHOW_ALL_TICKETS -> listAllSales();
                        case SALE_DETAILS -> getSaleDetails(saleView.getSaleId().get());
                    }
                } catch (DAOException e) {
                    saleView.displayErrorMessage("Database operation failed: " + e.getMessage());
                    log.error("DAO Error in SaleWorkers: {}", e.getMessage(), e);
                } catch (Exception e) {
                    saleView.displayErrorMessage("An unexpected error occurred: " + e.getMessage());
                    log.error("Unexpected error in SaleWorkers: {}", e.getMessage(), e);
                }
            } else {
                saleView.displayErrorMessage("Invalid option. Please choose a valid number from the menu.");
                log.warn("Error: The value {} is invalid in the sale management menu.", answer);
            }
        } while (true);
    }

    // --- Métodos CRUD que actúan como la lógica del controlador ---
    private void createSale() {
        try {
            // Obtener detalles de la nueva venta
            SaleDetailsDTO saleDetailsDTO = getSaleDetails(false); // 'false' indica creación
            if (saleDetailsDTO == null) { // Validar datos nulos
                saleView.displayErrorMessage("Sale creation canceled: invalid or missing sale details.");
                return;
            }

            // Construir la entidad Sale usando los datos del DTO
            Sale newSale = new Sale();
            newSale.setIdTicket(saleDetailsDTO.getTicketId());
            newSale.setPrice(saleDetailsDTO.getPrice());
            // Aquí necesitaríamos un método para obtener el ID del jugador a partir del nombre
            Optional<Player> playerOptional = playerDAO.findByName(saleDetailsDTO.getPlayerName());

            if (playerOptional.isEmpty()) {
                saleView.displayErrorMessage("Sale creation failed: Player not found with name: " + saleDetailsDTO.getPlayerName());
                return;
            }

            newSale.setIdPlayer(playerOptional.get().getId());

            // Insertar la nueva venta en la base de datos
            Sale savedSale = saleDAO.create(newSale);

            // Validar si se guarda correctamente
            if (savedSale == null || savedSale.getId() == 0) {
                saleView.displayErrorMessage("Sale creation failed: Unable to save sale to the database.");
                log.error("Failed to save sale: {}", newSale);
                return;
            }

            // Retroalimentar al usuario
            saleView.displayMessage("Sale created successfully: (ID: " + savedSale.getId() + ")");
        } catch (DAOException e) {
            // Manejo de errores DAO y log
            saleView.displayErrorMessage("An error occurred while creating the sale: " + e.getMessage());
            log.error("Error in createSale: ", e);
        } catch (Exception e) {
            // Manejo de cualquier otro error inesperado
            saleView.displayErrorMessage("Unexpected error during sale creation: " + e.getMessage());
            log.error("Unexpected error in createSale: ", e);
        }
    }

    private void deleteSale() throws DAOException {
        Optional<Integer> idOpt = saleView.getSaleId(); // Obtener ID de la Sale
        if (idOpt.isPresent()) {
            saleDAO.deleteById(idOpt.get());
            saleView.displayMessage("Sale with ID " + idOpt.get() + " deleted successfully (if existed).");
        }
    }

    private void calculateTotalBenefits() throws DAOException {
        // Implementar lógica para calcular beneficios totales, si aplica.
        List<Sale> sales = saleDAO.findAll();
        double totalBenefits = sales.stream()
                .mapToDouble(sale -> sale.getPrice().doubleValue())
                .sum();
        saleView.displayMessage("Total Benefits from sales: $" + totalBenefits);
    }

    private void listAllSales() throws DAOException {
        List<Sale> sales = saleDAO.findAll();
        saleView.displaySales(sales);
    }

    public SaleDetailsDTO getSaleDetails(boolean forUpdate) {
        try {
            int saleId = ConsoleUtils.readRequiredInt("Enter sale ID: ");
            if (saleId <= 0) {
                saleView.displayErrorMessage("Invalid Sale ID. Operation canceled.");
                return null;
            }

            // Obtener la venta del DAO
            Optional<Sale> saleOptional = saleDAO.findById(saleId);

            if (saleOptional.isEmpty()) {
                saleView.displayErrorMessage("Sale not found with ID: " + saleId);
                return null;
            }

            // Extraemos los datos de la venta
            Sale sale = saleOptional.get();
            int ticketId = sale.getIdTicket();
            BigDecimal price = sale.getPrice();
            int playerId = sale.getIdPlayer();

            // Consultamos el nombre del usuario en PlayerDAO
            Optional<Player> playerOptional = playerDAO.findById(playerId);
            if (playerOptional.isEmpty()) {
                saleView.displayErrorMessage("Player not found with ID: " + playerId);
                return null;
            }

            String playerName = playerOptional.get().getName();

            // Si es una operación de "actualización", podemos permitir modificar detalles como el precio
            if (forUpdate) {
                price = ConsoleUtils.readRequiredBigDecimal("Enter new sale price: ");
            }

            // Creamos y retornamos el DTO con los datos requeridos
            return new SaleDetailsDTO(ticketId, price, playerName);

        } catch (DAOException e) {
            saleView.displayErrorMessage("Error while retrieving sale details: " + e.getMessage());
            log.error("DAOException in getSaleDetails: ", e);
            return null;
        }
    }

}
