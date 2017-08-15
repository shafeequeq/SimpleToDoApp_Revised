package com.example.android.Fragment;

import com.example.android.Model.ITask;

import java.util.List;

/**
 * Created by shafe on 8/14/2017.
 */

public interface IFragmentActivityCallback {
    // get Data.
    public List<ITask> getAllTasks(String filter);

    // persist Data
    public void persistChanges(List<ITask> mInserted , List<ITask> mUpdated , List<ITask> mDeleted );

}
