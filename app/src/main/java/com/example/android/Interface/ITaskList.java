package com.example.android.Interface;

import java.util.Date;

/**
 * Created by shafe on 8/12/2017.
 */

public interface ITaskList {
    String getTitle();
    void setTitle(String newTitle);

    String getKind();
    void setKind(String kind);

    String getID();
    void setID( String ID);

    Date getLastUpdated();
    void setLastUpdated( Date updatedDate);

}
