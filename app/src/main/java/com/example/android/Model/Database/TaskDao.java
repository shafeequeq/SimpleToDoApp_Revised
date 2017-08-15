package com.example.android.Model.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

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
    @Query("select * from taskdb")
    List<TaskDb> loadAllTasks();

    @Query("select * from taskdb where dueDate <= :before " +
      "ORDER BY dueDate DESC")
    List<TaskDb> loadTasksDueBy(Date before);

    @Query("select * from taskdb where dueDate >= :after")
    List<TaskDb> loadTasksDueAfter(Date after);

    @Query("select * from taskdb where dueDate >= :start" +
    " AND dueDate <= :end")
    List<TaskDb> loadTasksDueBetween(Date start , Date end);

    @Query("select * from taskdb where id = :id")
    TaskDb loadTaskById(int id);

    @Insert(onConflict = IGNORE)
    void insertTask(TaskDb task);

    @Delete
    void deleteTask(TaskDb task);

    @Query("delete from taskdb where id = :id")
    int deleteTaskByID(String id);

    @Query("DELETE FROM taskdb")
    void deleteAll();
}
