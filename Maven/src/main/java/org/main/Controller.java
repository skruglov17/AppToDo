package org.main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.main.backend.PriorityEnum;
import org.main.backend.StatusEnum;
import org.main.backend.Task;
import org.main.backend.connection.DBConnector;

import java.net.URL;
import java.sql.*;
import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.ResourceBundle;

import static org.main.backend.Task.*;

public class Controller implements Initializable {

    @FXML
    private TreeView tasksTree;
    @FXML
    private ToggleButton priorityOne, priorityTwo, priorityThree, priorityFour;
    @FXML
    private TextField topic, topicSubtask, responsible;
    @FXML
    private TextField dayRegistration, monthRegistration, yearRegistration, hourRegistration, minuteRegistration, secondRegistration;
    @FXML
    private TextField dayWish, monthWish, yearWish, hourWish, minuteWish, secondWish;
    @FXML
    private TextField dayComplete, monthComplete, yearComplete, hourComplete, minuteComplete, secondComplete;
    @FXML
    private CheckBox checkComplete;
    @FXML
    private TextArea description;
    private static Task selectedTask;

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
     * Метод предназначен для получения выбранной задачи из списка
     */
    private void addListener() {
        TreeItem<Task> selectedTask;
        SelectionModel<TreeItem<Task>> selectionModel = tasksTree.getSelectionModel();
        selectionModel.selectedItemProperty().addListener(new ChangeListener<TreeItem<Task>>(){
            public void changed(ObservableValue<? extends TreeItem<Task>> changed,
                                TreeItem<Task> unSelectedValue, TreeItem<Task> selectedValue){
                if(selectedValue != null) {
                    updateTreeTasks(selectedValue);
                    Controller.selectedTask = selectedValue.getValue();
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
        listTasks = new LinkedList<>();
        Task.mainTreeNode = new TreeItem<Task>(new Task("Задачи"));
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM tasks");
        ) {
            while(resultSet.next()) {
                listTasks.add(mapRowTask(resultSet));
            }
            for (Task task : listTasks) {
                if(task.getTreeItem() == null) task.setTreeItem(new TreeItem<Task>(task));
            }
            for (Task task : listTasks) {
                if(task.getParentTask() == 0) {
                    Task.mainTreeNode.getChildren().add(task.getTreeItem());
                } else {
                    for (Task temp : listTasks) {
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
        selectedTask = mainTreeNode.getValue();
    }

    /**
     * Метод для заполнения карточки задачи
     * @param selectedTask - выбранная задача из списка
     */
    private void updateTreeTasks(TreeItem<Task> selectedTask) {
        //Заполнение темы задачи
        topic.setText(selectedTask.getValue().getTopic());
        //Заполнение приоритета задачи
        PriorityEnum priorityTask = selectedTask.getValue().getPriority();
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
        responsible.setText(selectedTask.getValue().getResponsiblePerson());
        //Заполнение даты и времени регистрации
        OffsetDateTime registrationTask = selectedTask.getValue().getCreateDate();
        if(registrationTask != null) {
            dayRegistration.setText(Integer.toString(registrationTask.getDayOfMonth()));
            monthRegistration.setText(Integer.toString(registrationTask.getMonthValue()));
            yearRegistration.setText(Integer.toString(registrationTask.getYear()));
            hourRegistration.setText(Integer.toString(registrationTask.getHour()));
            minuteRegistration.setText(Integer.toString(registrationTask.getMinute()));
            secondRegistration.setText(Integer.toString(registrationTask.getSecond()));
        } else {
            dayRegistration.setText("");
            monthRegistration.setText("");
            yearRegistration.setText("");
            hourRegistration.setText("");
            minuteRegistration.setText("");
            secondRegistration.setText("");
        }
        //Заполнение даты и времени желаемой даты реализации
        OffsetDateTime wishDateTask = selectedTask.getValue().getWishDate();
        if(wishDateTask != null) {
            dayWish.setText(Integer.toString(wishDateTask.getDayOfMonth()));
            monthWish.setText(Integer.toString(wishDateTask.getMonthValue()));
            yearWish.setText(Integer.toString(wishDateTask.getYear()));
            hourWish.setText(Integer.toString(wishDateTask.getHour()));
            minuteWish.setText(Integer.toString(wishDateTask.getMinute()));
            secondWish.setText(Integer.toString(wishDateTask.getSecond()));
        } else {
            dayWish.setText("");
            monthWish.setText("");
            yearWish.setText("");
            hourWish.setText("");
            minuteWish.setText("");
            secondWish.setText("");
        }
        //Заполнение даты и времени даты выполнения задачи
        OffsetDateTime completeDateTask = selectedTask.getValue().getCompleteDate();
        if(completeDateTask != null) {
            dayComplete.setText(Integer.toString(completeDateTask.getDayOfMonth()));
            monthComplete.setText(Integer.toString(completeDateTask.getMonthValue()));
            yearComplete.setText(Integer.toString(completeDateTask.getYear()));
            hourComplete.setText(Integer.toString(completeDateTask.getHour()));
            minuteComplete.setText(Integer.toString(completeDateTask.getMinute()));
            secondComplete.setText(Integer.toString(completeDateTask.getSecond()));
        } else {
            dayComplete.setText("");
            monthComplete.setText("");
            yearComplete.setText("");
            hourComplete.setText("");
            minuteComplete.setText("");
            secondComplete.setText("");
        }
        //Проставление статуса выполнения задачи
        StatusEnum completeCheckStatus = selectedTask.getValue().getStatus();
        if(completeCheckStatus == StatusEnum.COMPLETE) {
            checkComplete.setSelected(true);
        } else {
            checkComplete.setSelected(false);
        }
        //Заполнение описания задачи
        String descriptionTask = selectedTask.getValue().getDescription();
        if(descriptionTask != null) {
            description.setText(descriptionTask);
        } else {
            description.setText("");
        }
    }

    /**
     * Метод добавления подзадачи
     */
    @FXML
    private void addSubtask() {
        if(topicSubtask.getText() == null || topicSubtask.getText().equals("")) return;
        int priorityParentTask = 4;
        if(selectedTask.getPriority() != null) {
            switch (selectedTask.getPriority()) {
                case ONE:
                    priorityParentTask = 1;
                    break;
                case TWO:
                    priorityParentTask = 2;
                    break;
                case THREE:
                    priorityParentTask = 3;
                    break;
                case FOUR:
                    priorityParentTask = 4;
                    break;
            }
        }
        TreeItem<Task> parentTaskTreeItem = selectedTask.getTreeItem();
        Task subtask;
        Connection connection = DBConnector.getConnection();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("INSERT INTO tasks (topic, priority, create_date, parental_task) VALUES ('" + topicSubtask.getText() + "', " + priorityParentTask + ", '" + OffsetDateTime.now() + "', '" + selectedTask.getId() +"') RETURNING id;");
                ) {
            while (resultSet.next()) {
                int idAddedTask = resultSet.getInt("id");
                try (
                        Statement statement2 = connection.createStatement();
                        ResultSet resultSet2 = statement2.executeQuery("SELECT * FROM tasks WHERE ID = " + idAddedTask + ";");
                ) {
                    while (resultSet2.next()) {
                        subtask = mapRowTask(resultSet2);
                        if(subtask.getTreeItem() == null) subtask.setTreeItem(new TreeItem<Task>(subtask));
                        if(parentTaskTreeItem != null) {
                            parentTaskTreeItem.getChildren().add(subtask.getTreeItem());
                        } else {
                            mainTreeNode.getChildren().add(subtask.getTreeItem());
                        }
                        tasksTree.getSelectionModel().select(subtask.getTreeItem());
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        topicSubtask.setText("");
    }

    @FXML
    private void deleteTask() {
        if(selectedTask.getId() == 0) return;
        TreeItem<Task> parentTaskTreeItem = selectedTask.getTreeItem().getParent();
        Connection connection = DBConnector.getConnection();
        String query = "DELETE FROM tasks \n" +
                "\tWHERE id = ?;";
        try (
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, selectedTask.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        parentTaskTreeItem.getChildren().remove(selectedTask.getTreeItem());
        tasksTree.getSelectionModel().select(parentTaskTreeItem);
    }

    @FXML
    private void changePriorityOne() {
        priorityOne.setSelected(true);
        priorityTwo.setSelected(false);
        priorityThree.setSelected(false);
        priorityFour.setSelected(false);
    }
    @FXML
    private void changePriorityTwo() {
        priorityOne.setSelected(false);
        priorityTwo.setSelected(true);
        priorityThree.setSelected(false);
        priorityFour.setSelected(false);
    }
    @FXML
    private void changePriorityThree() {
        priorityOne.setSelected(false);
        priorityTwo.setSelected(false);
        priorityThree.setSelected(true);
        priorityFour.setSelected(false);
    }
    @FXML
    private void changePriorityFour() {
        priorityOne.setSelected(false);
        priorityTwo.setSelected(false);
        priorityThree.setSelected(false);
        priorityFour.setSelected(true);
    }






}