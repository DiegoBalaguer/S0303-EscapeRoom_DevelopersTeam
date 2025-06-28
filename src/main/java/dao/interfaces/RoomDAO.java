package dao.interfaces;

import dao.exceptions.DAOException;
import model.Room;


public interface RoomDAO extends BaseDAO<Room, Integer> {
    public Room create(Room room) throws DAOException;

    double calculateTotalRoomValue() throws DAOException;

    void addClueToRoom(Integer roomId, Integer clueId) throws DAOException;

    void addDecorationToRoom(Integer roomId, Integer decorationId) throws DAOException;
}
