package dao.interfaces;

import dao.exceptions.DAOException;
import mvc.dto.SaleDisplayDTO;
import mvc.model.Sale;
import java.util.List;

public interface SaleDAO  extends BaseDAO<Sale, Integer> {
    List<SaleDisplayDTO> findAllCompleteInfo() throws DAOException;
}
