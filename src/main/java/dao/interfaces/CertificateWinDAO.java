package dao.interfaces;

import dao.exceptions.DAOException;
import mvc.dto.CertificateWinDisplayDTO;
import mvc.model.CertificateWin;

import java.util.List;

public interface CertificateWinDAO extends BaseDAO<CertificateWin, Integer> {
    List<CertificateWinDisplayDTO> findByPlayerId(Integer playerId) throws DAOException;
}
