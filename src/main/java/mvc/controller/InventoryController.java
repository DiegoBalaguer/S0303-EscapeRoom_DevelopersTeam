package mvc.controller;

import dao.exceptions.DAOException;
import dao.interfaces.RoomDAO;
import mvc.dto.InventoryDisplayDTO;
import mvc.view.BaseView;
import dao.factory.DAOFactory;
import java.util.List;

public class InventoryController {
        private RoomDAO roomDAO;
        private BaseView baseView;

        private static InventoryController inventoryControllerInstance;

        private InventoryController() {
            baseView = new BaseView();
            try {
                roomDAO = DAOFactory.getDAOFactory().getRoomDAO();
            } catch (Exception e) {
                throw new RuntimeException("Error initializing InventoryController: " + e.getMessage());
            }
        }

        public static InventoryController getInstance() {
            if (inventoryControllerInstance == null) {
                synchronized (InventoryController.class) {
                    if (inventoryControllerInstance == null) {
                        inventoryControllerInstance = new InventoryController();
                    }
                }
            }
            return inventoryControllerInstance;
        }

    public void showInventoryMenu() {
        try {
            baseView.displayMessage2ln("### INVENTORY MENU ###");
            baseView.displayMessage2ln("1. Show Full Inventory");
            baseView.displayMessage2ln("2. Exit");
            int option = baseView.getReadRequiredInt("Enter an option: ");

            switch (option) {
                case 1 -> showFullInventory();
                case 2 -> baseView.displayMessage2ln("Exiting the inventory menu...");
                default -> baseView.displayErrorMessage("Invalid option. Please try again.");
            }
        } catch (Exception e) {
            baseView.displayErrorMessage("An unexpected error occurred: " + e.getMessage());
        }
    }

    private void showFullInventory() {
        try {
            List<InventoryDisplayDTO> inventoryList = roomDAO.findInventory();

            if (inventoryList.isEmpty()) {
                baseView.displayMessageln("The inventory is empty.");
                return;
            }

            // Cabecera con tipo incluido
            baseView.displayMessageln("### FULL INVENTORY ###");
            baseView.displayMessageln("INVENTORY\tID\tNAME\tPRICE");

            for (InventoryDisplayDTO item : inventoryList) {
                String output = String.format(
                        "%s\t%d\t%s\t$%.2f",
                        item.getInventory(), // Mostrar el tipo del elemento (Room, Clue, Decoration)
                        item.getId(),
                        item.getName(),
                        item.getPrice()
                );
                baseView.displayMessageln(output);
            }
        } catch (DAOException e) {
            baseView.displayErrorMessage("Error retrieving inventory: " + e.getMessage());
        }
    }


}
