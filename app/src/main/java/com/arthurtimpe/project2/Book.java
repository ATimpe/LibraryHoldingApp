package com.arthurtimpe.project2;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "books")
public class Book {
    @PrimaryKey(autoGenerate=true)
    private int accountId;

    @ColumnInfo(name = "title")
    private String Title;

    @ColumnInfo(name = "author")
    private String Author;

    @ColumnInfo(name = "genre")
    private String Genre;

    public Book(String Title, String Author, String Genre) {
        this.Title = Title; this.Author = Author; this.Genre = Genre;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }
}