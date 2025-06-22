package dao.interfaces;

import dao.exceptions.DAOException;
import enums.Difficulty;
import model.Room;

import java.util.List;

public interface RoomDAO extends GenericDAO<Room, Integer> {
    List<Room> findByDifficulty(Difficulty difficulty) throws DAOException;
    List<Room> findByTheme(String theme) throws DAOException;
    List<Room> findRoomsWithPriceRange(double minPrice, double maxPrice) throws DAOException;
    double calculateTotalRoomValue() throws DAOException;
    void addClueToRoom(Integer roomId, Integer clueId) throws DAOException;
    void addDecorationToRoom(Integer roomId, Integer decorationId) throws DAOException;
}
