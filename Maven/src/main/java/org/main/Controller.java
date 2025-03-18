package org.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import org.main.backend.Task;
import org.main.backend.connection.DBConnector;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.ResourceBundle;

import static org.main.backend.Task.mapRowTask;

public class Controller implements Initializable {

    @FXML
    private TreeView tasksTree;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillTasksTree();
    }

    @FXML
    private void fillTasksTree() {
        Connection connection = DBConnector.getConnection();
        LinkedList<Task> tasks = new LinkedList<>();
        TreeItem<String> mainTreeNode = new TreeItem<String>("Задачи");
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM tasks")
        ) {
            while(resultSet.next()) {
                tasks.add(mapRowTask(resultSet));
            }
            for (Task task : tasks) {
                task.setTreeItem(new TreeItem<String>(task.getTopic()));
            }
            for (Task task : tasks) {
                if(task.getParentTask() == 0) {
                    mainTreeNode.getChildren().add(task.getTreeItem());
                } else {
                    for (Task temp : tasks) {
                        if(temp.getId() == task.getParentTask()){
                            temp.getTreeItem().getChildren().add(task.getTreeItem());
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tasksTree.setRoot(mainTreeNode);
    }

}
