package com.example.android.Model.GoogleTasksAPI;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by shafe on 8/7/2017.
 */

public class TaskListGoogle{
    public String kind;
    public String id;
    public String title;
    public Date updated;
    public String selfLink;
    private ArrayList<TaskGoogle> tasks;

    public TaskListGoogle(){
        tasks = new ArrayList<>();
    }

    public void addTask( TaskGoogle task){
        if( tasks != null )
            tasks.add( task );
    }

    public void addTasks( ArrayList<TaskGoogle> newTasks){
        tasks.addAll( newTasks );
    }

    public Object[] getTasks(){
        return tasks.toArray();
    }


}

