package cloudserver;

import filesystem.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerSetting {

    public static final int PORT = 8189;
    public static final String NAME = "Skynet";
    private static final String DEF_SYNC_DIR = "server\\src\\main\\resources\\Sync";
    private static final Logger LOG = LoggerFactory.getLogger(ServerSetting.class);

    private static Path syncPath;

    public static void setSyncPath(Path syncPath) throws IOException {
        ServerSetting.syncPath = syncPath;
        FileHelper.createDirectoryIfNotExists(ServerSetting.syncPath);
    }

    public static Path getSyncPath() {
        return syncPath;
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
