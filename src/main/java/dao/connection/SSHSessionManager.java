package dao.connection;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import config.LoadConfigDB;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SSHSessionManager implements AutoCloseable {
    private static SSHSessionManager instance;
    private Session session;

    private SSHSessionManager() throws Exception {
        JSch jsch = new JSch();
        session = jsch.getSession(
                LoadConfigDB.getSshUser(),
                LoadConfigDB.getSshHost(),
                LoadConfigDB.getSshPort()
        );
        session.setPassword(LoadConfigDB.getSshPassword());
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        // Mongo
        session.setPortForwardingL(
                LoadConfigDB.getMongodbLocalPort(),
                LoadConfigDB.getMongodbHost(),
                LoadConfigDB.getMongodbRemotePort()
        );

        // MySQL
        session.setPortForwardingL(
                LoadConfigDB.getMySqlLocalPort(),
                LoadConfigDB.getMySqlHost(),
                LoadConfigDB.getMySqlRemotePort()
        );

        log.info("SSH session established with port forwardings for MongoDB and MySQL.");
    }

    public static synchronized SSHSessionManager getInstance() throws Exception {
        if (instance == null || !instance.isConnected()) {
            instance = new SSHSessionManager();
        }
        return instance;
    }

    public boolean isConnected() {
        return session != null && session.isConnected();
    }

    @Override
    public void close() {
        if (session != null && session.isConnected()) {
            session.disconnect();
            log.info("SSH Session Closed");
        }
        instance = null;
    }
}
