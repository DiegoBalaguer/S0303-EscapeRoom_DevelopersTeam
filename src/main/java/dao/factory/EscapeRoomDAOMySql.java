package dao.factory;

import dao.exceptions.DatabaseConnectionException;
import dao.impl.mysql.ConnectionDAOMySqlImpl;
import dao.interfaces.*;
import dao.impl.h2.*;

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
}
