package dao.interfaces;

import dao.exceptions.DAOException;
import model.Certificate;

import java.util.List;

public interface CertificateDAO extends GenericDAO<Certificate, Integer> {
    List<Certificate> findByPlayerId(Integer playerId) throws DAOException;
    List<Certificate> findByRoomId(Integer roomId) throws DAOException;
}
