package mvc.dto;

import mvc.model.Ticket;

public class TicketMapper {
    public static TicketDisplayDTO toDisplayDTO(Ticket ticket) {
        return TicketDisplayDTO.builder()
                .id(ticket.getId())
                .name(ticket.getName())
                .price(ticket.getPrice())
                .description(ticket.getDescription())
                .isActive(ticket.isActive())
                .build();
    }
}

