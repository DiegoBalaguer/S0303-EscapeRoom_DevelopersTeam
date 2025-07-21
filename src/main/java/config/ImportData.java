package config;

import dao.exceptions.DatabaseConnectionException;
import mongo.MongoDBConnection;
import mvc.entities.escapeRoom.EscapeRoom;

public class ImportData {

    public static void configDataEscapeRoom(EscapeRoom escapeRoom) {
        if (!LoadConfigDB.getMongodbEnable()) {
            escapeRoom.setId(LoadConfigApp.getAppBusinessId());
            escapeRoom.setName(LoadConfigApp.getAppBusinessName());
            escapeRoom.setAddress(LoadConfigApp.getAppBusinessAddress());
        } else {
            try {
                MongoDBConnection connection = MongoDBConnection.getInstance();
                escapeRoom.setId(connection.getEscapeRoomId().get());
                escapeRoom.setName(connection.getEscapeRoomName(escapeRoom.getId()));
                escapeRoom.setAddress(connection.getEscapeRoomAddress(escapeRoom.getId()));
            } catch (DatabaseConnectionException e) {
                throw new RuntimeException(e);
            }
        }
    }
}