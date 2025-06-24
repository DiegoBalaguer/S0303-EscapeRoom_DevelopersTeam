package dao.impl;

import dao.exceptions.DAOException;
import dao.interfaces.BaseDAO;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public abstract class BaseDAOImpl<T, I> implements BaseDAO<T, I> {
    protected final Map<I, T> entities = new HashMap<>();
    protected AtomicInteger idSequence = new AtomicInteger(1);

    private static final String DELETING_ENTITY = "Deleting entity with id: {}";
    private static final String FINDING_ENTITY = "Finding entity with id: {}";
    private static final String FINDING_ALL = "Finding all entities";

    @Override
    public Optional<T> findById(I id) throws DAOException {
        log.debug(FINDING_ENTITY, id);
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public List<T> findAll() throws DAOException {
        log.debug(FINDING_ALL);
        return new ArrayList<>(entities.values());
    }

    @Override
    public void deleteById(I id) throws DAOException {
        log.debug(DELETING_ENTITY, id);
        entities.remove(id);
    }

    protected abstract I getEntityId(T entity);
    protected abstract void setEntityId(T entity, I id);
}