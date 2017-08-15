package com.example.android.Fragment;

import com.example.android.Model.ITask;

import java.util.List;

/**
 * Created by shafe on 8/14/2017.
 */

public interface IFragmentActivityCallback {
    // get Data.
    public List<ITask> getAllTasks(String filter);

    // TODO put the onMethods here.
}
