package dao.interfaces;

import dao.exceptions.DAOException;
import dto.CertificateWinDisplayDTO;
import model.CertificateWin;

import java.util.List;

public interface CertificateWinDAO extends BaseDAO<CertificateWin, Integer> {
    List<CertificateWinDisplayDTO> findByPlayerId(Integer playerId) throws DAOException;
}
