package mvc.dto;

import mvc.model.Sale;

public class SaleMapper {
    public static SaleDisplayDTO toDisplayDTO(Sale sale) {
        return SaleDisplayDTO.builder()
                .id(sale.getId())
                .idTicket(sale.getIdTicket())
                .idPlayer(sale.getIdPlayer())
                .idRoom(sale.getIdRoom())
                .players(sale.getPlayers())
                .price(sale.getPrice())
                .completion(sale.getCompletion())
                .dateSale(sale.getDateSale())
                .isActive(sale.isActive())
                .ticketName("")
                .playerName("")
                .roomName("")
                .build();
    }
}
