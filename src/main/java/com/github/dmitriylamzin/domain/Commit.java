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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Commit)) return false;

        Commit commit = (Commit) o;

        if (id != commit.id) return false;
        if (previousCommitId != commit.previousCommitId) return false;
        if (message != null ? !message.equals(commit.message) : commit.message != null) return false;
        if (date != null ? !date.equals(commit.date) : commit.date != null) return false;
        return !(filePaths != null ? !filePaths.equals(commit.filePaths) : commit.filePaths != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (previousCommitId ^ (previousCommitId >>> 32));
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (filePaths != null ? filePaths.hashCode() : 0);
        return result;
    }

    //    public void setFilePaths(ArrayList<String> filePaths) {
//        this.filePaths = filePaths;
//    }

}
