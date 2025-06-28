package dao.interfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionDAOsql {
    public void beginTransaction() throws SQLException;

    public void commitTransaction() throws SQLException;

    public void rollbackTransaction() throws SQLException;
}
