package dao.factory;

import mvc.entities.decoration.DecorationDAOH2Impl;
import mvc.entities.escapeRoom.EscapeRoomDAO;
import mvc.entities.certificate.CertificateDAO;
import mvc.entities.certificate.CertificateDAOH2Impl;
import mvc.entities.certificateWin.CertificateWinDAO;
import mvc.entities.certificateWin.CertificateWinDAOH2Impl;
import mvc.entities.clue.ClueDAO;
import dao.exceptions.DatabaseConnectionException;
import dao.connections.ConnectionDAOMySqlImpl;
import mvc.entities.decoration.DecorationDAO;
import mvc.entities.clue.ClueDAOH2Impl;
import mvc.entities.notification.NotificationDAO;
import mvc.entities.notification.NotificationDAOH2Impl;
import mvc.entities.player.PlayerDAO;
import mvc.entities.player.PlayerDAOH2Impl;
import mvc.entities.reward.RewardDAO;
import mvc.entities.reward.RewardDAOH2Impl;
import mvc.entities.rewardWin.RewardWinDAO;
import mvc.entities.rewardWin.RewardWinDAOH2Impl;
import mvc.entities.room.RoomDAO;
import mvc.entities.room.RoomDAOH2Impl;
import mvc.entities.sale.SaleDAO;
import mvc.entities.sale.SaleDAOH2Impl;
import mvc.entities.ticket.TicketDAO;
import mvc.entities.ticket.TicketDAOH2Impl;

public class EscapeRoomDAOMySql implements EscapeRoomDAO {
    private final ConnectionDAOMySqlImpl connectionDAO;


    public EscapeRoomDAOMySql() throws DatabaseConnectionException {
        this.connectionDAO = ConnectionDAOMySqlImpl.getInstance();
    }

    @Override
    public void closeConnection() {
        if (connectionDAO != null) {
            connectionDAO.closeConnection();
        }}

    @Override
    public PlayerDAO getPlayerDAO() {
        return new PlayerDAOH2Impl(connectionDAO);
    }

    @Override
    public RoomDAO getRoomDAO() {
        return new RoomDAOH2Impl(connectionDAO);
    }

    @Override
    public ClueDAO getClueDAO() {
        return new ClueDAOH2Impl(connectionDAO);
    }

    @Override
    public DecorationDAO getDecorationDAO() {
        return new DecorationDAOH2Impl(connectionDAO);
    }

    @Override
    public CertificateDAO getCertificateDAO() {
        return new CertificateDAOH2Impl(connectionDAO);
    }

    @Override
    public RewardDAO getRewardDAO() {
        return new RewardDAOH2Impl(connectionDAO);
    }

    @Override
    public TicketDAO getTicketDAO() {
        return new TicketDAOH2Impl(connectionDAO);
    }

    @Override
    public SaleDAO getSaleDAO() {
        return new SaleDAOH2Impl(connectionDAO);
    }

    @Override
    public CertificateWinDAO getCertificateWinDAO() {
        return new CertificateWinDAOH2Impl(connectionDAO);
    }

    @Override
    public RewardWinDAO getRewardWinDAO() {
        return new RewardWinDAOH2Impl(connectionDAO);
    }

    @Override
    public NotificationDAO getNotificationDAO() {
        return new NotificationDAOH2Impl(connectionDAO);
    }
}
