package mvc.entities.sale;

import dao.exceptions.DAOException;
import dao.interfaces.BaseDAO;

import java.util.List;

public interface SaleDAO  extends BaseDAO<Sale, Integer> {
    List<SaleDisplayDTO> findAllCompleteInfo() throws DAOException;
}
