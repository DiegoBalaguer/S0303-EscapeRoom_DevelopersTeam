package mvc.view;

import mvc.dto.TicketMapper;
import mvc.model.Ticket;
import utils.ConsoleUtils;
import utils.StringUtils;

import java.util.List;
import java.util.Optional;

public class TicketView {

    private static BaseView baseView = BaseView.getInstance();
    private static final String NAME_OBJECT = "Ticket";

    public Ticket getTicketDetailsCreate() {
        try {
            return Ticket.builder()
                    .name(getInputName())
                    .description(getInputDescription())
                    .isActive(true)
                    .build();
        } catch (Exception e) {
            baseView.displayErrorMessage("Error collecting " + NAME_OBJECT + " details: " + e.getMessage());
            return null;
        }
    }

    private String getInputName() {
        return ConsoleUtils.readRequiredString("Enter name: ");
    }

    private String getInputDescription() {
        return ConsoleUtils.readRequiredString("Enter description: ");
    }

    public Ticket getUpdateTicketDetails(Ticket ticket) {
        try {
            ticket.setName(getUpdateName(ticket.getName()));
            ticket.setDescription(getUpdateDescription(ticket.getDescription()));
            ticket.setActive(getUpdateIsActive(ticket.isActive()));
            return ticket;
        } catch (Exception e) {
            baseView.displayErrorMessage("Error editing " + NAME_OBJECT + ": " + e.getMessage());
            throw new IllegalArgumentException("Error editing " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private String getUpdateName(String oldValue) {
        return ConsoleUtils.readStringWithDefault("Enter name: ", Optional.of(oldValue)).get();
    }

    private String getUpdateDescription(String oldValue) {
        return ConsoleUtils.readStringWithDefault("Enter description: ", Optional.of(oldValue)).get();
    }

    private Boolean getUpdateIsActive(boolean oldValue) {
        return ConsoleUtils.readBooleanWithDefault("Enter is active ('Y' or 'N'): ", Optional.of(oldValue)).get();
    }

    public void displayRecordTicket(Ticket ticket) {
        String message = "";
        if (ticket != null) {
            message += baseView.LINE + "--- " + NAME_OBJECT + " Details ---" + baseView.LINE;
            message += "ID: " + ticket.getId() + baseView.LINE;
            message += "Name: " + ticket.getName() + baseView.LINE;
            message += "Description: " + ticket.getDescription() + baseView.LINE;
            message += "Is Active: " + (ticket.isActive() ? "Yes" : "No") + baseView.LINE;
            message += "-------------------------" + baseView.LINE;
        } else {
            message = NAME_OBJECT + " not found.";
        }
        baseView.displayMessageln(message);
    }

    public void displayListTickets(List<Ticket> tickets) {
        if (tickets.isEmpty()) {
            baseView.displayMessageln("No " + NAME_OBJECT + "s found.");
            return;
        }
        baseView.displayMessageln(
                StringUtils.makeLineToList(TicketMapper.toDisplayDTO(tickets.getFirst()).toListHead()));

        tickets.forEach(ticket -> baseView.displayMessageln(
                StringUtils.makeLineToList(TicketMapper.toDisplayDTO(ticket).toList())));
        baseView.displayMessage2ln("-------------------");
    }
}
