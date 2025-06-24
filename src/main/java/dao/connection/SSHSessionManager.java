package dao.connection;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import dao.loadConfigDB.LoadConfigDB;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SSHSessionManager implements AutoCloseable {
    private Session session;

    public SSHSessionManager() throws Exception {
        JSch jsch = new JSch();
        session = jsch.getSession(
                LoadConfigDB.getSshUser(),
                LoadConfigDB.getSshHost(),
                LoadConfigDB.getSshPort()
        );
        session.setPassword(LoadConfigDB.getSshPassword());
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        session.setPortForwardingL(
                LoadConfigDB.getSshLocalPort(),
                LoadConfigDB.getSshRemoteHost(),
                LoadConfigDB.getSshRemotePort()
        );
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
    }
}
