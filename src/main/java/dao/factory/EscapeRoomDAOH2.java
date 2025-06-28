package dao.factory;

import dao.exceptions.DatabaseConnectionException;
import dao.impl.h2.*;
import dao.interfaces.*;


public class EscapeRoomDAOH2 implements EscapeRoomDAO {
    private final ConnectionDAOH2Impl connectionDAO;


    public EscapeRoomDAOH2() throws DatabaseConnectionException {
        this.connectionDAO = ConnectionDAOH2Impl.getInstance();
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
}
