package com.github.dmitriylamzin.service;

import java.io.IOException;

/**
 * Created by Dmitriy on 25.10.2016.
 */
public interface BranchService {
    String createBranch(String... branchCommandArgs) throws IOException;
    String getBranch(String... branchCommandArgs);
    String removeBranch(String... branchCommandArgs);
}
