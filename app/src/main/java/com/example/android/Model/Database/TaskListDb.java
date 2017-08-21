package com.example.android.Model.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.android.Helper.DateConverter;
import com.example.android.Interface.ITaskList;

import java.util.Date;

/**
 * Created by shafe on 8/12/2017.
 */

@Entity
@TypeConverters(DateConverter.class)
public class TaskListDb implements ITaskList {
    public @PrimaryKey String id;
    public String title;
    public String kind;
    //public String status;
    public Date lastUpdated;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String newTitle) {
        title = newTitle;
    }

    @Override
    public String getKind() {
        return kind;
    }

    @Override
    public void setKind(String kind) {
        this.kind = kind;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public void setID(String ID) {
        this.id = ID;
    }

    @Override
    public Date getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public void setLastUpdated(Date updatedDate) {
         this.lastUpdated = updatedDate;
    }
}
