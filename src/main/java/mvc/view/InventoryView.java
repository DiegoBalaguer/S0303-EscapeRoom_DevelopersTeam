package mvc.view;

import mvc.dto.InventoryDisplayDTO;
import utils.ConsoleUtils;
import utils.StringUtils;

import java.util.List;

public class InventoryView {

    private static BaseView baseView = new BaseView();

    public void displayInventoryList(List<InventoryDisplayDTO> inventoryList, double totalPrice) {
        if (inventoryList.isEmpty()) {
            baseView.displayMessageln("The inventory is empty.");
            return;
        }
        baseView.displayMessageln("### INVENTORY LIST ###");
        baseView.displayMessageln(StringUtils.makeLineToList(inventoryList.get(0).toListHead()));

        inventoryList.forEach(inventory ->
                baseView.displayMessageln(StringUtils.makeLineToList(inventory.toList()))
        );

        baseView.displayMessage2ln("-------------------");
        baseView.displayMessage2ln("Total Price of Inventory: $" + String.format("%.2f", totalPrice));
    }

    public void noInventoryFound() {
        baseView.displayErrorMessage("No inventory data found.");
    }
}