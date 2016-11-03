package com.github.dmitriylamzin.service;

import com.github.dmitriylamzin.domain.Commit;

public interface CommitService {
    String saveTrackedFile(Commit commit, String changedFile);
    boolean saveCommit(Commit commit);
    boolean checkout(Commit commit);
}
