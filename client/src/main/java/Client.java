import javafx.application.Application;
import javafx.stage.Stage;

public class Client extends Application {

    private SceneController sceneController;
    
    @Override
    public void start(Stage stage) {
        sceneController = new SceneController(stage);
        SceneController.setScene(0);
    }
}
