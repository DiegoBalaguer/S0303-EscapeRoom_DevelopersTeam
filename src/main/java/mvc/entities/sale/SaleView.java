package mvc.entities.sale;

import mvc.entities.Inputs;
import mvc.entities.MessageMinMax;
import mvc.entities.ticket.Ticket;
import mvc.view.BaseView;
import utils.ConsoleUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class SaleView {

    private final BaseView BASE_VIEW = BaseView.getInstance();
    private static final String NAME_OBJECT = "Sale";

    public Optional<Integer> getSaleId() {
        try {
            int id = ConsoleUtils.readRequiredInt("Enter sale ID: ");
            return Optional.of(id);
        } catch (NumberFormatException e) {
            BASE_VIEW.displayErrorMessage("Invalid ID. Please enter a number.");
            return Optional.empty();
        }
    }

    protected Inputs getSaleDetailsCreate(MessageMinMax playersList, MessageMinMax roomsList, MessageMinMax ticketsList) {

        Optional<Integer> playerId = getEntityId("List of Players", "Input Player ID: ", playersList);
        Optional<Integer> roomId = getEntityId("List of Rooms", "Input Room ID: ", roomsList);
        Optional<Integer> ticketId = getEntityId("List of Tickets", "Input Tiket ID: ", ticketsList);
        Optional<Integer> playersCount = getInputPlayersCount();

        return new Inputs.Builder()
                .put("playerId", playerId.orElse(null))
                .put("roomId", roomId.orElse(null))
                .put("ticketId", ticketId.orElse(null))
                .put("playersCount", playersCount.orElse(null))
                .put("dateSale", LocalDateTime.now())
                .build();
    }

    private Optional<Integer> getEntityId(String title, String inputText, MessageMinMax messageMinMax) {
        return BASE_VIEW.getReadValueIntMinMax(title, inputText, messageMinMax);
    }

    private Optional<Integer> getInputPlayersCount() {
        return BASE_VIEW.getReadValueInt("Enter number of players: ");
    }

    protected Sale getUpdateSaleDetails(Sale sale, MessageMinMax playersList, MessageMinMax roomsList, MessageMinMax ticketsList) {
        sale.setIdPlayer(getUpdatePlayerId(playersList, sale.getIdPlayer()));
        sale.setIdRoom(getUpdateRoomId(roomsList, sale.getIdRoom()));
        sale.setIdTicket(getUpdateTicketId(ticketsList, sale.getIdTicket()));
        sale.setPlayers(getUpdatePlayers(sale.getPlayers()));
        sale.setActive(getUpdateIsActive(sale.isActive()));
        return sale;
    }

    private int getUpdatePlayerId(MessageMinMax message, int oldValue) {
        Optional<Integer> playerId = BASE_VIEW.getReadValueIntMinMax("List of Players", "Input Player ID: ", message);
        return playerId.orElse(oldValue);
    }


    private int getUpdateRoomId(MessageMinMax message, int oldValue) {
        Optional<Integer> roomId = BASE_VIEW.getReadValueIntMinMax("List of Rooms", "Input Room ID: ", message);
        return roomId.orElse(oldValue);
    }

    private int getUpdateTicketId(MessageMinMax message, int oldValue) {
        Optional<Integer> ticketId = BASE_VIEW.getReadValueIntMinMax("List of Tickets", "Input Ticket ID: ", message);
        return ticketId.orElse(oldValue);
    }

    private Boolean getUpdateIsActive(boolean oldValue) {
        return ConsoleUtils.readBooleanWithDefault("Enter is active ('Y' or 'N'): ", Optional.of(oldValue)).get();
    }

    private int getUpdatePlayers(int oldValue) {
        return ConsoleUtils.readIntWithDefault("Enter number of players: ", Optional.of(oldValue)).get();
    }


    protected void displayRecordSale(Sale sale) {
        String message = "";
        if (sale != null) {
            message += BASE_VIEW.LINE + "--- " + NAME_OBJECT + " Details ---" + BASE_VIEW.LINE;
            message += "ID: " + sale.getId() + BASE_VIEW.LINE;
            message += "Player ID: " + sale.getIdPlayer() + BASE_VIEW.LINE;
            message += "Room ID: " + sale.getIdRoom() + BASE_VIEW.LINE;
            message += "Ticket ID: " + sale.getIdTicket() + BASE_VIEW.LINE;
            message += "Players: " + sale.getPlayers() + BASE_VIEW.LINE;
            message += "Date Sale: " + sale.getDateSale() + BASE_VIEW.LINE;
            message += "Is Active: " + (sale.isActive() ? "Yes" : "No") + BASE_VIEW.LINE;
            message += "-------------------------" + BASE_VIEW.LINE;
        } else {
            message = NAME_OBJECT + " not found.";
        }
        BASE_VIEW.displayMessageln(message);
    }

    protected void displayActiveTickets(List<Ticket> tickets) {
        BASE_VIEW.displayMessageln("--- Available Tickets ---");
        if (tickets.isEmpty()) {
            BASE_VIEW.displayMessageln("No tickets available at the moment.");
            return;
        }

        tickets.forEach(ticket -> BASE_VIEW.displayMessageln(
                "ID: " + ticket.getId() +
                        ", Name: " + ticket.getName() +
                        ", Description: " + ticket.getDescription() +
                        ", Price: " + ticket.getPrice()
        ));
        BASE_VIEW.displayMessageln("-------------------------");
    }
}