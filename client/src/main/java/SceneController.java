import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;

public class SceneController {

    static ArrayList<Scene> scenes = new ArrayList<>();
    private static Stage stage;


    public static void showAlert(String message, String title, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void addScene(Parent parent) {
        scenes.add(new Scene(parent));
    }

    public static void setScene(int i) {
        if (i > scenes.size()) throw
                new ArrayIndexOutOfBoundsException(String.format("Illegal scene index %d", i));
        stage.setScene(scenes.get(i));
        stage.setTitle("Cloud Storage Client");
        stage.show();
    }

    {
        try {
            addScene(FXMLLoader.load(getClass().getResource("signInLayout.fxml")));
            addScene(FXMLLoader.load(getClass().getResource("signUpLayout.fxml")));
            addScene(FXMLLoader.load(getClass().getResource("clientLayout.fxml")));
            addScene(FXMLLoader.load(getClass().getResource("commandLayout.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SceneController(Stage stage) {
        SceneController.stage = stage;
    }
}
