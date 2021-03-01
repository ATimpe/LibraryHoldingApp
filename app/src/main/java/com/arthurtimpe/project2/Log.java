package com.arthurtimpe.project2;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "logs")
public class Log {
    @PrimaryKey(autoGenerate = true)
    private int logId;

    @ColumnInfo(name = "type")
    private String Type;

    @ColumnInfo(name = "username")
    private String Username;

    @ColumnInfo(name = "reservation num")
    private int ResNum;

    public Log(String Type, String Username) {
        this.Type = Type; this.Username = Username; this.ResNum = -1;
    }

    @Ignore
    public Log(String Type, String Username, int ResNum) {
        this.Type = Type; this.Username = Username; this.ResNum = ResNum;
    }

    @Override
    public String toString() {
        String toStr = "Transaction type: " + Type + ", Customer’s username: " + Username;
        if(ResNum != -1){ toStr += ", Reservation Number: " + ResNum; }
        return toStr + "\n";
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public int getResNum() {
        return ResNum;
    }

    public void setResNum(int resNum) {
        ResNum = resNum;
    }
}
