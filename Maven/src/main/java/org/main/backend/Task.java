package org.main.backend;

import javafx.scene.control.TreeItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.List;

public class Task {

    private int id;
    private String topic;
    private PriorityEnum priority;
    private String responsiblePerson;
    private String description;
    private StatusEnum status;
    private OffsetDateTime createDate;
    private OffsetDateTime wishDate;
    private OffsetDateTime completeDate;
    private int parentTask;
    private TreeItem<Task> treeItem;

    public Task() {}

    public Task(String topic) {
        this.topic = topic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public PriorityEnum getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        switch(priority) {
            case 1: this.priority = PriorityEnum.ONE;
            case 2: this.priority = PriorityEnum.TWO;
            case 3: this.priority = PriorityEnum.THREE;
            case 4: this.priority = PriorityEnum.FOUR;
        };
    }

    public String getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(String responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(int status) {
        switch(status) {
            case 0: this.status = StatusEnum.PROGRESS;
            case 1: this.status = StatusEnum.COMPLETE;
        };
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
    }

    public OffsetDateTime getWishDate() {
        return wishDate;
    }

    public void setWishDate(OffsetDateTime wishDate) {
        this.wishDate = wishDate;
    }

    public OffsetDateTime getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(OffsetDateTime completeDate) {
        this.completeDate = completeDate;
    }

    public int getParentTask() {
        return parentTask;
    }

    public void setParentTask(int parentTask) {
        this.parentTask = parentTask;
    }

    public TreeItem<Task> getTreeItem() {
        return treeItem;
    }

    public void setTreeItem(TreeItem<Task> treeItem) {
        this.treeItem = treeItem;
    }

    public static Task mapRowTask(ResultSet resultSet) throws SQLException {
        Task task = new Task();
        task.setId(resultSet.getInt("id"));
        task.setTopic(resultSet.getString("topic"));
        task.setPriority(resultSet.getInt("priority"));
        task.setResponsiblePerson(resultSet.getString("resposible"));
        task.setCreateDate(resultSet.getObject("create_date", OffsetDateTime.class));
        task.setWishDate(resultSet.getObject("wish_date", OffsetDateTime.class));
        task.setCompleteDate(resultSet.getObject("complete_date", OffsetDateTime.class));
        task.setStatus(resultSet.getInt("complete_status"));
        task.setDescription(resultSet.getString("description"));
        task.setParentTask(resultSet.getInt("parental_task"));
        return task;
    }

    @Override
    public String toString() {
        return this.getTopic();
    }
}
