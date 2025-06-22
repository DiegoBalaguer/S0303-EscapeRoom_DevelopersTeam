package dao.factory;

import dao.impl.*;
import dao.interfaces.*;


public class JdbcEscapeRoomDAO implements EscapeRoomDAO {

    @Override
    public RoomDAO getRoomDAO() {
        return new RoomDAOImpl();
    }

    @Override
    public PlayerDAO getPlayerDAO() {
        return new PlayerDAOImpl();
    }

}
