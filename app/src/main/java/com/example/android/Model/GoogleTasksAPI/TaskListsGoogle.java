package com.example.android.Model.GoogleTasksAPI;

import com.example.android.Interface.ITask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shafe on 8/7/2017.
 */

public class TaskListsGoogle {
    public String kind;
    public String etag;
    private HashMap<String, TaskListGoogle > items;

    public TaskListsGoogle(){
        items = new HashMap<String, TaskListGoogle>();

    }

    public void addTaskList( TaskListGoogle t , String id){
        if( items != null){
            items.put( id , t );
        }
    }

    public TaskListGoogle getTaskList( String id){
        return items.get( id );
    }

    public List<TaskGoogle> getTasks(){
        ArrayList<TaskGoogle> list = new ArrayList<>();

        Collection<TaskListGoogle> collection = items.values();
        if ( collection != null ){
            for( TaskListGoogle t : collection){

                Object[] listO = t.getTasks();
                for( Object o : listO ){
                    list.add( (TaskGoogle)o );
                }
            }
        }


        return list;
    }

    public List<ITask> getITasks(){
        ArrayList<ITask> list = new ArrayList<>();

        Collection<TaskListGoogle> collection = items.values();
        if ( collection != null ){
            for( TaskListGoogle t : collection){

                Object[] listO = t.getTasks();
                for( Object o : listO ){
                    list.add( (ITask) (TaskGoogle)o );
                }
            }
        }


        return list;
    }

    public List<TaskListGoogle> getTaskLists(){
        ArrayList<TaskListGoogle> list  = new ArrayList<>();

        list.addAll( items.values() );

        return list;

    }
}


