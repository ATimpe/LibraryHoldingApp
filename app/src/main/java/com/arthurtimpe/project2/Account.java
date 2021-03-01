package com.arthurtimpe.project2;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "accounts")
public class Account {
    @PrimaryKey(autoGenerate=true)
    private int accountId;

    @ColumnInfo(name = "username")
    private String Username;

    @ColumnInfo(name = "password")
    private String Password;

    @ColumnInfo(name = "admin")
    private boolean isAdmin;

    public Account(String Username, String Password) {
        this.Username = Username; this.Password = Password; isAdmin = false;
    }

    @Ignore
    public Account(String Username, String Password, boolean isAdmin) {
        this.Username = Username; this.Password = Password; this.isAdmin = isAdmin;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public boolean isAdmin() { return isAdmin; }

    public void setAdmin(boolean admin) { isAdmin = admin; }
}
