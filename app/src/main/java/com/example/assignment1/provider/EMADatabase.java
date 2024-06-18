package com.example.assignment1.provider;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.assignment1.Category;
import com.example.assignment1.Event;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Category.class, Event.class}, version = 1)
public abstract class EMADatabase extends RoomDatabase {

    public static final String EMA_DATABASE = "ema_database";

    public abstract EMADAO emaDAO();

    private static volatile EMADatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static EMADatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (EMADatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    EMADatabase.class, EMA_DATABASE)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
