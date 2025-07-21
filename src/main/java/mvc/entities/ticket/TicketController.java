package mvc.entities.ticket;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.exceptions.NotFoundException;
import dao.factory.DAOFactory;
import mvc.enumsMenu.OptionsMenuCLFUSDE;
import mvc.view.BaseView;

import java.util.List;
import java.util.Optional;

public class TicketController {

    private static TicketController ticketControllerInstance;
    private final TicketDAO TICKET_DAO;
    private final BaseView BASE_VIEW;
    private TicketView ticketView;
    private static final String NAME_OBJECT = "Ticket";

    public TicketController() {
        BASE_VIEW = BaseView.getInstance();
        BASE_VIEW.displayDebugMessage("Creation Class: " + this.getClass().getName());
        try {
            TICKET_DAO = DAOFactory.getDAOFactory().getTicketDAO();
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException(e);
        }
        ticketView = new TicketView();
    }

    public static TicketController getInstance() {
        if (ticketControllerInstance == null) {
            synchronized (TicketController.class) {
                if (ticketControllerInstance == null) {
                    ticketControllerInstance = new TicketController();
                }
            }
        }
        return ticketControllerInstance;
    }

    public void mainMenu() {
        do {
            BASE_VIEW.displayMessageln(OptionsMenuCLFUSDE.viewMenu(NAME_OBJECT.toUpperCase() + " MANAGEMENT"));
            int answer = BASE_VIEW.getReadRequiredInt("Choose an option: ");
            OptionsMenuCLFUSDE idMenu = OptionsMenuCLFUSDE.getOptionByNumber(answer);
            try {
                switch (idMenu) {
                    case EXIT -> {
                        BASE_VIEW.displayMessage2ln("Returning to Main Menu...");
                        return;
                    }
                    case CREATE -> createTicket();
                    case LIST_ALL -> listAllTickets();
                    case FIND_BY_ID -> getTicketById();
                    case UPDATE -> updateTicket();
                    case SOFT_DELETE -> softDeleteTicketById();
                    case DELETE -> deleteTicketById();

                    default -> BASE_VIEW.displayErrorMessage("Error: The value in menu is wrong: " + idMenu);
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

    private void createTicket() {
        BASE_VIEW.displayMessageln("#### CREATE " + NAME_OBJECT + "  #################");
            Ticket newTicket = ticketView.getTicketDetailsCreate();
            Ticket savedTicket = TICKET_DAO.create(newTicket);
            BASE_VIEW.displayMessage2ln(NAME_OBJECT + " created successfully: " + savedTicket.getName() + " (ID: " + savedTicket.getId() + ")");
    }

    private void getTicketById() {
        BASE_VIEW.displayMessage2ln("####  GET " + NAME_OBJECT.toUpperCase() + " BY ID  #################");
             Optional<Ticket> optionalTicket = TICKET_DAO.findById(getTicketIdWithList());

            ticketView.displayRecordTicket(optionalTicket.get());
    }

    private void listAllTickets() throws DAOException {
        BASE_VIEW.displayMessage2ln("####  LIST ALL " + NAME_OBJECT.toUpperCase() + "S  #################");
            listAllTicketDetail();
    }

    private void updateTicket() {
        BASE_VIEW.displayMessage2ln("####  UPDATE  " + NAME_OBJECT.toUpperCase() + "  #################");
            Optional<Ticket> existTicketOpt = TICKET_DAO.findById(getTicketIdWithList());

            BASE_VIEW.displayMessage2ln("Current " + NAME_OBJECT + " Details:");
            ticketView.displayRecordTicket(existTicketOpt.get());

            BASE_VIEW.displayMessage2ln("Enter new details:");
            BASE_VIEW.displayMessageln("Enter new value or [INTRO] for not changes.");
            Ticket updatedTicket = ticketView.getUpdateTicketDetails(existTicketOpt.get());

            Ticket savedTicket = TICKET_DAO.update(updatedTicket);
            BASE_VIEW.displayMessage2ln(NAME_OBJECT + " updated successfully: " + savedTicket.getName() + " (ID: " + savedTicket.getId() + ")");
    }

    private void deleteTicketById() throws DAOException, NotFoundException {
        BASE_VIEW.displayMessage2ln("####  DELETE " + NAME_OBJECT.toUpperCase() + "  #################");
            TICKET_DAO.deleteById(getTicketIdWithList());
            BASE_VIEW.displayMessage2ln(NAME_OBJECT + " deleted successfully (if existed).");
    }

    private void softDeleteTicketById() throws DAOException {
        BASE_VIEW.displayMessage2ln("#### SOFT DELETE  " + NAME_OBJECT.toUpperCase() + "  #################");
            Optional<Ticket> existTicketOpt = TICKET_DAO.findById(getTicketIdWithList());
            existTicketOpt.get().setActive(false);

            TICKET_DAO.update(existTicketOpt.get());
            BASE_VIEW.displayMessage2ln(NAME_OBJECT + " soft deleted successfully: " + existTicketOpt.get().getName() + " (ID: " + existTicketOpt.get().getId() + ")");
    }

    public Optional<Ticket> getFindTicketById(int ticketId) {
        return TICKET_DAO.findById(ticketId);
    }

    public int getTicketIdWithList() {
        listAllTicketDetail();
        Optional<Integer> searchID = BASE_VIEW.getReadValueInt("Enter " + NAME_OBJECT + " ID: ");
        if (searchID.isEmpty() || TICKET_DAO.findById(searchID.get()).isEmpty()) {
            String message =  NAME_OBJECT + " with ID required or not found.";
            BASE_VIEW.displayErrorMessage(message);
            throw new IllegalArgumentException(message);
        }
        return searchID.get();
    }

    private void listAllTicketDetail() throws DAOException {
        List<Ticket> tickets = getTicketFindAll();
        ticketView.displayListTickets(tickets);
    }

    public String getTicketNameById(int ticketId) throws DAOException {
        return TICKET_DAO.findById(ticketId)
                .map(Ticket::getName)
                .orElse("Unknown " + NAME_OBJECT);
    }

    public List<Ticket> getTicketFindAll() {
        return TICKET_DAO.findAll();
    }
}