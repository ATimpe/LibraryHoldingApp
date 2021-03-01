package com.arthurtimpe.project2;

import android.content.Context;

import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Account.class}, version = 1, exportSchema = false)
public abstract class AccountDatabase extends RoomDatabase {
    public abstract AccountDao account();
    private static AccountDatabase sInstance;
    public static synchronized AccountDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(),
                    AccountDatabase.class,
                    "users.db")
                    .allowMainThreadQueries()
                    .build();
        }
        return sInstance;
    }

    public void populateInitialData() {
        if(account().count() == 0) {
            runInTransaction(new Runnable() {
                @Override
                public void run() {
                    account().addAccount(new Account("!admin2", "!admin2", true));
                    account().addAccount(new Account("a@lice5", "@csit100"));
                    account().addAccount(new Account("$brian7", "123abc##"));
                    account().addAccount(new Account("!chris12!", "CHRIS12!!"));
                }
            });
        }
    }

    public boolean accountExists(String username) {
        for (int i = 0; i < account().count(); i++) {
            if (account().getAll().get(i).getUsername().equals(username)) {
                return true;
            }
        }

        return false;
    }

    public boolean validAccountLogin(String username, String password) {
        for (int i = 0; i < account().count(); i++) {
            if (account().getAll().get(i).getUsername().equals(username)
                    && account().getAll().get(i).getPassword().equals(password)) {
                return true;
            }
        }

        return false;
    }

    @Dao
    public interface AccountDao {
        @Insert
        void addAccount(Account account);

        @Query("SELECT COUNT(*) FROM accounts")
        int count();

        @Query("SELECT * FROM accounts")
        List<Account> getAll();

        @Delete
        void delete(Account account);
    }


}
