package org.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
<<<<<<< Updated upstream
    public void start(Stage stage) {
        var label = new Label("Hello, JavaFX.");
        var scene = new Scene(new StackPane(label), 1280, 640);
        stage.setScene(scene);
=======
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/main/resources/schema.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
>>>>>>> Stashed changes
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}