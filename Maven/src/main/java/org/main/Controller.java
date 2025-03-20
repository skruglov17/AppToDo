package org.main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.main.backend.PriorityEnum;
import org.main.backend.StatusEnum;
import org.main.backend.Task;
import org.main.backend.connection.DBConnector;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.ResourceBundle;

import static org.main.backend.Task.mapRowTask;

public class Controller implements Initializable {

    @FXML
    private TreeView tasksTree;
    @FXML
    private ToggleButton priorityOne, priorityTwo, priorityThree, priorityFour;
    @FXML
    private TextField topic, topicSubtask, responsible, dateRegistration, timeRegistration, dateWish, timeWish, dateComplete, timeComplete;
    @FXML
    private CheckBox checkComplete;
    @FXML
    private TextArea description;
    @FXML
    private Button buttonAddSubtask;

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

    @FXML
    private void addSubtask() {
        String nameTopicSubtask = topicSubtask.getText();
        Connection connection = DBConnector.getConnection();
        LinkedList<Task> tasks = new LinkedList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM tasks")
        ) {
            while(resultSet.next()) {
                tasks.add(mapRowTask(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод предназначен для заполнения данных по выбранной задаче из списка
     */
    private void addListener() {
        SelectionModel<TreeItem<Task>> selectionModel = tasksTree.getSelectionModel();
        selectionModel.selectedItemProperty().addListener(new ChangeListener<TreeItem<Task>>(){
            public void changed(ObservableValue<? extends TreeItem<Task>> changed,
                                TreeItem<Task> unSelectedValue, TreeItem<Task> selectedValue){
                //Заполнение темы задачи
                topic.setText(selectedValue.getValue().getTopic());
                //Заполнение приоритета задачи
                PriorityEnum priorityTask = selectedValue.getValue().getPriority();
                if(priorityTask != null) {
                    switch(priorityTask) {
                        case ONE:
                            priorityOne.setSelected(true);
                            priorityTwo.setSelected(false);
                            priorityThree.setSelected(false);
                            priorityFour.setSelected(false);
                            break;
                        case TWO:
                            priorityOne.setSelected(false);
                            priorityTwo.setSelected(true);
                            priorityThree.setSelected(false);
                            priorityFour.setSelected(false);
                            break;
                        case THREE:
                            priorityOne.setSelected(false);
                            priorityTwo.setSelected(false);
                            priorityThree.setSelected(true);
                            priorityFour.setSelected(false);
                            break;
                        case FOUR:
                            priorityOne.setSelected(false);
                            priorityTwo.setSelected(false);
                            priorityThree.setSelected(false);
                            priorityFour.setSelected(true);
                            break;
                        default:
                            System.out.println("Ошибка определения приоритета по задаче!");
                    }
                } else {
                    priorityOne.setSelected(false);
                    priorityTwo.setSelected(false);
                    priorityThree.setSelected(false);
                    priorityFour.setSelected(false);
                }
                //Заполнение ответственного по задаче
                responsible.setText(selectedValue.getValue().getResponsiblePerson());
                //Заполнение даты и времени регистрации
                OffsetDateTime registrationTask = selectedValue.getValue().getCreateDate();
                if(registrationTask != null) {
                    dateRegistration.setText(registrationTask.toLocalDate().toString());
                    timeRegistration.setText(registrationTask.toLocalTime().toString());
                } else {
                    dateRegistration.setText("");
                    timeRegistration.setText("");
                }
                //Заполнение даты и времени желаемой даты реализации
                OffsetDateTime wishDateTask = selectedValue.getValue().getWishDate();
                if(wishDateTask != null) {
                    dateWish.setText(wishDateTask.toLocalDate().toString());
                    timeWish.setText(wishDateTask.toLocalTime().toString());
                } else {
                    dateWish.setText("");
                    timeWish.setText("");
                }
                //Заполнение даты и времени даты выполнения задачи
                OffsetDateTime completeDateTask = selectedValue.getValue().getCompleteDate();
                if(completeDateTask != null) {
                    dateComplete.setText(completeDateTask.toLocalDate().toString());
                    timeComplete.setText(completeDateTask.toLocalTime().toString());
                } else {
                    dateComplete.setText("");
                    timeComplete.setText("");
                }
                //Проставление статуса выполнения задачи
                StatusEnum completeCheckStatus = selectedValue.getValue().getStatus();
                if(completeCheckStatus == StatusEnum.COMPLETE) {
                    checkComplete.setSelected(true);
                } else {
                    checkComplete.setSelected(false);
                }
                //Заполнение описания задачи
                String descriptionTask = selectedValue.getValue().getDescription();
                if(descriptionTask != null) {
                    description.setText(descriptionTask);
                } else {
                    description.setText("");
                }
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
        if(Task.mainTreeNode == null) Task.mainTreeNode = new TreeItem<Task>(new Task("Задачи"));
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM tasks")
        ) {
            while(resultSet.next()) {
                tasks.add(mapRowTask(resultSet));
            }
            for (Task task : tasks) {
                if(task.getTreeItem() == null) task.setTreeItem(new TreeItem<Task>(task));
            }
            for (Task task : tasks) {
                if(task.getParentTask() == 0) {
                    Task.mainTreeNode.getChildren().add(task.getTreeItem());
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
        tasksTree.setRoot(Task.mainTreeNode);
    }

}
