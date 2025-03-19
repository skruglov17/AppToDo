package org.main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    @FXML
    private TextField topic;

    /**
     * Метод предназначен для заполнения данными при запуске приложения
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillTasksTree();
        addListener();
    }

    /**
     * Метод предназначен для заполнения данных по выбранной задаче из списка
     */
    private void addListener() {
        SelectionModel<TreeItem<Task>> selectionModel = tasksTree.getSelectionModel();
        selectionModel.selectedItemProperty().addListener(new ChangeListener<TreeItem<Task>>(){
            public void changed(ObservableValue<? extends TreeItem<Task>> changed,
                                TreeItem<Task> unSelectedValue, TreeItem<Task> selectedValue){
                topic.setText(selectedValue.getValue().getTopic());
            }
        });
    }

    /**
     * Метод предназначен для получения всего списка задач, сохранения их из БД в объекты и формирования списка дерева
     */
    @FXML
    private void fillTasksTree() {
        Connection connection = DBConnector.getConnection();
        LinkedList<Task> tasks = new LinkedList<>();
        TreeItem<Task> mainTreeNode = new TreeItem<Task>(new Task("Задачи"));
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM tasks")
        ) {
            while(resultSet.next()) {
                tasks.add(mapRowTask(resultSet));
            }
            for (Task task : tasks) {
                task.setTreeItem(new TreeItem<Task>(task));
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
