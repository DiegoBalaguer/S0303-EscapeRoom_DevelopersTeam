package dao.interfaces;

import dao.exceptions.DAOException;
import enums.Difficulty;
import model.Room;

import java.util.List;

public interface RoomDAO extends BaseDAO<Room, Integer> {
    double calculateTotalRoomValue() throws DAOException;
    void addClueToRoom(Integer roomId, Integer clueId) throws DAOException;
    void addDecorationToRoom(Integer roomId, Integer decorationId) throws DAOException;
}
