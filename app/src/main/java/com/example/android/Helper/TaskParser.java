package com.example.android.Helper;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.android.Model.GoogleTasksAPI.TaskGoogle;
import com.example.android.Model.GoogleTasksAPI.TaskListGoogle;
import com.example.android.Model.GoogleTasksAPI.TaskListsGoogle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by shafe on 8/7/2017.
 */

public class TaskParser {
    // private JSONParser parser;
    String strJSONTaskLists;
    String strJSONTaskData;

    public TaskParser(String taskListsFileName , String taskDataFileName , Context context) {
        JSONParser parser = new JSONParser(taskListsFileName, context);
        strJSONTaskLists = parser.getJSONData();

        parser = new JSONParser( taskDataFileName , context );
        strJSONTaskData = parser.getJSONData();

    }



    public TaskListsGoogle getTaskLists() {

        //ArrayList<TaskGoogle> tasks = null;
        TaskListsGoogle taskLists = null;
        if (strJSONTaskLists != null) {
            try {

// Parse tasklists.json
               taskLists = getListsFromTaskListJson();



// Parse taskdata.json & update taskLists with all the tasks.

                JSONObject jsonTaskListsObj = new JSONObject( strJSONTaskData );
                JSONArray jsonTaskListArr = jsonTaskListsObj.getJSONArray("lists");
                for(int i = 0; i < jsonTaskListArr.length();++i){
                    JSONObject jsonTasksObj = jsonTaskListArr.getJSONObject(i);
                    String taskListID = JSONParser.GetString( jsonTasksObj , "id" );

                    // Get all tasks for this TaskListGoogle ID
                    ArrayList<TaskGoogle> tasksArray =  getTasksFromTaskDataJSON(jsonTasksObj , taskListID ); // TODO add taskListID as parent.

                    // Update tasklist with all tasks.
                    TaskListGoogle existingList = taskLists.getTaskList( taskListID);
                    if( existingList != null){
                        existingList.addTasks( tasksArray);
                    }
                }


            } catch (final JSONException e) {
                taskLists = null;
            }
       }

        return taskLists;
    }

    private TaskListsGoogle getListsFromTaskListJson(){
        TaskListsGoogle taskLists ;
        try {
// Parse tasklists.json
            taskLists = new TaskListsGoogle();
            JSONObject jsonTaskListsObj = new JSONObject( strJSONTaskLists );
            String kind = JSONParser.GetString(jsonTaskListsObj, "kind");
            taskLists.kind = kind;

            String etag = JSONParser.GetString(jsonTaskListsObj, "etag");
            taskLists.etag = etag;

            JSONArray jsonTasklists = jsonTaskListsObj.getJSONArray("items");
            for (int k = 0; k < jsonTasklists.length(); ++k) {
                // read each TaskListGoogle data.
                TaskListGoogle taskList = ReadTaskList(jsonTasklists.getJSONObject(k));

                taskLists.addTaskList(taskList, taskList.id);
            }
        }
        catch (final JSONException e) {
            taskLists = null;
        }
        return taskLists;
    }

    private final ArrayList<TaskGoogle> getTasksFromTaskDataJSON( JSONObject jsonTaskDataListsObj , String taskListID ){
        ArrayList<TaskGoogle> tasksArray;
        try{
            tasksArray = new ArrayList<>();
            // read each TaskListGoogle from taskdata.json

            JSONArray jsonTasksArr = jsonTaskDataListsObj.getJSONArray( "items");

            // read and add each tasks.
            for(int j = 0 ; j < jsonTasksArr.length();++j){
                TaskGoogle task = ReadTask( jsonTasksArr.getJSONObject( j ) );
                task.setTaskListID( taskListID );
                tasksArray.add( task );
            }

        }
        catch(final JSONException e){
            tasksArray = null;
        }
        finally {

        }
        return tasksArray;
    }
    private static TaskListGoogle ReadTaskList(JSONObject c){
        /*public String kind;
        public String id;
        public String title;
        public Date updated;
        public String selfLink;*/

        TaskListGoogle taskList = new TaskListGoogle();
        String kind = JSONParser.GetString( c ,"kind");
        taskList.kind = kind;

        String id = JSONParser.GetString( c ,"id");
        taskList.id = id;

        String title = JSONParser.GetString( c ,"title");
        taskList.title = title;

        taskList.updated = JSONParser.GetDate( c , "updated" );

        String selfLink = JSONParser.GetString( c ,"selfLink");
        taskList.selfLink = selfLink;

        return taskList;
    }

    private static TaskGoogle ReadTask( JSONObject c){
        TaskGoogle task = new TaskGoogle();

        String kind = JSONParser.GetString( c ,"kind"); // will be used as priority field.
        task.setPriority( kind );

        String id = JSONParser.GetString( c ,"id");
        task.setID( id );

        String etag = JSONParser.GetString( c ,"etag");
        task.set_etag( etag );

        Date updated = JSONParser.GetDate( c , "updated" );
        task.setLastUpdated( updated );

        String selfLink = JSONParser.GetString( c ,"selfLink");
        task.set_selfLink( selfLink );

        String parent = JSONParser.GetString( c ,"parent");
        task.set_parent( parent );

        String position = JSONParser.GetString( c ,"position");
        task.set_position( position );

        String notes = JSONParser.GetString( c ,"notes");
        task.setNotes( notes );

        String status = JSONParser.GetString( c ,"status");
        task.setStatus( status );

        Date due = JSONParser.GetDate( c , "due" );
        task.setDueDate( due );

        Date completed = JSONParser.GetDate( c , "completed" );
        task.set_completed( completed );

        String title = JSONParser.GetString( c ,"title");
        task.setTitle( title );

        return task;
    }
}

 class JSONParser {
    private String fileName ;
    private Context context;

    public JSONParser(String name , Context context){
        this.fileName = name;
        this.context = context;
    }

    public String getJSONData(){
        String jsonString = null;
        AssetManager assetManager = context.getAssets();
        InputStream file = null;
        try {

             file = assetManager.open(fileName);
            byte[] data = new byte[file.available()];
            file.read(data);
            file.close();
            jsonString = new String(data);
        }
        catch(IOException e){
            jsonString = null;
           // Log.e(e.getMessage());
        }
        finally {
            if ( file != null) {
                try {
                    file.close();
                }
                catch(IOException e){

                }
            }
        }
        return jsonString;
    }

    public static String GetString( JSONObject o , String name){

        String value;
        try{
            value = o.getString( name );
        }
        catch(JSONException e){
            value = null;
        }
        return value;
    }

    public static Date GetDate( JSONObject o , String name ){
        Date value;
        try{
            // "updated": "2017-08-05T01:04:44.000Z"
            SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            value = sourceFormat.parse( o.getString( name ));

        }
        catch(ParseException e){
            value = null;
        }
        catch(JSONException e){
            value = null;
        }
        return value;
    }
}





