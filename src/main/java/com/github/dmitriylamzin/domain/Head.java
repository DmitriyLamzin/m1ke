package com.github.dmitriylamzin.domain;

import org.apache.log4j.Logger;

import java.beans.Transient;
import java.io.Serializable;

public class Head implements Serializable{

    private transient Logger logger = Logger.getLogger(this.getClass());
    private Branch currentBranch;
    private long lastCommitNumber;

    public Branch getCurrentBranch() {
        return currentBranch;
    }

    public void setCurrentBranch(Branch currentBranch) {
        this.currentBranch = currentBranch;
    }

    public void setLastCommitNumber(long lastCommitNumber) {
        this.lastCommitNumber = lastCommitNumber;
    }

    public long getLastCommitNumber(){
        return lastCommitNumber;
    }
}
