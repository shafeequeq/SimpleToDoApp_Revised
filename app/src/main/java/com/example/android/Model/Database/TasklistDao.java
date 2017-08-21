package com.example.android.Model.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

/**
 * Created by shafe on 8/12/2017.
 */
@Dao
public interface TasklistDao {
    @Query("select * from tasklistdb")
    List<TaskListDb> loadAllTaskLists();

    @Query("select * from tasklistdb where id = :id")
    TaskListDb loadTaskListByID(int id);

    @Insert(onConflict = IGNORE)
    void insertTaskList(TaskListDb taskList);

    @Delete
    void deleteTaskList(TaskListDb taskList);

    @Query("delete from tasklistdb where id = :tasklistId")
    int deleteTaskListByID(String tasklistId);

    @Query("DELETE FROM tasklistdb")
    void deleteAll();
}
