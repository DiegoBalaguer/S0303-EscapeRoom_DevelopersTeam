package mvc.controller;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.PlayerDAO;
import dao.interfaces.TicketDAO;
import dao.interfaces.SaleDAO;
import mvc.dto.SaleDetailsDTO;
import mvc.enumsMenu.OptionsMenuSale;
import lombok.extern.slf4j.Slf4j;
import mvc.model.Player;
import mvc.model.Sale;
import mvc.model.Ticket;
import mvc.view.BaseView;
import utils.ConsoleUtils;
import mvc.view.SaleView;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
public class SaleController {
    private static volatile SaleController saleControllerInstance;
    private final SaleView saleView;
    private final SaleDAO saleDAO;
    private final TicketDAO ticketDAO;
    private final PlayerDAO playerDAO;

    private static BaseView baseView;

    private SaleController() {
        baseView = new BaseView();
        this.saleView = new SaleView();

        try {
            // Inicializamos los DAOs desde la fábrica
            this.saleDAO = DAOFactory.getDAOFactory().getSaleDAO();
            this.playerDAO = DAOFactory.getDAOFactory().getPlayerDAO();
            this.ticketDAO = DAOFactory.getDAOFactory().getTicketDAO(); // Inicialización del ticketDAO
        } catch (DatabaseConnectionException e) {
            log.error("Error al inicializar los DAOs debido a problemas de conexión con la base de datos: {}", e.getMessage(), e);
            throw new IllegalStateException("No se pudieron inicializar los DAOs.", e);
        } catch (Exception e) {
            log.error("Ocurrió un error inesperado al inicializar los DAOs: {}", e.getMessage(), e);
            throw new IllegalStateException("Error inesperado al inicializar los DAOs.", e);
        }

        // Validación de DAOs para detectar problemas desde el inicio
        validateNotNull(this.saleDAO, "SaleDAO no pudo ser inicializado.");
        validateNotNull(this.playerDAO, "PlayerDAO no pudo ser inicializado.");
        validateNotNull(this.ticketDAO, "TicketDAO no pudo ser inicializado."); // Validamos que el ticketDAO no sea nulo
    }

    // Método helper para validar nulos
    private void validateNotNull(Object obj, String errorMsg) {
        if (obj == null) {
            log.error(errorMsg);
            throw new IllegalStateException(errorMsg);
        }
    }

    // Patrón Singleton (thread-safe)
    public static SaleController getInstance() {
        if (saleControllerInstance == null) {
            synchronized (SaleController.class) {
                if (saleControllerInstance == null) {
                    saleControllerInstance = new SaleController();
                }
            }
        }
        return saleControllerInstance;
    }

    // Menú principal
    public void mainMenu() {
        boolean running = true;
        while (running) {
            baseView.displayMessageln(OptionsMenuSale.viewMenu("SALE MANAGEMENT"));
            int answer = baseView.getReadRequiredInt("Choose an option: ");
            OptionsMenuSale selectedOption = OptionsMenuSale.getOptionByNumber(answer);

            if (selectedOption != null) {
                try {
                    switch (selectedOption) {
                        case EXIT -> {
                            baseView.displayMessageln("Returning to Main Menu...");
                            running = false; // Salimos del bucle
                        }
                        case SELL -> createSale();
                        case DELETE -> deleteSale();
                        case CALCULATE_TOTAL_BENEFITS -> calculateTotalBenefits();
                        case SHOW_ALL_TICKETS -> listAllSales();

                        default -> baseView.displayErrorMessage("Unknown option selected.");
                    }
                } catch (DAOException e) {
                    handleError("Database operation failed: ", e);
                } catch (Exception e) {
                    handleError("An unexpected error occurred: ", e);
                }
            } else {
                baseView.displayErrorMessage("Invalid option. Please choose a valid number from the menu.");
                log.warn("Error: The value {} is invalid in the sale management menu.", answer);
            }
        }
    }
    /*private void createSale() {
        try {
            // Solicitar el nombre del ticket
            String ticketName = ConsoleUtils.readRequiredString("Enter the Ticket Name you want to purchase: ");
            Optional<Ticket> ticketOptional = ticketDAO.findByName(ticketName);

            if (ticketOptional.isEmpty()) {
                baseView.displayErrorMessage("Sale failed: Ticket with name '" + ticketName + "' does not exist.");
                return;
            }

            // Solicitar el email del jugador
            String playerEmail = ConsoleUtils.readRequiredString("Enter the Player Email: ");
            Optional<Player> playerOptional = playerDAO.findByEmail(playerEmail);

            if (playerOptional.isEmpty()) {
                baseView.displayErrorMessage("Sale failed: Player with email '" + playerEmail + "' does not exist.");
                return;
            }

            // Solicitar el número de jugadores
            int playersCount = ConsoleUtils.readRequiredInt("Enter the number of players: ");
            if (playersCount <= 0) {
                baseView.displayErrorMessage("Sale failed: The number of players must be greater than 0.");
                return;
            }

            // Calcular el precio total
            Ticket ticket = ticketOptional.get();
            BigDecimal totalPrice = ticket.getPrice().multiply(BigDecimal.valueOf(playersCount));

            // Crear el objeto Sale
            int roomId = 1; // En este caso, asumimos que será una sala con ID predeterminada
            Sale newSale = Sale.builder()
                    .idTicket(ticket.getId()) // ID del ticket desde la base de datos
                    .idPlayer(playerOptional.get().getId()) // ID del jugador desde la base de datos
                    .idRoom(roomId) // ID de la sala definida
                    .players(playersCount) // Número de jugadores
                    .price(totalPrice) // Precio calculado
                    .completion(0) // Valor de completion predeterminado
                    .dateSale(java.time.LocalDate.now()) // Fecha actual
                    .isActive(true) // Venta activa
                    .build();

            // Guardar la venta en la base de datos
            Sale savedSale = saleDAO.create(newSale);
            if (savedSale == null || savedSale.getId() == 0) {
                baseView.displayErrorMessage("Sale creation failed: Unable to save the sale to the database.");
                return;
            }

            // Mostrar mensaje de éxito
            baseView.displayMessageln("Sale created successfully! Sale ID: " + savedSale.getId());
        } catch (DAOException e) {
            handleError("An error occurred while creating the sale: ", e);
        } catch (NumberFormatException e) {
            baseView.displayErrorMessage("Invalid input. Please enter valid data.");
        }
    }*/
  private void createSale() {
        try {
            // Solicitar ID del ticket
            int ticketId = ConsoleUtils.readRequiredInt("Enter the Ticket ID you want to purchase: ");
            Optional<Ticket> ticketOptional = saleDAO.findTicketById(ticketId);

            if (ticketOptional.isEmpty()) {
                baseView.displayErrorMessage("Sale failed: Ticket with ID " + ticketId + " does not exist.");
                return;
            }

            // Solicitar ID del jugador
            int playerId = ConsoleUtils.readRequiredInt("Enter the Player ID: ");
            Optional<Player> playerOptional = saleDAO.findPlayerById(playerId);

            if (playerOptional.isEmpty()) {
                baseView.displayErrorMessage("Sale failed: Player with ID " + playerId + " does not exist.");
                return;
            }

            // Solicitar número de jugadores
            int playersCount = ConsoleUtils.readRequiredInt("Enter the number of players: ");
            if (playersCount <= 0) {
                baseView.displayErrorMessage("Sale failed: The number of players must be greater than 0.");
                return;
            }

            // Calcular el precio total
            Ticket ticket = ticketOptional.get();
            Player player = playerOptional.get();
            BigDecimal totalPrice = ticket.getPrice().multiply(BigDecimal.valueOf(playersCount));

            // Crear el objeto Sale
            Sale newSale = Sale.builder()
                    .idTicket(ticketId)
                    .idPlayer(playerId)
                    .idRoom(1) // En este caso, asumimos que es la sala con ID 1 (sin lógica para seleccionarla)
                    .players(playersCount)
                    .price(totalPrice)
                    .completion(0) // Por ahora, no estamos calculando "completion"
                    .dateSale(java.time.LocalDate.now())
                    .isActive(true)
                    .build();

            // Guardar la venta en la base de datos
            Sale savedSale = saleDAO.create(newSale);
            if (savedSale == null || savedSale.getId() == 0) {
                baseView.displayErrorMessage("Sale creation failed: Unable to save the sale to the database.");
                return;
            }

            // Mostrar mensaje de éxito
            baseView.displayMessageln("Sale created successfully! Sale ID: " + savedSale.getId());
        } catch (DAOException e) {
            handleError("An error occurred while creating the sale: ", e);
        } catch (NumberFormatException e) {
            baseView.displayErrorMessage("Invalid input. Please enter a valid number.");
        }
  }

    private void deleteSale() {
        try {
            Optional<Integer> idOpt = saleView.getSaleId();
            if (idOpt.isPresent()) {
                saleDAO.deleteById(idOpt.get());
                baseView.displayMessageln("Sale with ID " + idOpt.get() + " deleted successfully (if existed).");
            } else {
                baseView.displayErrorMessage("Sale ID is required to delete a sale.");
            }
        } catch (DAOException e) {
            handleError("An error occurred while deleting the sale: ", e);
        }
    }

    private void calculateTotalBenefits() {
        try {
            List<Sale> sales = saleDAO.findAll();
            double totalBenefits = sales.stream()
                    .mapToDouble(sale -> sale.getPrice().doubleValue())
                    .sum();
            baseView.displayMessageln("Total Benefits from sales: $" + totalBenefits);
        } catch (DAOException e) {
            handleError("An error occurred while calculating total benefits: ", e);
        }
    }

    private void listAllSales() {
        try {
            List<Sale> sales = saleDAO.findAll();
            saleView.displayListSales(sales);
        } catch (DAOException e) {
            handleError("An error occurred while listing sales: ", e);
        }
    }

    private SaleDetailsDTO getSaleDetailsForOperation(boolean forUpdate) {
        try {
            // Solicitar el ID de la venta (saleId) al usuario
            int saleId = ConsoleUtils.readRequiredInt("Enter sale ID: ");
            if (saleId <= 0) {
                baseView.displayErrorMessage("Invalid Sale ID. Operation canceled.");
                return null;
            }

            // Buscar la venta en la base de datos
            Optional<Sale> saleOptional = saleDAO.findById(saleId);
            if (saleOptional.isEmpty()) {
                baseView.displayErrorMessage("Sale not found with ID: " + saleId);
                return null;
            }

            // Obtener la venta (sale)
            Sale sale = saleOptional.get();

            // Buscar el Player relacionado a la venta
            Optional<Player> playerOptional = playerDAO.findById(sale.getIdPlayer());
            if (playerOptional.isEmpty()) {
                baseView.displayErrorMessage("Player not found with ID: " + sale.getIdPlayer());
                return null;
            }

            // Obtener el nombre del Player
            Player player = playerOptional.get();

            // Obtener el precio (actualizar si se requiere para edición)
            BigDecimal price = sale.getPrice();
            if (forUpdate) {
                price = ConsoleUtils.readRequiredBigDecimal("Enter new sale price: ");
                if (price.compareTo(BigDecimal.ZERO) <= 0) {
                    baseView.displayErrorMessage("Invalid price. Price must be greater than zero.");
                    return null;
                }
            }

            // Devolver el DTO con la información del Player y el Ticket
            return new SaleDetailsDTO(sale.getIdTicket(), price, player.getName());
        } catch (DAOException e) {
            handleError("Error while retrieving sale details: ", e);
            return null;
        } catch (NumberFormatException e) {
            baseView.displayErrorMessage("Invalid input. Please enter valid numeric data.");
            return null;
        }
    }

    private int readSaleIdFromUser() {
        try {
            return ConsoleUtils.readRequiredInt("Enter sale ID: ");
        } catch (NumberFormatException e) {
            baseView.displayErrorMessage("Invalid input. Sale ID must be a number.");
            return -1;
        }
    }

    private Optional<SaleDetailsDTO> validateAndRetrieveSaleDetails(int saleId, boolean forUpdate) {
        Optional<Sale> saleOptional = saleDAO.findById(saleId);
        if (saleOptional.isEmpty()) {
            baseView.displayErrorMessage("Sale not found with ID: " + saleId);
            return Optional.empty();
        }

        Sale sale = saleOptional.get();
        Optional<Player> playerOptional = playerDAO.findById(sale.getIdPlayer());
        if (playerOptional.isEmpty()) {
            baseView.displayErrorMessage("Player not found with ID: " + sale.getIdPlayer());
            return Optional.empty();
        }

        BigDecimal price = sale.getPrice();
        if (forUpdate) {
            try {
                price = ConsoleUtils.readRequiredBigDecimal("Enter new sale price: ");
            } catch (NumberFormatException e) {
                baseView.displayErrorMessage("Invalid price entered. Operation canceled.");
                return Optional.empty();
            }
        }

        return Optional.of(new SaleDetailsDTO(sale.getIdTicket(), price, playerOptional.get().getName()));
    }

    private void handleError(String message, Exception e) {
        baseView.displayErrorMessage(message + e.getMessage());
        log.error(message, e);
    }
}