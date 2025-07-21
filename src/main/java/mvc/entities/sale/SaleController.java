package mvc.entities.sale;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.exceptions.NotFoundException;

import mvc.entities.Inputs;
import mvc.entities.ServiceManager;
import mvc.view.BaseView;

import java.util.Optional;

public class SaleController {
    private static SaleController instance;
    private final SaleService SALE_SERVICE;
    private final SaleView SALE_VIEW;

    private final BaseView BASE_VIEW;
    private static final String NAME_OBJECT = "Ticket";

    private SaleController(SaleService saleService) {
        BASE_VIEW = BaseView.getInstance();
        BASE_VIEW.displayDebugMessage("Creation Class: " + this.getClass().getName());
        this.SALE_SERVICE = saleService;
        this.SALE_VIEW = new SaleView();
    }

    public static SaleController getInstance() throws DatabaseConnectionException {
        if (instance == null) {
            synchronized (SaleController.class) {
                if (instance == null) {
                    SaleService service = ServiceManager.getInstance().getSaleService();
                    instance = new SaleController(service);
                }
            }
        }
        return instance;
    }

    public void mainMenu() throws DatabaseConnectionException {
        do {
            BASE_VIEW.displayMessageln(OptionsMenuSale.viewMenu("SALE MANAGEMENT"));
            int answer = BASE_VIEW.getReadRequiredInt("Choose an option: ");
            OptionsMenuSale selectedOption = OptionsMenuSale.getOptionByNumber(answer);
            try {
                switch (selectedOption) {
                    case EXIT -> {
                        BASE_VIEW.displayMessageln("Returning to Main Menu...");
                        return;
                    }
                    case SELL -> createSale();
                    case DELETE -> deleteSaleById();
                    case CALCULATE_TOTAL_BENEFITS -> calculateTotalBenefits();
                    case SHOW_ALL_SALES -> listAllSales();

                    default -> BASE_VIEW.displayErrorMessage("Unknown option selected.");
                }
            } catch (IllegalArgumentException e) {
                BASE_VIEW.displayErrorMessage("Error: Invalid data entered. " + e.getMessage());
            } catch (DAOException e) {
                BASE_VIEW.displayErrorMessage("Error: Database operation failed. " + e.getMessage());
            } catch (NullPointerException e) {
                BASE_VIEW.displayErrorMessage("An unexpected error occurred (Null Pointer): " + e.getMessage());
            } catch (NotFoundException e) {
                BASE_VIEW.displayErrorMessage("Entity not found: " + e.getMessage());
            }
        } while (true);
    }

    private void createSale() throws NotFoundException {
        BASE_VIEW.displayMessage2ln("####  LIST ALL " + NAME_OBJECT.toUpperCase() + "S  #################");
    
        Inputs valuesInput = SALE_VIEW.getSaleDetailsCreate(
                SALE_SERVICE.getAllPlayers(),
                SALE_SERVICE.getAllRooms(),
                SALE_SERVICE.getAllTickets());
        SALE_SERVICE.createSale(valuesInput);
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " added successfully.");
    }

    private void getSaleById() throws NotFoundException {
        BASE_VIEW.displayMessage2ln("####  GET " + NAME_OBJECT.toUpperCase() + " BY ID  #################");
        Optional<Integer> searchIdOpt = getSaleIdWithList();
        Optional<Sale> optionalSale = SALE_SERVICE.getSaleById(searchIdOpt.get());
        SALE_VIEW.displayRecordSale(optionalSale.get());
    }

    private void listAllSales() throws DAOException {
        BASE_VIEW.displayMessage2ln("####  LIST ALL " + NAME_OBJECT.toUpperCase() + "S  #################");
        BASE_VIEW.displayMessageln(SALE_SERVICE.getAllSales().getMessage());
    }

    private void updateSale() throws NotFoundException {
        BASE_VIEW.displayMessage2ln("####  UPDATE  " + NAME_OBJECT.toUpperCase() + "  #################");
        Optional<Integer> searchIdOpt = getSaleIdWithList();
        Optional<Sale> existSaleOpt = SALE_SERVICE.getSaleById(searchIdOpt.get());
        if (existSaleOpt.isEmpty()) {
            String message = NAME_OBJECT + " not found for update.";
            BASE_VIEW.displayErrorMessage(message);
            throw new IllegalArgumentException(message);
        }
        BASE_VIEW.displayMessage2ln("Current " + NAME_OBJECT + " Details:");
        SALE_VIEW.displayRecordSale(existSaleOpt.get());

        BASE_VIEW.displayMessageln("Enter new details:");
        BASE_VIEW.displayMessageln("Enter new value or [INTRO] for no changes.");
        Sale updatedSale = SALE_VIEW.getUpdateSaleDetails(existSaleOpt.get(),SALE_SERVICE.getAllPlayers(),
                SALE_SERVICE.getAllRooms(),
                SALE_SERVICE.getAllTickets());
        SALE_SERVICE.updateSale(updatedSale);
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " updated successfully.");
    }

    private void deleteSaleById() throws NotFoundException {
        BASE_VIEW.displayMessage2ln("####  DELETE " + NAME_OBJECT.toUpperCase() + "  #################");
        Optional<Integer> searchIdOpt = getSaleIdWithList();
        SALE_SERVICE.deleteSale(searchIdOpt.get());
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " deleted successfully.");
    }

    private void softDeleteSaleById() throws NotFoundException {
        BASE_VIEW.displayMessage2ln("#### SOFT DELETE  " + NAME_OBJECT.toUpperCase() + "  #################");
        Optional<Integer> searchIdOpt = getSaleIdWithList();
        SALE_SERVICE.softDeleteSale(searchIdOpt.get());
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " soft deleted successfully.");
    }

    private Optional<Integer> getSaleIdWithList() throws NotFoundException {
        Optional<Integer> searchIdOpt = BASE_VIEW.getReadValueIntMinMax("List of Sales", "Input Sale ID: ", SALE_SERVICE.getAllSales());
        if (searchIdOpt.isEmpty()) {
            String message = NAME_OBJECT + " not found.";
            BASE_VIEW.displayErrorMessage(message);
            throw new NotFoundException(message);
        }
        return  searchIdOpt;
    }

    private void calculateTotalBenefits() {
        double totalBenefits = SALE_SERVICE.calculateTotalBenefits();
        BASE_VIEW.displayMessageln("Total Benefits from sales: $" + totalBenefits);
    }
}