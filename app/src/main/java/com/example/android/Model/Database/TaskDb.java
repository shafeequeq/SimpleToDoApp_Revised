package com.example.android.Model.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.android.Helper.DateConverter;
import com.example.android.Interface.ITask;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by shafe on 8/12/2017.
 */
@Entity(foreignKeys = {
        @ForeignKey(entity = TaskListDb.class,
                parentColumns = "id",
                childColumns = "tasklist_id")})
@TypeConverters(DateConverter.class)
public class TaskDb implements ITask , Serializable {
    public @PrimaryKey String id;
    public String title;
    public String priority;
    @ColumnInfo(name="tasklist_id")
    public  String taskListID;
    public String status;
    @ColumnInfo(name="last_updated")
    public Date lastUpdated;
    public String notes;
    public Date dueDate;


    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String newTitle) {
        title = newTitle;
    }

    @Override
    public String getPriority() {
        return priority;
    }

    @Override
    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public void setID(String Id) {
        this.id = Id;
    }

    @Override
    public String getTaskListID() {
        return taskListID;
    }

    @Override
    public void setTaskListID(String taskListID) {
        this.taskListID = taskListID;
    }

    @Override
    public String getTaskListName() {
        return null; // TODO : implement this.
    }

    @Override
    public Date getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public void setLastUpdated(Date updatedDate) {
        this.lastUpdated = updatedDate;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String newStatus) {
        this.status = newStatus;
    }



    @Override
    public String getNotes() {
        return notes;
    }

    @Override
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public Date getDue() {
        return dueDate;
    }

    @Override
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }


}
