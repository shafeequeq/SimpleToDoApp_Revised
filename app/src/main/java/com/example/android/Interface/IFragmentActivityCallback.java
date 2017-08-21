package com.example.android.Interface;

import java.util.List;

/**
 * Created by shafe on 8/14/2017.
 */

public interface IFragmentActivityCallback {
    // get Data.
    public List<ITask> getAllTasks(String filter);

    // persist Data
    //public void persistChanges(List<ITask> mInserted , List<ITask> mUpdated , List<ITask> mDeleted );

    public void taskAdded( ITask task);
    public void taskUpdated( ITask task);
    public void taskDeleted( ITask task);
}
