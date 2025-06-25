package dao.interfaces;

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
}
