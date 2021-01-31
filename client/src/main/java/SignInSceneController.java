import databasetools.ServerConnection;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class SignInSceneController implements Initializable {

    private final static BackgroundImage background = new BackgroundImage(
            new Image("cloud_background.png"), null, null, null,
            new BackgroundSize(200, 200, false, false, true, true));
    private static final Logger LOG = LoggerFactory.getLogger(SignInSceneController.class);

    public javafx.scene.control.TextInputControl login;
    public javafx.scene.control.TextInputControl password;
    public javafx.scene.control.TextInputControl serverAddress;
    public javafx.scene.control.TextInputControl port;
    public Pane backgroundPane;

    public void signIn() {
        try (ServerConnection connection = ServerConnection.createConnection()) {
            if (!connection.loginUser(login.getText(), password.getText()))
                SceneController.showAlert(String.format("Incorrect Login/Password for user %s",
                        login.getText()), "Error", Alert.AlertType.ERROR);
            else {
                ClientSetting.setHost(serverAddress.getText());
                ClientSetting.setPort(Integer.parseInt(port.getText()));
                ClientSetting.setLogin(login.getText());
                SceneController.setScene(2);
            }
        } catch (SQLException | IOException e) {
            SceneController.showAlert(e.getMessage(), "ERROR", Alert.AlertType.ERROR);
        }
    }

    public void signUp() {
        SceneController.setScene(1);
    }

    public void closeApp() {
        Platform.exit();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backgroundPane.setBackground(new Background(background));
        login.setText(ClientSetting.getLogin());
        serverAddress.setText(ClientSetting.getHost());
        port.setText(String.valueOf(ClientSetting.getPort()));
    }
}
