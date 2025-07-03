package mvc.view;

//import mvc.dto.SaleMapper;
import mvc.dto.CertificateWinDisplayDTO;
import mvc.dto.PlayerMapper;
import mvc.dto.SaleDisplayDTO;
import mvc.dto.SaleMapper;
import mvc.model.Player;
import mvc.model.Sale;
import mvc.model.Ticket;
import utils.ConsoleUtils;
import utils.StringUtils;

import java.util.List;
import java.util.Optional;

public class SaleView {

    private final BaseView baseView = BaseView.getInstance();
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

    public void displayListSaleDTO(List<SaleDisplayDTO> saleDisplayDTOS) {
        if (saleDisplayDTOS.isEmpty()) {
            baseView.displayMessageln("No " + NAME_OBJECT + "s found.");
            return;
        }
        baseView.displayMessageln(
                StringUtils.makeLineToList(saleDisplayDTOS.getFirst().toListHead()));

        saleDisplayDTOS.forEach(certificateWins -> baseView.displayMessageln(
                StringUtils.makeLineToList(certificateWins.toList())));
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
}