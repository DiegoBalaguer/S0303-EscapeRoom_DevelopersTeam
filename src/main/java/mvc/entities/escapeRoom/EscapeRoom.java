package mvc.entities.escapeRoom;

import dao.exceptions.DatabaseConnectionException;
import dao.exceptions.NotFoundException;
import mvc.entities.ServiceManager;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import interfaces.Observer;
import interfaces.Observable;

@Slf4j
@Data
public class EscapeRoom implements Observable {
    private static EscapeRoom instance;
    private String id;
    private String name;
    private String address;
    private final EscapeRoomService ESCAPE_ROOM_SERVICE;


    private EscapeRoom (EscapeRoomService escapeRoomService) {
        this.ESCAPE_ROOM_SERVICE = escapeRoomService;
    }

    public static EscapeRoom getInstance() throws DatabaseConnectionException {
        if (instance == null) {
            synchronized (EscapeRoom.class) {
                if (instance == null) {
                    EscapeRoomService service = ServiceManager.getInstance().getEscapeRoomService();
                     instance = new EscapeRoom(service);
                }
            }
        }
        return instance;
    }

    @Override
    public void addObserver(Observer observer) {
        throw new UnsupportedOperationException("Observers are managed dynamically using the database.");
    }

    @Override
    public void removeObserver(Observer observer) {
        throw new UnsupportedOperationException("Observers are managed dynamically using the database.");
    }

    @Override
    public void notifyObservers(String message) throws DatabaseConnectionException, NotFoundException {
        ESCAPE_ROOM_SERVICE.notifyObservers(message);

    }

}
