import mvc.dto.InventoryDisplayDTO;
import utils.PairTextLength;

import java.util.List;

public class InventoryView {

    public void displayInventory(List<InventoryDisplayDTO> inventoryList) {
        if (inventoryList.isEmpty()) {
            System.out.println("The inventory is empty.");
            return;
        }

        InventoryDisplayDTO example = new InventoryDisplayDTO();
        example.toListHead().forEach(pair ->
                System.out.print(String.format("%-" + pair.getLength() + "s", pair.getText()))
        );
        System.out.println();

        inventoryList.forEach(item -> {
            item.toList().forEach(pair ->
                    System.out.print(String.format("%-" + pair.getLength() + "s", pair.getText()))
            );
            System.out.println();
        });
    }
}