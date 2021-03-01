package com.arthurtimpe.project2;

import android.content.Context;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Log.class}, version = 1, exportSchema = false)
public abstract class LogDatabase extends RoomDatabase {
    public abstract LogDatabase.LogDao log();
    private static LogDatabase sInstance;
    public static synchronized LogDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(),
                            LogDatabase.class,
                            "logs.db")
                    .allowMainThreadQueries()
                    .build();
        }
        return sInstance;
    }

    public int generateReservationNumber() {
        int resNum = 1;
        for (int i = 0; i < log().count(); i++) {
            if (log().getAll().get(i).getType().equals("Place hold")) {
                resNum++;
            }
        }

        return resNum;
    }

    @Dao
    public interface LogDao {
        @Insert
        void addLog(Log log);

        @Query("SELECT COUNT(*) FROM logs")
        int count();

        @Query("SELECT * FROM logs")
        List<Log> getAll();

        @Delete
        void delete(Log log);
    }

    @Override
    public String toString() {
        String toStr = "";
        for (int i = 0; i < log().count(); i++) {
            toStr += log().getAll().get(i).toString();
        }

        return toStr;
    }
}

