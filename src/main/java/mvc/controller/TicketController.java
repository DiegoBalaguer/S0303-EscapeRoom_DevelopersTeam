package mvc.controller;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.TicketDAO;
import mvc.enumsMenu.OptionsMenuCLFUSDE;
import mvc.model.Player;
import mvc.model.Ticket;
import mvc.view.BaseView;
import mvc.view.TicketView;

import java.util.List;
import java.util.Optional;

public class TicketController {

    private static TicketController ticketControllerInstance;
    private final TicketDAO TICKET_DAO;
    private BaseView baseView;
    private TicketView ticketView;
    private static final String NAME_OBJECT = "Ticket";

    public TicketController() {
        baseView = BaseView.getInstance();
        baseView.displayDebugMessage("Creation Class: " + this.getClass().getName());
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
            baseView.displayMessageln(OptionsMenuCLFUSDE.viewMenu(NAME_OBJECT.toUpperCase() + " MANAGEMENT"));
            int answer = baseView.getReadRequiredInt("Choose an option: ");
            OptionsMenuCLFUSDE idMenu = OptionsMenuCLFUSDE.getOptionByNumber(answer);
            try {
                switch (idMenu) {
                    case EXIT -> {
                        baseView.displayMessage2ln("Returning to Main Menu...");
                        return;
                    }
                    case CREATE -> createTicket();
                    case LIST_ALL -> listAllTickets();
                    case FIND_BY_ID -> getTicketById();
                    case UPDATE -> updateTicket();
                    case SOFT_DELETE -> softDeleteTicketById();
                    case DELETE -> deleteTicketById();

                    default -> baseView.displayErrorMessage("Error: The value in menu is wrong: " + idMenu);
                }
            } catch (IllegalArgumentException | NullPointerException e) {
                baseView.displayErrorMessage("Error: The value in menu is wrong." + e.getMessage());
            } catch (DAOException e) {
                baseView.displayErrorMessage("Error: Database operation failed: " + e.getMessage());
            } catch (Exception e) {
                baseView.displayErrorMessage("Error: An unexpected error occurred: " + e.getMessage());
            }
        } while (true);
    }


    private void createTicket() {
        baseView.displayMessageln("#### CREATE " + NAME_OBJECT + "  #################");
        try {
            Ticket newTicket = ticketView.getTicketDetailsCreate();
            Ticket savedTicket = TICKET_DAO.create(newTicket);
            baseView.displayMessage2ln(NAME_OBJECT + " created successfully: " + savedTicket.getName() + " (ID: " + savedTicket.getId() + ")");
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error creating " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void getTicketById() {
        baseView.displayMessage2ln("####  GET " + NAME_OBJECT.toUpperCase() + " BY ID  #################");
        try {
             Optional<Ticket> optionalTicket = TICKET_DAO.findById(getTicketIdWithList());

            ticketView.displayRecordTicket(optionalTicket.get());
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error show " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void listAllTickets() throws DAOException {
        baseView.displayMessage2ln("####  LIST ALL " + NAME_OBJECT.toUpperCase() + "S  #################");
        try {
            listAllTicketDetail();
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error list all " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void updateTicket() {
        baseView.displayMessage2ln("####  UPDATE  " + NAME_OBJECT.toUpperCase() + "  #################");
        try {
            Optional<Ticket> existTicketOpt = TICKET_DAO.findById(getTicketIdWithList());

            baseView.displayMessage2ln("Current " + NAME_OBJECT + " Details:");
            ticketView.displayRecordTicket(existTicketOpt.get());

            baseView.displayMessage2ln("Enter new details:");
            baseView.displayMessageln("Enter new value or [INTRO] for not changes.");
            Ticket updatedTicket = ticketView.getUpdateTicketDetails(existTicketOpt.get());

            Ticket savedTicket = TICKET_DAO.update(updatedTicket);
            baseView.displayMessage2ln(NAME_OBJECT + " updated successfully: " + savedTicket.getName() + " (ID: " + savedTicket.getId() + ")");
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error updating " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void deleteTicketById() {
        baseView.displayMessage2ln("####  DELETE " + NAME_OBJECT.toUpperCase() + "  #################");
        try {
            TICKET_DAO.deleteById(getTicketIdWithList());
            baseView.displayMessage2ln(NAME_OBJECT + " deleted successfully (if existed).");
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error deleting the " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void softDeleteTicketById() throws DAOException {
        baseView.displayMessage2ln("#### SOFT DELETE  " + NAME_OBJECT.toUpperCase() + "  #################");
        try {
            Optional<Ticket> existTicketOpt = TICKET_DAO.findById(getTicketIdWithList());
            existTicketOpt.get().setActive(false);

            TICKET_DAO.update(existTicketOpt.get());
            baseView.displayMessage2ln(NAME_OBJECT + " soft deleted successfully: " + existTicketOpt.get().getName() + " (ID: " + existTicketOpt.get().getId() + ")");
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error soft deleting " + NAME_OBJECT +": " + e.getMessage());
        }
    }

    public Optional<Ticket> getFindTicketById(int ticketId) {
        return TICKET_DAO.findById(ticketId);
    }

    public int getTicketIdWithList() {
        listAllTicketDetail();
        Optional<Integer> searchID = baseView.getReadValueInt("Enter " + NAME_OBJECT + " ID: ");
        if (searchID.isEmpty() || TICKET_DAO.findById(searchID.get()).isEmpty()) {
            String message =  NAME_OBJECT + " with ID required or not found.";
            baseView.displayErrorMessage(message);
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