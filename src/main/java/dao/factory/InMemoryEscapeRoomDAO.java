package dao.factory;

import dao.impl.*;
import dao.interfaces.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InMemoryEscapeRoomDAO implements EscapeRoomDAO {
    private static final InMemoryEscapeRoomDAO INSTANCE = new InMemoryEscapeRoomDAO();
    
    private final RoomDAO roomDAO = new RoomDAOImpl();
    private final PlayerDAO playerDAO = new PlayerDAOImpl();

    public static InMemoryEscapeRoomDAO getInstance() {
        return INSTANCE;
    }
}
