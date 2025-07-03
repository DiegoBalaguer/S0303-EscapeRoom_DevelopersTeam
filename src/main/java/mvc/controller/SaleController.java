package mvc.controller;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;

import dao.interfaces.SaleDAO;
import mvc.dto.SaleDisplayDTO;
import mvc.enumsMenu.OptionsMenuSale;
import lombok.extern.slf4j.Slf4j;
import mvc.model.Player;
import mvc.model.Room;
import mvc.model.Sale;
import mvc.model.Ticket;
import mvc.view.BaseView;
import mvc.view.SaleView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
public class SaleController {
    private static volatile SaleController saleControllerInstance;
    private final SaleView saleView;
    private final SaleDAO SALE_DAO;

    private static BaseView baseView;
    private static final String NAME_OBJECT = "Ticket";

    private SaleController() {
        baseView = BaseView.getInstance();
        this.saleView = new SaleView();

        try {
            this.SALE_DAO = DAOFactory.getDAOFactory().getSaleDAO();
        } catch (DatabaseConnectionException e) {
            log.error("Error al inicializar los DAOs debido a problemas de conexión con la base de datos: {}", e.getMessage(), e);
            throw new IllegalStateException("No se pudieron inicializar los DAOs.", e);
        } catch (Exception e) {
            log.error("Ocurrió un error inesperado al inicializar los DAOs: {}", e.getMessage(), e);
            throw new IllegalStateException("Error inesperado al inicializar los DAOs.", e);
        }
    }

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
        do {
            baseView.displayMessageln(OptionsMenuSale.viewMenu("SALE MANAGEMENT"));
            int answer = baseView.getReadRequiredInt("Choose an option: ");
            OptionsMenuSale selectedOption = OptionsMenuSale.getOptionByNumber(answer);

            if (selectedOption != null) {
                try {
                    switch (selectedOption) {
                        case EXIT -> {
                            baseView.displayMessageln("Returning to Main Menu...");
                            return;
                        }
                        case SELL -> createSale();
                        case DELETE -> deleteSale();
                        case CALCULATE_TOTAL_BENEFITS -> calculateTotalBenefits();
                        case SHOW_ALL_TICKETS -> listAllSalesDetailDTO();

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
        } while (true);
    }

    private void createSale() {
        baseView.displayMessage2ln("####  LIST ALL " + NAME_OBJECT.toUpperCase() + "S  #################");

        try {
            List<Ticket> activeTickets = TicketController.getInstance().getTicketFindAll();

            if (activeTickets.isEmpty()) {
                baseView.displayErrorMessage("No active tickets found.");
                return;
            }

            int roomId = RoomController.getInstance().getRoomIdWithList();
            Optional<Room> roomOptional = RoomController.getInstance().getFindRoomById(roomId);
            if (roomOptional.isEmpty()) {
                baseView.displayErrorMessage("Sale failed: Room with ID " + roomId + " does not exist.");
                return;
            }

            int ticketId = TicketController.getInstance().getTicketIdWithList();
            Optional<Ticket> ticketOptional = TicketController.getInstance().getFindTicketById(ticketId);
            if (ticketOptional.isEmpty()) {
                baseView.displayErrorMessage("Sale failed: Ticket with ID " + ticketId + " does not exist.");
                return;
            }

            int playerId = PlayerController.getInstance().getPlayerIdWithList();
            Optional<Player> playerOptional = PlayerController.getInstance().findById(playerId);
            if (playerOptional.isEmpty()) {
                baseView.displayErrorMessage("Sale failed: Player with ID " + playerId + " does not exist.");
                return;
            }

            int playersCount = baseView.getReadRequiredInt("Enter the number of players: ");
            if (playersCount <= 0) {
                baseView.displayErrorMessage("Sale failed: The number of players must be greater than 0.");
                return;
            }

            BigDecimal totalPrice =
                    ticketOptional.get().getPrice().multiply(BigDecimal.valueOf(playersCount));

            Sale newSale = Sale.builder()
                    .idTicket(ticketId)
                    .idPlayer(playerId)
                    .idRoom(roomId)
                    .players(playersCount)
                    .price(totalPrice)
                    .completion(0)
                    .dateSale(java.time.LocalDateTime.now())
                    .isActive(true)
                    .build();

            Sale savedSale = createSale(newSale);
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
            getPlayerIdWithList();
            int idOpt = getPlayerIdWithList();//saleView.getSaleId();
            deleteById(idOpt);
            baseView.displayMessageln("Sale with ID " + idOpt + " deleted successfully (if existed).");
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

    private Sale createSale(Sale sale) {
        return SALE_DAO.create(sale);
    }

    private void deleteById(int saleId) {
        SALE_DAO.deleteById(saleId);
    }

    public Optional<Sale> getfindById(int saleId) {
        return SALE_DAO.findById(saleId);
    }

    public int getPlayerIdWithList() {
        listAllSalesDetail();
        Optional<Integer> searchID = baseView.getReadValueInt("Enter " + NAME_OBJECT + " ID: ");
        if (searchID.isEmpty() || getfindById(searchID.get()).isEmpty()) {
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

    public boolean listAllSalesDetailDTO() throws DAOException {
        List<SaleDisplayDTO> saleDisplayDTOS = getfindAllCompleteInfo();
        if (saleDisplayDTOS.isEmpty()) {
            baseView.displayMessageln("No " + NAME_OBJECT + "s found.");
            return false;
        }
        saleView.displayListSaleDTO(saleDisplayDTOS);
        return true;
    }

    public List<SaleDisplayDTO> getfindAllCompleteInfo() {
        return SALE_DAO.findAllCompleteInfo();
    }

    private void handleError(String message, Exception e) {
        baseView.displayErrorMessage(message + e.getMessage());
        log.error(message, e);
    }
}