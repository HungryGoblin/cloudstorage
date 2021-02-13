import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import transport.MessageType;

import java.net.URL;
import java.util.ResourceBundle;

public class CommandSceneController implements Initializable {

    public javafx.scene.control.TextInputControl command;

    public static ClientController getClientController() {
        return clientController;
    }

    public static void setClientController(ClientController clientController) {
        CommandSceneController.clientController = clientController;
    }

    private static ClientController clientController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void run () {
        if (command.getText().isEmpty()) {
            SceneController.showAlert("Command field can't be blank", "Error", Alert.AlertType.ERROR);
            return;
        }
        clientController.sendData(command.getText(), MessageType.COMMAND);
        command.clear();
        SceneController.setScene(2);
    }

    public void cancel () {
        command.clear();
        SceneController.setScene(2);
    }

}
