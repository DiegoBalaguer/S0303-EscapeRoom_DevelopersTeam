package dao.interfaces;

import dao.exceptions.DAOException;
import mvc.model.Certificate;

import java.util.List;

public interface CertificateDAO extends BaseDAO<Certificate, Integer> {
    List<Certificate> findByPlayerId(Integer playerId) throws DAOException;
    List<Certificate> findByRoomId(Integer roomId) throws DAOException;
}
