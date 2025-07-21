package interfaces;

import dao.exceptions.DatabaseConnectionException;
import dao.exceptions.NotFoundException;

public interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(String message) throws DatabaseConnectionException, NotFoundException;
}

