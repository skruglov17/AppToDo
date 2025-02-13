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
    public void start(Stage stage) throws Exception {
        //Загрузим сцену с задачами из XML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/main/resources/schema.fxml"));
        Parent root = loader.load();
        Scene sceneTasks = new Scene(root);
        stage.setScene(sceneTasks);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}