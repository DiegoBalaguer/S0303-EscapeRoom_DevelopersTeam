package dao.interfaces;

import dao.exceptions.DAOException;
import mvc.dto.ClueDisplayDTO;
import mvc.dto.InventoryDisplayDTO;
import mvc.model.Clue;
import mvc.model.Decoration;
import mvc.model.Room;

import java.util.List;


public interface RoomDAO extends BaseDAO<Room, Integer> {
    Room create(Room room) throws DAOException;

    double calculateTotalRoomValue() throws DAOException;

    void addClueToRoom(Integer roomId, Integer clueId) throws DAOException;

    void addDecorationToRoom(Integer roomId, Integer decorationId) throws DAOException;

    List<InventoryDisplayDTO> findInventory() throws DAOException;

    }
