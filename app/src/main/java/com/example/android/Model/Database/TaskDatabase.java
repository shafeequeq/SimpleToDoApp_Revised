package com.example.android.Model.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by shafe on 8/12/2017.
 */

@Database(entities = {TaskDb.class, TaskListDb.class}, version = 1)
public abstract class TaskDatabase extends RoomDatabase{
    private static TaskDatabase INSTANCE;

    public abstract TaskDao taskModel();
    public abstract TasklistDao taskListModel();

    public static TaskDatabase getInMemoryDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.inMemoryDatabaseBuilder(context.getApplicationContext(), TaskDatabase.class)
                            // To simplify the codelab, allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
