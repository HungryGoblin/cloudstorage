import com.sun.istack.internal.NotNull;
import filesystem.FileHelper;
import filesystem.FileList;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import transport.Container;
import transport.FileMessage;
import transport.MessageType;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;


public class ClientController implements Initializable {

    private static final Logger LOG = LoggerFactory.getLogger(ClientController.class);

    public ListView<String> clientFiles;
    public ListView<String> serverFiles;
    public javafx.scene.control.TextArea logArea = new TextArea();

    private Socket socket;
    private ObjectEncoderOutputStream os;
    private ObjectDecoderInputStream is;


    public boolean isConnected() {
        return (socket != null && socket.isConnected());
    }

    public void clearLog(ActionEvent event) {
        logArea.clear();
    }

    public void putLog(String message) {
        logArea.appendText(message + "\r\n");
        LOG.info(message);
    }

    public void updateFiles(ListView listView, FileList fileList) {
        listView.getItems().clear();
        if (fileList.size() > 0)
            listView.getItems().addAll(fileList.getFileList());
    }

    public void connect() {
        String errorMessage = "";
        try {
            if (!isConnected()) {
                socket = new Socket(ClientSetting.getHost(), ClientSetting.getPort());
                if (isConnected()) {
                    os = new ObjectEncoderOutputStream(socket.getOutputStream());
                    is = new ObjectDecoderInputStream(socket.getInputStream());
                }
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }

        putLog(String.format("CONNECT: Cloud storage %s:%d %s",
                ClientSetting.getHost(),
                ClientSetting.getPort(),
                isConnected() ? "connected" : "not connected " + errorMessage));
        readServerData();

    }

    public void sendFile(Path filePath) {
        try {
            filePath = ClientSetting.getSyncPath().resolve(filePath);
            String stringPath = ClientSetting.getSyncPath().relativize(filePath).toString();
            FileHelper.fileExists(filePath, true);
            FileMessage fileMessage = new FileMessage(filePath);
            fileMessage.setStringPath(stringPath);
            for (int i = 0; i < fileMessage.getPartsTotal(); i++) {
                sendData(fileMessage.getPart(i), MessageType.FILE);
            }
        } catch (Exception e) {
            putLog(String.format("ERROR: File sending error [%s]: %s", filePath.toString(), e.getMessage()));
        }
    }

    public void synchronize() throws IOException {
        if (isConnected()) {
            FileList fileList = new FileList(ClientSetting.getSyncPath());
            updateFiles(clientFiles, fileList);
            sendData(fileList, MessageType.FILE_LIST);
        }
    }

    public void processServerData(Container container) throws IOException {
        MessageType messageType = container.getMessageType();
        if (messageType == MessageType.FILE_LIST) {
            FileList fileList = (FileList) container.getMessage();
            updateFiles(serverFiles, fileList);
            fileList.setRoot(ClientSetting.getSyncPath());
            fileList.getFileList().forEach(fileEntity -> sendFile(fileEntity.getPath()));
        }
    }

    public void sendData(@NotNull Object message, MessageType messageType) {
        if (isConnected()) {
            try {
                Container container = new Container(ClientSetting.getLogin(), message, messageType);
                os.writeObject(container);
                os.flush();
                putLog(String.format("DATA SENT: %s", container));
            } catch (Exception e) {
                putLog(String.format("Data sending error: %s", e.getMessage()));
            }
        }
    }

    public void readServerData() {
        if (isConnected()) {
            try {
                new Thread(() -> {
                    while (isConnected()) {
                        try {
                            Container container = (Container) is.readObject();
                            putLog("DATA RECEIVED: " + container.toString());
                            processServerData(container);
                            Thread.sleep(ClientSetting.getRate());
                        } catch (Exception e) {
                            LOG.error("e = ", e);
                            break;
                        }
                    }
                }).start();
            } catch (Exception e) {
                putLog(String.format("An error has occurred: %s", e.getMessage()));
                LOG.error("e = ", e);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connect();
    }
}
