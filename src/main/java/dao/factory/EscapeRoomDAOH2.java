package dao.factory;

import dao.impl.h2.*;
import dao.interfaces.*;


public class EscapeRoomDAOH2 implements EscapeRoomDAO {

    @Override
    public ConnectionDAO getConnectionDAO() {
        return ConnectionDAOH2Impl.getInstance();
    }

    @Override
    public PlayerDAO getPlayerDAO() {
        return new PlayerDAOH2Impl();
    }

    @Override
    public RoomDAO getRoomDAO() {
        return new RoomDAOH2Impl();
    }

    @Override
    public ClueDAO getClueDAO() {
        return new ClueDAOH2Impl();
    }

    @Override
    public DecorationDAO getDecorationDAO() {
        return new DecorationDAOH2Impl();
    }

    @Override
    public CertificateDAO getCertificateDAO() {
        return new CertificateDAOH2Impl();
    }

    @Override
    public RewardDAO getRewardDAO() {
        return new RewardDAOH2Impl();
    }

    @Override
    public TicketDAO getTicketDAO() {
        return new TicketDAOH2Impl();
    }

    @Override
    public SaleDAO getSaleDAO() {
        return new SaleDAOH2Impl();
    }

    @Override
    public CertificateWinDAO getCertificateWinDAO() {
        return new CertificateWinDAOH2Impl();
    }

    @Override
    public RewardWinDAO getRewardWinDAO() {
        return new RewardWinDAOH2Impl();
    }
}
