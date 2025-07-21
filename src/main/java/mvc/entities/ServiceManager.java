package mvc.entities;

import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import lombok.Data;
import mvc.entities.certificate.CertificateDAO;
import mvc.entities.certificate.CertificateService;
import mvc.entities.certificateWin.CertificateWinDAO;
import mvc.entities.certificateWin.CertificateWinService;
import mvc.entities.clue.ClueDAO;
import mvc.entities.clue.ClueService;
import mvc.entities.decoration.DecorationDAO;
import mvc.entities.decoration.DecorationService;
import mvc.entities.escapeRoom.EscapeRoomService;
import mvc.entities.notification.NotificationDAO;
import mvc.entities.notification.NotificationService;
import mvc.entities.player.PlayerDAO;
import mvc.entities.player.PlayerService;
import mvc.entities.player.playerNotify.PlayerNotifyService;
import mvc.entities.reward.RewardDAO;
import mvc.entities.reward.RewardService;
import mvc.entities.rewardWin.RewardWinDAO;
import mvc.entities.rewardWin.RewardWinService;
import mvc.entities.room.RoomDAO;
import mvc.entities.room.RoomService;
import mvc.entities.sale.SaleDAO;
import mvc.entities.sale.SaleService;
import mvc.entities.ticket.TicketDAO;
import mvc.entities.ticket.TicketService;

@Data
public class ServiceManager {

    private static ServiceManager instance;

    private final CertificateService certificateService;
    private final CertificateWinService certificateWinService;
    private final ClueService clueService;
    private final DecorationService decorationService;
    private final EscapeRoomService escapeRoomService;
    private final NotificationService notificationService;
    private final PlayerService playerService;
    private final PlayerNotifyService playerNotifyService;
    private final RewardService rewardService;
    private final RewardWinService rewardWinService;
    private final RoomService roomService;
    private final SaleService saleService;
    private final TicketService ticketService;

    private ServiceManager() throws DatabaseConnectionException {
        CertificateDAO certificateDAO = DAOFactory.getDAOFactory().getCertificateDAO();
        CertificateWinDAO certificateWinDAO = DAOFactory.getDAOFactory().getCertificateWinDAO();
        ClueDAO clueDAO = DAOFactory.getDAOFactory().getClueDAO();
        DecorationDAO decorationDAO = DAOFactory.getDAOFactory().getDecorationDAO();
        NotificationDAO notificationDAO = DAOFactory.getDAOFactory().getNotificationDAO();
        PlayerDAO playerDAO = DAOFactory.getDAOFactory().getPlayerDAO();
        RewardDAO rewardDAO = DAOFactory.getDAOFactory().getRewardDAO();
        RewardWinDAO rewardWinDAO = DAOFactory.getDAOFactory().getRewardWinDAO();
        RoomDAO roomDAO = DAOFactory.getDAOFactory().getRoomDAO();
        SaleDAO saleDAO = DAOFactory.getDAOFactory().getSaleDAO();
        TicketDAO ticketDAO = DAOFactory.getDAOFactory().getTicketDAO();

        roomService = new RoomService(roomDAO);

        certificateService = new CertificateService(certificateDAO);
        clueService = new ClueService(clueDAO, roomService);
        decorationService = new DecorationService(decorationDAO, roomService);

        roomService.setClueService(clueService);
        roomService.setDecorationService(decorationService);

        playerService = new PlayerService(playerDAO);
        rewardService = new RewardService(rewardDAO);

        notificationService = new NotificationService(notificationDAO, playerService);
        escapeRoomService = new EscapeRoomService(notificationService);
        certificateWinService = new CertificateWinService(certificateWinDAO, playerService, roomService, certificateService, notificationService);
        rewardWinService = new RewardWinService(rewardWinDAO, playerService, rewardService, notificationService);
        playerNotifyService = new  PlayerNotifyService(playerService, notificationService);
        ticketService = new TicketService(ticketDAO);
        saleService = new SaleService(saleDAO, playerService, roomService, ticketService);
    }

    public static synchronized ServiceManager getInstance() throws DatabaseConnectionException {
        if (instance == null) {
            instance = new ServiceManager();
        }
        return instance;
    }
}
