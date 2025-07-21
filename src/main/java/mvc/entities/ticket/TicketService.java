package mvc.entities.ticket;

import dao.exceptions.DAOException;
import mvc.entities.MessageMinMax;
import utils.StringUtils;

import java.util.List;
import java.util.Optional;

public class TicketService {

    private final TicketDAO TICKET_DAO;
    private static final String NAME_OBJECT = "Ticket";

    public TicketService(TicketDAO ticketDAO) {
        this.TICKET_DAO = ticketDAO;
    }

    public Optional<Ticket> getTicketById(int id) {
        return TICKET_DAO.findById(id);
    }

    public MessageMinMax getAllTickets() throws DAOException {
        List<Ticket> tickets = TICKET_DAO.findAll();
        return new MessageMinMax(displayListTickets(tickets), tickets.size() > 0 ? 1 : 0, tickets.size());
    }

    public String displayListTickets(List<Ticket> tickets) {
        StringBuilder message = new StringBuilder();
        if (tickets.isEmpty()) {
            return "No " + NAME_OBJECT + " found.";
        }
        message.append(
                StringUtils.makeLineToList(TicketMapper.toDisplayDTO(tickets.getFirst()).toListHead())).append(System.lineSeparator());

        tickets.forEach(ticket -> message.append(
                StringUtils.makeLineToList(TicketMapper.toDisplayDTO(ticket).toList())).append(System.lineSeparator()));
        message.append("-------------------").append(System.lineSeparator());
        return message.toString();
    }
}