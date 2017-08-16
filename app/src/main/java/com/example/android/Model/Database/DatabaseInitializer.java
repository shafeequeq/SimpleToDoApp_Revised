package com.example.android.Model.Database;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.android.Helper.TaskParser;
import com.example.android.Model.GoogleTasksAPI.TaskGoogle;
import com.example.android.Model.GoogleTasksAPI.TaskListGoogle;
import com.example.android.Model.GoogleTasksAPI.TaskListsGoogle;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

//import com.example.android.Model.GoogleTasksAPI.TaskListsGoogle;

/**
 * Created by shafe on 8/12/2017.
 */

public class DatabaseInitializer {
    public static void populateSync(@NonNull final TaskDatabase db) {
        populateWithTestData(db);
    }

    public static void clearAllData(@NonNull final TaskDatabase db){ clearData(db);}

    public static void populateSyncFromJSON(@NonNull final TaskDatabase db , Context context) {
        populateWithTestDataFromJSON(db , context);
    }

    private static TaskListDb createTaskList(String id, String title , String kind , Date updatedDate){
        TaskListDb taskList = new TaskListDb();


        taskList.setID(id);
        taskList.setTitle(title);
        taskList.setKind(kind);
        taskList.setLastUpdated( updatedDate );

        return taskList;
    }

    private static TaskListDb createTaskList(TaskListGoogle t){
        TaskListDb taskListDb = new TaskListDb();


        taskListDb.setID(t.id);
        taskListDb.setTitle(t.title);
        taskListDb.setKind(t.kind);
        taskListDb.setLastUpdated( t.updated );

        return taskListDb;
    }

    private static TaskDb createTask( String id , String title , String priority , String taskListID , String status , Date lastUpdated ){
        TaskDb taskDb = new TaskDb();
        taskDb.id = id;
        taskDb.title = title;
        taskDb.priority = priority;
        taskDb.taskListID = taskListID;
        taskDb.status = status;
        taskDb.lastUpdated = lastUpdated;

        return taskDb;
    }

    private static TaskDb createTask( TaskGoogle task ){
        TaskDb taskDb = new TaskDb();
        taskDb.id = task.getID();
        taskDb.title = task.getTitle();
        taskDb.priority = task.getPriority();
        taskDb.taskListID = task.getTaskListID();
        taskDb.status = task.getStatus();
        taskDb.lastUpdated = task.getLastUpdated();
        taskDb.notes = task.getNotes();
        taskDb.dueDate = task.get_due();

        return taskDb;
    }

    public static Date GetTodayEndofDay( ) {
        Date today = new Date( System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( today );
        calendar.set(Calendar.HOUR_OF_DAY , calendar.getMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE , calendar.getMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND , calendar.getMaximum(Calendar.SECOND));

        return calendar.getTime();
    }

    public static Date GetTodayStartofDay( ) {
        Date today = new Date( System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( today );
        calendar.set(Calendar.HOUR_OF_DAY , calendar.getMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE , calendar.getMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND , calendar.getMinimum(Calendar.SECOND));

        return calendar.getTime();
    }

    public static Date GetTodayPlusDays(int days ) {
        // returns end of day of a day few days from now.
        Date today = new Date( System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( today );
        calendar.add(Calendar.DAY_OF_MONTH , days );
        calendar.set(Calendar.HOUR_OF_DAY , calendar.getMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE , calendar.getMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND , calendar.getMaximum(Calendar.SECOND));
        return calendar.getTime();
    }

    public static Date GetTodayMinusDays(int days ) {
        // returns start of day of a Day few days back.
        Date today = new Date( System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( today );
        calendar.add(Calendar.DAY_OF_MONTH , (-1 * days ));
        calendar.set(Calendar.HOUR_OF_DAY , calendar.getMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE , calendar.getMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND , calendar.getMinimum(Calendar.SECOND));
        return calendar.getTime();
    }

    private static void clearData(TaskDatabase db){
        db.beginTransaction();
        try {
            db.taskModel().deleteAll();
            db.taskListModel().deleteAll();
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }

    private static void populateWithTestData(TaskDatabase db) {
        db.beginTransaction();
        try {
            db.taskModel().deleteAll();
            db.taskListModel().deleteAll();

            String taskListID = "abc";
            String taskListTitle = "Personal";

            TaskListDb taskList = createTaskList(taskListID, taskListTitle, "none", GetTodayPlusDays(0));
            db.taskListModel().insertTaskList(taskList);
/*

 */
            // add tasks.
            TaskDb taskDb = createTask("MDQxOTI0MzIxNTAzNjYwMDY5NDQ6MDoxMzUyNzE3NjAw", "Test Data", "tasks#task", taskListID, "needsAction", GetTodayPlusDays(0));
            db.taskModel().insertTask(taskDb);

            taskDb = createTask("MDQxOTI0MzIxNTAzNjYwMDY5NDQ6MDoxMjMxMDkxMTcz", "New Work Items", "tasks#task", taskListID, "needsAction", GetTodayPlusDays(0));
            db.taskModel().insertTask(taskDb);
            //Mark transaction as successful.
            db.setTransactionSuccessful();
        }
        catch(Exception e){

        }
        finally {
            db.endTransaction();
        }

    }

    private static void populateWithTestDataFromJSON(TaskDatabase db , Context context ) {
        db.taskModel().deleteAll();
        db.taskListModel().deleteAll();

        TaskParser parser = new TaskParser("tasklists.json", "taskdata.json", context);
        TaskListsGoogle lists = parser.getTaskLists();

        List<TaskListGoogle> taskLists = lists.getTaskLists();
        //ArrayList<TaskListDb> taskListDbs = new ArrayList<>();
        // foreach TaskList
        for(int i = 0 ; i < taskLists.size() ; ++i ){
            // for each TaskLists in it.
            TaskListDb tld = createTaskList( taskLists.get(i));
            //taskListDbs.add( );
            db.taskListModel().insertTaskList( tld );

        }



        //for each tasks.

        List<TaskGoogle> tasks = lists.getTasks();
        for(int i = 0 ; i < tasks.size() ; ++i){
            // for each tasks in it.
            TaskDb tdb = createTask( tasks.get(i));
            db.taskModel().insertTask( tdb );
        }

    }
}
