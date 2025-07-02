package mvc.view;

import mvc.dto.PlayerMapper;
import mvc.dto.SaleMapper;
import mvc.model.Sale;
import mvc.model.Ticket;
import utils.ConsoleUtils;
import utils.StringUtils;

import java.util.List;
import java.util.Optional;

public class SaleView {

    private final BaseView baseView = new BaseView();
    private static final String NAME_OBJECT = "Sale";

    public Optional<Integer> getSaleId() {
        try {
            int id = ConsoleUtils.readRequiredInt("Enter sale ID: ");
            return Optional.of(id);
        } catch (NumberFormatException e) {
            baseView.displayErrorMessage("Invalid ID. Please enter a number.");
            return Optional.empty();
        }
    }

    public void displayListSales(List<Sale> sales) {
        if (sales.isEmpty()) {
            System.out.println("No " + NAME_OBJECT + "s found.");
            return;
        }
        baseView.displayMessageln(
                StringUtils.makeLineToList(SaleMapper.toDisplayDTO(sales.getFirst()).toListHead()));

        sales.forEach(sale -> baseView.displayMessageln(
                StringUtils.makeLineToList(SaleMapper.toDisplayDTO(sale).toList())));
        baseView.displayMessage2ln("-------------------");
    }
    public void displayActiveTickets(List<Ticket> tickets) {
        baseView.displayMessageln("--- Available Tickets ---");
        if (tickets.isEmpty()) {
            baseView.displayMessageln("No tickets available at the moment.");
            return;
        }

        tickets.forEach(ticket -> baseView.displayMessageln(
                "ID: " + ticket.getId() +
                        ", Name: " + ticket.getName() +
                        ", Description: " + ticket.getDescription() +
                        ", Price: " + ticket.getPrice()
        ));
        baseView.displayMessageln("-------------------------");
    }
   /* public void displayRecordSale(Sale sale) {
        String message = "";
        if (sale != null) {
            message += "--- Sale Details ---" + baseView.LINE;
            message += "ID: " + sale.getId() + baseView.LINE;
            message += "Ticket ID: " + sale.getIdTicket() + baseView.LINE;
            message += "Player ID: " + sale.getIdPlayer() + baseView.LINE;
            message += "Room ID: " + sale.getIdRoom() + baseView.LINE;
            message += "Players: " + sale.getPlayers() + baseView.LINE;
            message += "Price: " + sale.getPrice() + baseView.LINE;
            message += "Completion: " + sale.getCompletion() + "%" + baseView.LINE;
            message += "Sale Date: " + sale.getDateSale() + baseView.LINE;
            message += "Active: " + (sale.isActive() ? "Yes" : "No") + baseView.LINE;
            message += "----------------------" + baseView.LINE;
        } else {
            message = "Sale not found.";
        }
        baseView.displayMessageln(message);
    }

    public int getTicketId() {
        return ConsoleUtils.readRequiredInt("Enter the ticket ID you want to purchase: ");
    }
*/

}