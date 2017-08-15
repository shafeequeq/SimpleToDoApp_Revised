package com.example.android.Helper;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.android.Model.Database.TaskDatabase;
import com.example.android.Model.Database.TaskDb;
import com.example.android.Model.Database.TaskListDb;
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

    private static void populateWithTestData(TaskDatabase db) {
        db.taskModel().deleteAll();
        db.taskListModel().deleteAll();

        String taskListID = "abc";
        String taskListTitle = "Personal";

        TaskListDb taskList = createTaskList( taskListID , taskListTitle , "none" , GetTodayPlusDays( 0 ));
        db.taskListModel().insertTaskList( taskList );
/*

 */
        // add tasks.
        TaskDb taskDb = createTask("MDQxOTI0MzIxNTAzNjYwMDY5NDQ6MDoxMzUyNzE3NjAw", "Test Data", "tasks#task", taskListID , "needsAction" , GetTodayPlusDays(0) );
        db.taskModel().insertTask( taskDb );

        taskDb = createTask("MDQxOTI0MzIxNTAzNjYwMDY5NDQ6MDoxMjMxMDkxMTcz", "New Work Items", "tasks#task", taskListID , "needsAction" , GetTodayPlusDays(0) );
        db.taskModel().insertTask( taskDb );


       /* User user1 = addUser(db, "1", "Jason", "Seaver", 40);
        User user2 = addUser(db, "2", "Mike", "Seaver", 12);
        addUser(db, "3", "Carol", "Seaver", 15);

        Book book1 = addBook(db, "1", "Dune");
        Book book2 = addBook(db, "2", "1984");
        Book book3 = addBook(db, "3", "The War of the Worlds");
        Book book4 = addBook(db, "4", "Brave New World");
        addBook(db, "5", "Foundation");
        try {
            // Loans are added with a delay, to have time for the UI to react to changes.

            Date today = getTodayPlusDays(0);
            Date yesterday = getTodayPlusDays(-1);
            Date twoDaysAgo = getTodayPlusDays(-2);
            Date lastWeek = getTodayPlusDays(-7);
            Date twoWeeksAgo = getTodayPlusDays(-14);

            addLoan(db, "1", user1, book1, twoWeeksAgo, lastWeek);
            Thread.sleep(DELAY_MILLIS);
            addLoan(db, "2", user2, book1, lastWeek, yesterday);
            Thread.sleep(DELAY_MILLIS);
            addLoan(db, "3", user2, book2, lastWeek, today);
            Thread.sleep(DELAY_MILLIS);
            addLoan(db, "4", user2, book3, lastWeek, twoDaysAgo);
            Thread.sleep(DELAY_MILLIS);
            addLoan(db, "5", user2, book4, lastWeek, today);
            Log.d("DB", "Added loans");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
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
