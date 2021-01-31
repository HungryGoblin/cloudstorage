import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import filesystem.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientSetting {

    private static final String DEF_SYNC_DIR = "client\\src\\main\\resources\\Sync";
    private static final String DEF_LOGIN = "";
    private static final String DEF_HOST = "localhost";
    private static final int DEF_PORT = 8189;
    private static final int DEF_RATE = 50;
    private static final Logger LOG = LoggerFactory.getLogger(ClientSetting.class);

    private static Path syncPath;

    private static String login = DEF_LOGIN;
    private static String host = DEF_HOST;
    private static int port = DEF_PORT;
    private static int rate = DEF_RATE;

    public static Path getSyncPath() {
        return syncPath;
    }

    public static void setSyncPath(Path syncPath) throws IOException {
        ClientSetting.syncPath = syncPath;
        FileHelper.createDirectoryIfNotExists(ClientSetting.syncPath);
        LOG.info("path = ", ClientSetting.syncPath.toString());
    }

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        ClientSetting.host = host;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        ClientSetting.port = port;
    }

    public static String getLogin() {
        return login;
    }

    public static void setLogin(String login) {
        ClientSetting.login = login;
    }

    public static int getRate() {
        return rate;
    }

    public static void setRate(int rate) {
        ClientSetting.rate = rate;
    }

    static {
        syncPath = Paths.get(DEF_SYNC_DIR);
        try {
            setSyncPath(syncPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
