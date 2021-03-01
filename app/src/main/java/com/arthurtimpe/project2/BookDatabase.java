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

@Database(entities = {Book.class}, version = 1, exportSchema = false)
public abstract class BookDatabase extends RoomDatabase {
    public abstract BookDatabase.BookDao book();
    private static BookDatabase sInstance;
    public static synchronized BookDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(),
                            BookDatabase.class,
                            "books.db")
                    .allowMainThreadQueries()
                    .build();
        }
        return sInstance;
    }

    public void populateInitialData() {
        if(book().count() == 0) {
            runInTransaction(new Runnable() {
                @Override
                public void run() {
                    book().addBook(new Book("I Know Why the Caged Bird Sings", "Maya Angelou", "Memoir"));
                    book().addBook(new Book("The Mythical Man-Month", "Frederick Brooks", "Computer Science"));
                    book().addBook(new Book("Frankenstein", "Mary Shelley", "Fiction"));
                }
            });
        }
    }

    public boolean isGenreEmpty(String genre) {
        for (int i = 0; i < book().count(); i++) {
            if (book().getAll().get(i).getGenre().equals(genre)) {
                return false;
            }
        }

        return true;
    }

    @Dao
    public interface BookDao {
        @Insert
        void addBook(Book book);

        @Query("SELECT COUNT(*) FROM books")
        int count();

        @Query("SELECT * FROM books")
        List<Book> getAll();

        @Delete
        void delete(Book book);
    }
}
