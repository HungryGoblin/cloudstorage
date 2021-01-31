import databasetools.ServerConnection;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignUpSceneController implements Initializable {

    public javafx.scene.control.TextInputControl name;
    public javafx.scene.control.TextInputControl login;
    public javafx.scene.control.TextInputControl password;
    public javafx.scene.control.TextInputControl password1;
    public Pane backgroundPane;

    private final static BackgroundImage background = new BackgroundImage(
            new Image("cloud_background.png"), null, null, null,
            new BackgroundSize(200, 200, false, false, true, true));
    private static final Logger LOG = LoggerFactory.getLogger(SignInSceneController.class);

    public void signUpUser() {
        if (name.getText().isEmpty()) {
            SceneController.showAlert("Name cannot be empty", "Error", Alert.AlertType.ERROR);
            return;
        }
        if (login.getText().isEmpty()) {
            SceneController.showAlert("Login cannot be empty", "Error", Alert.AlertType.ERROR);
            return;
        }
        if (!password.getText().equals(password1.getText())) {
            SceneController.showAlert("Passwords do not match", "Error", Alert.AlertType.ERROR);
            return;
        }
        try {
            ServerConnection.createConnection().createUser(login.getText(), password.getText(), name.getText());
        } catch (SQLException e) {
            SceneController.showAlert(e.getMessage(), "Error", Alert.AlertType.ERROR);
            return;
        }
        SceneController.showAlert(String.format("User %s has registered successfully", login.getText()),
                "INFORMATION", Alert.AlertType.INFORMATION);
        SceneController.setScene(0);
    }

    public void cancel() {
        SceneController.setScene(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backgroundPane.setBackground(new Background(background));
    }
}
