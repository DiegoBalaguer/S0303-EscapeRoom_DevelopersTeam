package view;

import dao.exceptions.DAOException;
import dto.SaleDetailsDTO;
import enums.OptionsMenuPlayer;
import loadConfigApp.LoadConfigApp;
import lombok.extern.slf4j.Slf4j;
import model.Player;
import model.Sale;
import utils.ConsoleUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
public class SaleView {

    public void displaySaleMenu(String title) {
        OptionsMenuPlayer.viewMenu(LoadConfigApp.getAppName());
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

    public void displaySale(Sale sale) {
        if (sale != null) {
            System.out.println("\n--- Sale Details ---");
            System.out.println("ID: " + sale.getId());
            System.out.println("Registration Date: " + sale.getDateSale());
            System.out.println("----------------------");
        } else {
            System.out.println("Player not found.");
        }
    }

    public void displaySales(List<Sale> sales) {
        if (sales.isEmpty()) {
            System.out.println("No players found.");
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