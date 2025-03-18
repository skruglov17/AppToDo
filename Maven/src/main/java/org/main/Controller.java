package org.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import org.main.backend.Task;
import org.main.backend.connection.DBConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import static org.main.backend.Task.mapRowTask;

public class Controller {

    @FXML
    private TreeView tasksTree;

    private void click(ActionEvent event) {
        
    }

    @FXML
    private void fillData() {
        Connection connection = DBConnector.getConnection();
        LinkedList<Task> tasks = new LinkedList<>();
        TreeItem<String> mainTreeNode = new TreeItem<String>("Задачи");
        tasksTree = new TreeView<String>(mainTreeNode);
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
