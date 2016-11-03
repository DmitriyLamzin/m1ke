package com.github.dmitriylamzin.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IntegrationResult implements Serializable {
    private List<String> newFiles = new ArrayList<>();
    private List<String> removedFiles = new ArrayList<>();
    private List<String> changedFiles = new ArrayList<>();

    public List<String> getNewFiles() {
        return newFiles;
    }

    public List<String> getRemovedFiles() {
        return removedFiles;
    }

    public List<String> getChangedFiles() {
        return changedFiles;
    }

    public void setNewFiles(List<String> newFiles) {
        this.newFiles = newFiles;
    }

    public void setRemovedFiles(List<String> removedFiles) {
        this.removedFiles = removedFiles;
    }

    public void setChangedFiles(List<String> changedFiles) {
        this.changedFiles = changedFiles;
    }

    public void addNewFile(String path){
        newFiles.add(path);
    }

    public void addRemovedFile(String path){
        removedFiles.add(path);
    }

    public void addChangedFile(String path){
        changedFiles.add(path);
    }

    @Override
    public String toString() {
        return "IntegrationResult{" +
                "newFiles=" + newFiles +
                ", removedFiles=" + removedFiles +
                ", changedFiles=" + changedFiles +
                '}';
    }

    public List<String> toStringArray(){
        List<String> allChanged =  new ArrayList<String>();
        allChanged.addAll(newFiles);
        allChanged.addAll(removedFiles);
        allChanged.addAll(changedFiles);
        return allChanged;
    }
}
