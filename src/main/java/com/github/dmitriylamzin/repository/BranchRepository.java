package com.github.dmitriylamzin.repository;

import com.github.dmitriylamzin.domain.Branch;

import java.io.IOException;

public interface BranchRepository {
    boolean saveBranch(Branch branchToSave);
    Branch getBranch(String branchName);
    boolean deleteBranch(String branchName);

}
