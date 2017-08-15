package com.example.android.Model;

import java.util.Date;

/**
 * Created by shafe on 8/12/2017.
 */

public interface ITask {
    String getTitle();
    void setTitle(String newTitle);

    String getPriority();
    void setPriority(String priority);

    String getID();
    void setID( String ID);

    String getTaskListID();
    void setTaskListID( String taskListID );
    String getTaskListName();

    Date getLastUpdated();
    void setLastUpdated( Date updatedDate);

    String getStatus();
    void setStatus( String newStatus);

    String getNotes();
    void setNotes( String notes);

    Date getDue();
    void setDueDate( Date dueDate );

    public class TaskPriority {
        public static final String PRIORITY_LOW = "Low";
        public static final String PRIORITY_MEDIUM = "Normal";
        public static final String PRIORITY_HIGH = "High";

        public final String mPriorityString;

        public TaskPriority(String priorityString) {
            this.mPriorityString = priorityString;
        }
    }
    /*
        <array name = "priority">
        <item>Low</item>
        <item>Normal</item>
        <item>High</item>
    </array>
     */
}




