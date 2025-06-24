package dao.interfaces;

import dao.exceptions.DAOException;

import java.util.List;
import java.util.Optional;

public interface BaseDAO<T, ID> {
    T create(T entity) throws DAOException;

    Optional<T> findById(ID id) throws DAOException;

    List<T> findAll() throws DAOException;

    T update(T entity) throws DAOException;

    void deleteById(ID id) throws DAOException;

    boolean isExistsById(ID id) throws DAOException;
}

