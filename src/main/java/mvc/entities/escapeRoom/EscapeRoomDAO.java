package mvc.entities.escapeRoom;

import mvc.entities.certificate.CertificateDAO;
import mvc.entities.certificateWin.CertificateWinDAO;
import mvc.entities.clue.ClueDAO;
import mvc.entities.decoration.DecorationDAO;
import mvc.entities.notification.NotificationDAO;
import mvc.entities.notification.NotificationDAOH2Impl;
import mvc.entities.player.PlayerDAO;
import mvc.entities.reward.RewardDAO;
import mvc.entities.rewardWin.RewardWinDAO;
import mvc.entities.room.RoomDAO;
import mvc.entities.sale.SaleDAO;
import mvc.entities.ticket.TicketDAO;

public interface EscapeRoomDAO {
    PlayerDAO getPlayerDAO();
    RoomDAO getRoomDAO();
    ClueDAO getClueDAO();
    DecorationDAO getDecorationDAO();
    CertificateDAO getCertificateDAO();
    RewardDAO getRewardDAO();
    TicketDAO getTicketDAO();
    SaleDAO getSaleDAO();
    CertificateWinDAO getCertificateWinDAO();
    RewardWinDAO getRewardWinDAO();
    NotificationDAO getNotificationDAO();
    void closeConnection();
}
