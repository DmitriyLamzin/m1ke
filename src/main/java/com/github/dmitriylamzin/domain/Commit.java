package com.github.dmitriylamzin.domain;

import java.io.Serializable;
import java.util.*;

public class Commit implements Serializable {
    private long id;
    private long previousCommitId;
    private String message;
    private Date date = new Date();
    private TreeMap<String, String> filePaths;

    public Commit() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPreviousCommitId() {
        return previousCommitId;
    }

    public void setPreviousCommitId(long previousCommitId) {
        this.previousCommitId = previousCommitId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Collection<String> getFilePathKeys() {
        return filePaths.keySet();
    }

    public TreeMap<String, String> getFilePaths() {
        return filePaths;
    }

    public void setFilePaths(TreeMap<String, String> filePaths) {
        this.filePaths = filePaths;
    }

    //    public void setFilePaths(ArrayList<String> filePaths) {
//        this.filePaths = filePaths;
//    }

}
