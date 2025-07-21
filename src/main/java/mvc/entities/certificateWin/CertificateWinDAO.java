package mvc.entities.certificateWin;

import dao.exceptions.DAOException;
import dao.interfaces.BaseDAO;

import java.util.List;

public interface CertificateWinDAO extends BaseDAO<CertificateWin, Integer> {
    List<CertificateWinDisplayDTO> findByPlayerId(Integer playerId) throws DAOException;
}
