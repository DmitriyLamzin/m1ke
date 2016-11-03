package com.github.dmitriylamzin.service;

import com.github.dmitriylamzin.domain.Branch;

import java.io.IOException;

public interface BranchService {
    String createBranch(String... branchCommandArgs);
    String getBranch(String... branchCommandArgs);
    String removeBranch(String... branchCommandArgs);
    boolean saveBranch(Branch branch);
}
