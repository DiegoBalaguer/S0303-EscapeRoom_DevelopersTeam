package mvc.entities.room;

import dao.exceptions.DAOException;
import dao.interfaces.BaseDAO;
import inventory.InventoryDisplayDTO;

import java.util.List;


public interface RoomDAO extends BaseDAO<Room, Integer> {
    double calculateTotalRoomValue() throws DAOException;
    List<InventoryDisplayDTO> findInventory() throws DAOException;
    }
