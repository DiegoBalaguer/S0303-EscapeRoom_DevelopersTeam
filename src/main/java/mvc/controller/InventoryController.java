package mvc.controller;

import dao.exceptions.DAOException;
import dao.interfaces.RoomDAO;
import mvc.dto.InventoryDisplayDTO;
import mvc.view.BaseView;
import dao.factory.DAOFactory;
import utils.StringUtils;

import java.util.List;

public class InventoryController {
    private RoomDAO roomDAO;
    private BaseView baseView;

    private static InventoryController inventoryControllerInstance;

    private InventoryController() {
        baseView = BaseView.getInstance();
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
            int option = 0;
            do {
                baseView.displayMessage2ln("### INVENTORY MENU ###");
                baseView.displayMessageln("1. Show Full Inventory");
                baseView.displayMessage2ln("0. Exit");
                option = baseView.getReadRequiredInt("Enter an option: ");

                switch (option) {
                    case 1 -> showFullInventory();
                    case 0 -> baseView.displayMessage2ln("Exiting the inventory menu...");
                    default -> baseView.displayErrorMessage("Invalid option. Please try again.");
                }
            } while (option != 0);
        } catch (DAOException e) {
            baseView.displayErrorMessage("An unexpected error occurred: " + e.getMessage());
        }
    }

    private void showFullInventory() {
        try {
            List<InventoryDisplayDTO> inventoryList = getInventoryList();

            if (inventoryList.isEmpty()) {
                baseView.displayMessageln("The inventory is empty.");
                return;
            }
            displayListInventoryDTO(inventoryList);
        } catch (DAOException e) {
            baseView.displayErrorMessage("Error retrieving inventory: " + e.getMessage());
        }
    }

    private void displayListInventoryDTO(List<InventoryDisplayDTO> inventoryDisplayDTOS) {
        baseView.displayMessageln(
                StringUtils.makeLineToList(inventoryDisplayDTOS.getFirst().toListHead()));

        inventoryDisplayDTOS.forEach(inventoryItem -> baseView.displayMessageln(
                StringUtils.makeLineToList(inventoryItem.toList())));
        baseView.displayMessage2ln("-------------------");
    }

    private List<InventoryDisplayDTO> getInventoryList() {
        return roomDAO.findInventory();
    }
}
