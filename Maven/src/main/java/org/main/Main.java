package org.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.main.backend.DBConnector;
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/main/resources/schema.fxml"));
        Parent root = loader.load();
        fillData();
        Scene sceneTasks = new Scene(root);
        stage.setScene(sceneTasks);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private void fillData() {
        Connection connection = DBConnector.getConnection();
        LinkedList<Task> tasks = new LinkedList<>();
        TreeItem<String> mainTreeNode = new TreeItem<String>("Задачи");
        TreeView<String> tasksTree = new TreeView<String>(mainTreeNode);
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM tasks WHERE parental_task IS NULL")
        ) {
            while(resultSet.next()) {
                tasks.add(mapRowTask(resultSet));
            }
            for (Task task : tasks) {
                TreeItem<String> node = new TreeItem<String>(task.getTopic());
                mainTreeNode.getChildren().add(node);
                AnchorPane tasksPane = new AnchorPane(tasksTree);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}