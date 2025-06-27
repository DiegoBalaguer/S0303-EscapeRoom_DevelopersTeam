package dao.interfaces;

import dao.exceptions.DAOException;
import enums.Difficulty;
import model.Room;

import java.util.List;
import java.util.Optional;

public interface RoomDAO extends BaseDAO<Room, Integer> {
    public Room create(Room room) throws DAOException;
    double calculateTotalRoomValue() throws DAOException;
    void addClueToRoom(Integer roomId, Integer clueId) throws DAOException;
    void addDecorationToRoom(Integer roomId, Integer decorationId) throws DAOException;
    //Optional<Room> findByName(String name) throws DAOException;

}
