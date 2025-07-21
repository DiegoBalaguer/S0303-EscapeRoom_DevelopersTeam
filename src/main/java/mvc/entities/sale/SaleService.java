package mvc.entities.sale;

import dao.exceptions.DAOException;
import dao.exceptions.NotFoundException;
import mvc.entities.Inputs;
import mvc.entities.MessageMinMax;
import mvc.entities.player.PlayerService;
import mvc.entities.room.RoomService;
import mvc.entities.ticket.Ticket;
import mvc.entities.ticket.TicketService;
import utils.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class SaleService {

    private final SaleDAO SALE_DAO;
    private final PlayerService PLAYER_SERVICE;
    private final RoomService ROOM_SERVICE;
    private final TicketService TICKET_SERVICE;
    private static final String NAME_OBJECT = "Sale";

    public SaleService(SaleDAO saleDAO, PlayerService playerService, RoomService roomService, TicketService ticketService) {
        this.SALE_DAO = saleDAO;
        this.PLAYER_SERVICE = playerService;
        this.ROOM_SERVICE = roomService;
        this.TICKET_SERVICE = ticketService;
    }

    public Sale createSale(Inputs inputsValue) throws IllegalArgumentException, DAOException, NotFoundException {
        Optional<Integer> playerId = inputsValue.getFieldAs("playerId", Integer.class);
        Optional<Integer> roomId = inputsValue.getFieldAs("roomId", Integer.class);
        Optional<Integer> ticketId = inputsValue.getFieldAs("ticketId", Integer.class);
        Optional<Integer> playersCount = inputsValue.getFieldAs("playersCount", Integer.class);
        Optional<LocalDateTime> dateSale = inputsValue.getFieldAs("dateSale", LocalDateTime.class);
        Optional<Ticket> ticket = TICKET_SERVICE.getTicketById(ticketId.get());
        
        if (playerId.isEmpty() || PLAYER_SERVICE.getPlayerById(playerId.get()).isEmpty()) {
            throw new NotFoundException("Player with ID " + playerId.get() + " not found.");
        }
        if (roomId.isEmpty() || ROOM_SERVICE.getRoomById(roomId.get()).isEmpty()) {
            throw new NotFoundException("Room with ID " + roomId.get() + " not found.");
        }
        if (ticketId.isEmpty() || TICKET_SERVICE.getTicketById(ticketId.get()).isEmpty()) {
            throw new NotFoundException("Sale with ID " + ticketId.get() + " not found.");
        }
        
        BigDecimal totalPrice = ticket.get().getPrice().multiply(BigDecimal.valueOf(playersCount.get()));
        
        Sale newSale = Sale.builder()
                .idPlayer(playerId.get())
                .idRoom(roomId.get())
                .idTicket(ticketId.get())
                .players(playersCount.get())
                .price(totalPrice)
                .completion(0)
                .dateSale(dateSale.get())
                .isActive(true)
                .build();
        
        return SALE_DAO.create(newSale);
    }

    public Optional<Sale> getSaleById(int id) throws DAOException {
        if (id <= 0) {
            throw new IllegalArgumentException(NAME_OBJECT + " ID must be positive.");
        }
        return SALE_DAO.findById(id);
    }

    public MessageMinMax getAllSales() throws DAOException {
        List<SaleDisplayDTO> sales = SALE_DAO.findAllCompleteInfo();
        return new MessageMinMax(displayListSalesDTO(sales), sales.size() > 0 ? 1 : 0, sales.size());
    }

    public Sale updateSale(Sale sale) throws IllegalArgumentException, NotFoundException, DAOException {
        if (sale == null || sale.getId() <= 0) {
            throw new IllegalArgumentException("The " + NAME_OBJECT + " or its ID is not valid for the update.");
        }
        if (SALE_DAO.findById(sale.getId()).isEmpty()) {
            throw new NotFoundException(NAME_OBJECT + " with ID " + sale.getId() + " not found for update.");
        }
        return SALE_DAO.update(sale);
    }

    public void deleteSale(int id) throws NotFoundException, DAOException {
        if (SALE_DAO.findById(id).isEmpty()) {
            throw new NotFoundException(NAME_OBJECT + " with ID " + id + " not found for deletion.");
        }
        SALE_DAO.deleteById(id);
    }

    public Sale softDeleteSale(int id) throws NotFoundException, DAOException {
        Optional<Sale> existingSaleOpt = SALE_DAO.findById(id);
        if (existingSaleOpt.isEmpty()) {
            throw new NotFoundException(NAME_OBJECT + " with ID " + id + " not found for soft deletion.");
        }
        Sale saleToSoftDelete = existingSaleOpt.get();
        saleToSoftDelete.setActive(false);
        return SALE_DAO.update(saleToSoftDelete);
    }

    private String displayListSales(List<Sale> sales) {
        StringBuilder message = new StringBuilder();
        if (sales.isEmpty()) {
            return "No " + NAME_OBJECT + "s found.";
        }
        message.append(
                StringUtils.makeLineToList(SaleMapper.toDisplayDTO(sales.getFirst()).toListHead())).append(System.lineSeparator());

        sales.forEach(sale -> message.append(
                StringUtils.makeLineToList(SaleMapper.toDisplayDTO(sale).toList())).append(System.lineSeparator()));

        message.append("-------------------").append(System.lineSeparator());
        return message.toString();
    }

    public String displayListSalesDTO(List<SaleDisplayDTO> saleDisplayDTOS) {
        StringBuilder message = new StringBuilder();
        if (saleDisplayDTOS.isEmpty()) {
            return "No " + NAME_OBJECT + "s found.";
        }
        message.append(
                StringUtils.makeLineToList(saleDisplayDTOS.getFirst().toListHead())).append(System.lineSeparator());

        saleDisplayDTOS.forEach(certificateWins -> message.append(
                StringUtils.makeLineToList(certificateWins.toList())).append(System.lineSeparator()));
        message.append("-------------------").append(System.lineSeparator());
        return message.toString();
    }

    public Double calculateTotalBenefits() {
        List<Sale> sales = SALE_DAO.findAll();
        return sales.stream()
                .mapToDouble(sale -> sale.getPrice().doubleValue())
                .sum();
    }

    protected MessageMinMax getAllPlayers() {
        return PLAYER_SERVICE.getAllPlayers();
    }

    protected MessageMinMax getAllRooms() {
        return ROOM_SERVICE.getAllRooms();
    }

    protected MessageMinMax getAllTickets() {
        return TICKET_SERVICE.getAllTickets();
    }
}
