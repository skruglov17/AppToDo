package org.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.main.backend.connection.DBConnector;
import org.main.backend.Task;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import static org.main.backend.Task.mapRowTask;

/**
 * JavaFX App
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        //Загрузим сцену с задачами из XML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/org/main/resources/schema.fxml"));
        Controller controller = loader.getController();
        Parent root = loader.load();
        Scene sceneTasks = new Scene(root);
        stage.setScene(sceneTasks);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}