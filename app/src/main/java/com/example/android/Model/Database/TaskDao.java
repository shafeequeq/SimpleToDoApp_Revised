package com.example.android.Model.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import com.example.android.Helper.DateConverter;

import java.util.Date;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

/**
 * Created by shafe on 8/12/2017.
 */

@Dao
@TypeConverters(DateConverter.class)
public interface TaskDao {
    @Query("select * from taskdb" +
            " ORDER BY dueDate ASC")
    List<TaskDb> loadAllTasks();

    @Query("select * from taskdb where dueDate <= :before " +
      " ORDER BY dueDate ASC")
    List<TaskDb> loadTasksDueBy(Date before);

    @Query("select * from taskdb where dueDate >= :after" +
            " ORDER BY dueDate ASC")
    List<TaskDb> loadTasksDueAfter(Date after);

    @Query("select * from taskdb where dueDate >= :start" +
    " AND dueDate <= :end" + " ORDER BY dueDate ASC")
    List<TaskDb> loadTasksDueBetween(Date start , Date end);

    @Query("select * from taskdb where id = :id")
    TaskDb loadTaskById(String id);

    @Insert(onConflict = IGNORE)
    void insertTask(TaskDb task);

    @Insert(onConflict = IGNORE)
    void insertTasks(TaskDb... taskDbs);

    @Delete
    void deleteTask(TaskDb task);

    @Delete
    public void deleteTasks(TaskDb... taskDbs);

    @Query("delete from taskdb where id = :id")
    int deleteTaskByID(String id);

    @Query("DELETE FROM taskdb")
    void deleteAll();

    @Update
    public void updateTasks(TaskDb... taskDbs);


}
