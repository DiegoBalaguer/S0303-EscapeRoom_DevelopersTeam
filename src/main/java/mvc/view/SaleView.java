package mvc.view;

import mvc.enumsMenu.OptionsMenuSale;
import config.loadConfigApp.LoadConfigApp;
import lombok.extern.slf4j.Slf4j;
import mvc.model.Sale;
import utils.ConsoleUtils;
import java.util.List;
import java.util.Optional;

@Slf4j
public class SaleView {

    public void displaySaleMenu(String title) {
        OptionsMenuSale.viewMenu(LoadConfigApp.getAppName());
    }

    public Optional<Integer> getSaleId() {
        try {
            int id = ConsoleUtils.readRequiredInt("Enter sale ID: ");
            return Optional.of(id);
        } catch (NumberFormatException e) {
            displayErrorMessage("Invalid ID. Please enter a number.");
            return Optional.empty();
        }
    }

    public void displaySales(List<Sale> sales) {
        if (sales.isEmpty()) {
            System.out.println("No sales found.");
            return;
        }
        System.out.println("\n--- All Sales ---");
        sales.forEach(sale -> System.out.println(
                "ID: " + sale.getId() +
                        ", Reg. Date: " + sale.getDateSale()
        ));
        System.out.println("-------------------");
    }

    public void displayMessage(String message) {
        System.out.println(System.lineSeparator() + message + System.lineSeparator());
    }

    public void displayErrorMessage(String message) {
        System.err.println(System.lineSeparator() + "ERROR: " + message + System.lineSeparator());
    }
}