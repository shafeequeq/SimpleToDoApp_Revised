package com.example.android.simpletodoapprevised;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.Model.Database.DatabaseInitializer;
import com.example.android.Helper.TaskParser;
import com.example.android.Model.Database.TaskDatabase;
import com.example.android.Model.Database.TaskDb;
import com.example.android.Model.Database.TaskListDb;
import com.example.android.Model.GoogleTasksAPI.TaskListsGoogle;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private TaskDatabase mDB;
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.android.simpletodoapprevised", appContext.getPackageName());
    }
    @Test
    public void testJSONParser(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        TaskParser parser = new TaskParser( "tasklists.json" , "taskdata.json", appContext);
        TaskListsGoogle lists = parser.getTaskLists();

        assert( lists != null );
    }

    private void dbSetup(){
        if( mDB == null){
            Context appContext = InstrumentationRegistry.getTargetContext();
            mDB = TaskDatabase.getInMemoryDatabase( appContext );
            DatabaseInitializer.populateSync( mDB );
        }
    }

    @Test
    public void testDBCreation(){

        dbSetup();
        assert ( mDB != null );

        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseInitializer.populateSync( mDB );

        List<TaskListDb> list = mDB.taskListModel().loadAllTaskLists();
        assert( list != null );
        for(int i = 0 ; i < list.size(); ++i){
            TaskListDb taskListDb =  list.get( i );
        }

        List<TaskDb> listT = mDB.taskModel().loadAllTasks();
        assert ( listT != null);
        for(int i = 0 ; i < listT.size(); ++i){
            TaskDb taskDb =  listT.get( i );
        }

    }

    @Test
    public void testDBCreationfromJSON(){
        dbSetup();
        assert ( mDB != null );

        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseInitializer.populateSyncFromJSON( mDB , appContext);

        List<TaskListDb> list = mDB.taskListModel().loadAllTaskLists();
        assert( list != null );
        for(int i = 0 ; i < list.size(); ++i){
            TaskListDb taskListDb =  list.get( i );
        }

        List<TaskDb> listT = mDB.taskModel().loadAllTasks();
        assert ( listT != null);
        for(int i = 0 ; i < listT.size(); ++i){
            TaskDb taskDb =  listT.get( i );
        }

        Date startofDay = DatabaseInitializer.GetTodayStartofDay(  );
        Date endofDay = DatabaseInitializer.GetTodayEndofDay(  );
        listT  = mDB.taskModel().loadTasksDueBetween( startofDay , endofDay );

        assert ( listT != null);
        for(int i = 0 ; i < listT.size(); ++i){
            TaskDb taskDb =  listT.get( i );
        }

        Date sevenDaysBack = DatabaseInitializer.GetTodayMinusDays( 7 );
        Date sevenDaysAfter = DatabaseInitializer.GetTodayPlusDays( 7 );
        listT  = mDB.taskModel().loadTasksDueBetween( sevenDaysBack , sevenDaysAfter );

        assert ( listT != null);
        for(int i = 0 ; i < listT.size(); ++i){
            TaskDb taskDb =  listT.get( i );
        }

         Date before = DatabaseInitializer.GetTodayMinusDays( 10 );
        listT  = mDB.taskModel().loadTasksDueBy( before );

        assert ( listT != null);
        for(int i = 0 ; i < listT.size(); ++i){
            TaskDb taskDb =  listT.get( i );
        }


    }

    @Test
    public void testUpdate() {
        dbSetup();
        assert (mDB != null);
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseInitializer.populateSyncFromJSON( mDB , appContext);


        final String id = "MDQxOTI0MzIxNTAzNjYwMDY5NDQ6MTgzMjQxNzA5MTo2MjUyODE1NzY";

        TaskDb taskDb = mDB.taskModel().loadTaskById( id );

        taskDb.setTitle( "Task4_Updated");

        mDB.taskModel().updateTasks( taskDb );
        TaskDb taskDb1 = mDB.taskModel().loadTaskById( id );


    }

}


