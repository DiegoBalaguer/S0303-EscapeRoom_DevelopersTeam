package mvc.entities.ticket;

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

