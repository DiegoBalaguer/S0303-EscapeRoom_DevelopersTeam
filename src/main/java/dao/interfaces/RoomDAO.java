package dao.interfaces;

import dao.exceptions.DAOException;
import mvc.dto.InventoryDisplayDTO;
import mvc.model.Room;

import java.util.List;


public interface RoomDAO extends BaseDAO<Room, Integer> {
    double calculateTotalRoomValue() throws DAOException;
    List<InventoryDisplayDTO> findInventory() throws DAOException;
    }
