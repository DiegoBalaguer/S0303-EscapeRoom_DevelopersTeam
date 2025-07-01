package mvc.controller;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.TicketDAO;
import dao.interfaces.SaleDAO;
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
    private final SaleDAO SALE_DAO;
    private final TicketDAO TICKET_DAO;

    private static BaseView baseView;
    private static final String NAME_OBJECT = "Ticket";

    private SaleController() {
        baseView = new BaseView();
        this.saleView = new SaleView();

        try {
            this.SALE_DAO = DAOFactory.getDAOFactory().getSaleDAO();
            this.TICKET_DAO = DAOFactory.getDAOFactory().getTicketDAO();
        } catch (DatabaseConnectionException e) {
            log.error("Error al inicializar los DAOs debido a problemas de conexión con la base de datos: {}", e.getMessage(), e);
            throw new IllegalStateException("No se pudieron inicializar los DAOs.", e);
        } catch (Exception e) {
            log.error("Ocurrió un error inesperado al inicializar los DAOs: {}", e.getMessage(), e);
            throw new IllegalStateException("Error inesperado al inicializar los DAOs.", e);
        }

       /* validateNotNull(this.SALE_DAO, "SaleDAO no pudo ser inicializado.");
        validateNotNull(this.playerDAO, "PlayerDAO no pudo ser inicializado.");
        validateNotNull(this.ticketDAO, "TicketDAO no pudo ser inicializado."); // Validamos que el ticketDAO no sea nulo*/
    }

    // Método helper para validar nulos
   /* private void validateNotNull(Object obj, String errorMsg) {
        if (obj == null) {
            log.error(errorMsg);
            throw new IllegalStateException(errorMsg);
        }
    }*/

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
                        case SHOW_ALL_TICKETS -> listAllSalesDetail();

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
  private void createSale() {
      baseView.displayMessage2ln("####  LIST ALL " + NAME_OBJECT.toUpperCase() + "S  #################");

      try {
          List<Ticket> activeTickets = SALE_DAO.findAllActiveTickets();

          if (activeTickets.isEmpty()) {
              baseView.displayErrorMessage("No active tickets found.");
              return;
          }

          saleView.displayActiveTickets(activeTickets);



          int ticketId = ConsoleUtils.readRequiredInt("Enter the Ticket ID you want to purchase: ");
            Optional<Ticket> ticketOptional = SALE_DAO.findTicketById(ticketId);

            if (ticketOptional.isEmpty()) {
                baseView.displayErrorMessage("Sale failed: Ticket with ID " + ticketId + " does not exist.");
                return;
            }

            int playerId = ConsoleUtils.readRequiredInt("Enter the Player ID: ");
            Optional<Player> playerOptional = SALE_DAO.findPlayerById(playerId);

            if (playerOptional.isEmpty()) {
                baseView.displayErrorMessage("Sale failed: Player with ID " + playerId + " does not exist.");
                return;
            }

            int playersCount = ConsoleUtils.readRequiredInt("Enter the number of players: ");
            if (playersCount <= 0) {
                baseView.displayErrorMessage("Sale failed: The number of players must be greater than 0.");
                return;
            }

            Ticket ticket = ticketOptional.get();
            Player player = playerOptional.get();
            BigDecimal totalPrice = ticket.getPrice().multiply(BigDecimal.valueOf(playersCount));

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

            Sale savedSale = SALE_DAO.create(newSale);
            if (savedSale == null || savedSale.getId() == 0) {
                baseView.displayErrorMessage("Sale creation failed: Unable to save the sale to the database.");
                return;
            }

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
                SALE_DAO.deleteById(idOpt.get());
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
            List<Sale> sales = SALE_DAO.findAll();
            double totalBenefits = sales.stream()
                    .mapToDouble(sale -> sale.getPrice().doubleValue())
                    .sum();
            baseView.displayMessageln("Total Benefits from sales: $" + totalBenefits);
        } catch (DAOException e) {
            handleError("An error occurred while calculating total benefits: ", e);
        }
    }

    public int getTicketIdWithList() {
        listAllSalesDetail();
        Optional<Integer> searchID = baseView.getReadValueInt("Enter " + NAME_OBJECT + " ID: ");
        if (searchID.isEmpty() || SALE_DAO.findById(searchID.get()).isEmpty()) {
            String message = NAME_OBJECT + " with ID required or not found.";
            baseView.displayErrorMessage(message);
            throw new IllegalArgumentException(message);
        }
        return searchID.get();
    }

    private void listAllSalesDetail() {
            List<Sale> sales = SALE_DAO.findAll();
            saleView.displayListSales(sales);
    }

    private void handleError(String message, Exception e) {
        baseView.displayErrorMessage(message + e.getMessage());
        log.error(message, e);
    }

}