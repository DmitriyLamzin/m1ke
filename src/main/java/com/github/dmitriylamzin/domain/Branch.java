package com.github.dmitriylamzin.domain;

import java.io.Serializable;


public class Branch implements Serializable{
    private String name;
    private Commit lastCommit;

    public Branch() {
    }

    public Branch(Commit lastCommit, String name) {
        this.lastCommit = lastCommit;
        this.name = name;
    }

    public Branch(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Commit getLastCommit() {
        return lastCommit;
    }

    public long getLastCommitId() {
        return lastCommit.getId();
    }

    public void setLastCommit(Commit newLastCommit) {
        if (lastCommit != null){
            newLastCommit.setPreviousCommitId(this.lastCommit.getPreviousCommitId());

        }
        this.lastCommit = newLastCommit;
    }
}
