package dao.interfaces;

import java.sql.Connection;

public interface ConnectionDAO {
    Connection getConnection();


    void closeConnection();

}
