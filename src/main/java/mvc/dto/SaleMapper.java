package mvc.dto;

import mvc.model.Sale;

public class SaleMapper {
    public static SaleDisplayDTO toDisplayDTO(Sale sale) {
        return new SaleDisplayDTO(
                sale.getId(),
                sale.getIdTicket(),
                sale.getIdPlayer(),
                sale.getIdRoom(),
                sale.getPlayers(),
                sale.getPrice(),
                sale.getCompletion(),
                sale.getDateSale(),
                sale.isActive()
        );
    }
}