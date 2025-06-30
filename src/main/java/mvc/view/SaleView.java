package mvc.view;

import mvc.enumsMenu.OptionsMenuSale;
import mvc.model.Sale;
import utils.ConsoleUtils;
import java.util.List;
import java.util.Optional;

public class SaleView {

    private final BaseView baseView = new BaseView();

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
            baseView.displayMessageln("No sales found.");
            return;
        }
        baseView.displayMessageln("--- All Sales ---");
        sales.forEach(sale -> baseView.displayMessage(
                "ID: " + sale.getId() +
                        ", Reg. Date: " + sale.getDateSale()
        ));
        baseView.displayMessage2ln("-------------------");
    }
}