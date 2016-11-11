package com.github.dmitriylamzin.service;

import com.github.dmitriylamzin.domain.Branch;
import com.github.dmitriylamzin.domain.Head;
import com.github.dmitriylamzin.repository.BranchRepository;
import com.github.dmitriylamzin.repository.HeadRepository;
import com.github.dmitriylamzin.repository.HeadRepositoryForFileSystem;
import com.github.dmitriylamzin.view.View;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BranchServiceImpl implements BranchService {

    private final Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private View view;
    @Autowired
    private HeadRepository headRepository;
    @Autowired
    private CommitService commitService;
    @Autowired
    private BranchRepository branchRepository;


    public BranchServiceImpl() {
    }

    @Override
    public String createBranch(String... branchCommandArgs) {
        if (branchCommandArgs.length > 0){
            String branchName = branchCommandArgs[0];
            Branch creatingBranch = new Branch(branchName);
            if (headRepository.getHead().getCurrentBranch() != null) {
                creatingBranch.setLastCommit(headRepository.getHead().getCurrentBranch().getLastCommit());
            }
            if (!saveBranch(creatingBranch)) {
                return "branch.has.not.been.created";
            }
        }else {
            log.info("branch name is not specified");
            return "branch.name.is.not.specified";
        }
        log.info("branch has been successfully ");
        return "branch.has.been.created";
    }

    public boolean saveBranch(Branch creatingBranch) {
        return branchRepository.saveBranch(creatingBranch);

    }


    @Override
    public String getBranch(String... branchCommandArgs) {
        Head head = headRepository.getHead();
        Branch branch = getBranchFromFile(branchCommandArgs);
        head.setCurrentBranch(branch);
        if (branch == null || !headRepository.saveHead(head)){
            return "branch.is.not.chosen";
        } else if(branch.getLastCommit() != null) {
            commitService.checkout(branch.getLastCommit());
        }
        return "branch.is.chosen" ;
    }

    @Override
    public String removeBranch(String... branchCommandArgs) {
        log.info("removing branch is proceeded " + branchCommandArgs[0]);
        Head head = headRepository.getHead();
        Branch branchToDelete = getBranchFromFile(branchCommandArgs);
        if (branchToDelete == null){
            return "branch.is.not.deleted";
            }else if (head.getCurrentBranch().getName().equals(branchToDelete.getName())){
            return "branch.is.active";
        }else {
            if (deleteBranchFile(branchCommandArgs)){
                return "branch.is.deleted";
            }else {
                return "branch.is.not.deleted";
            }
        }
    }


    private Branch getBranchFromFile(String... branchCommandArgs){
        Branch branch = null;
        if (branchCommandArgs.length > 0) {
            log.info("getting  branch from file with name " + branchCommandArgs[0]);
            branch = branchRepository.getBranch(branchCommandArgs[0]);

        }else {
            log.info("branch name is not defined");
            view.showInfo("branch.name.is.not.specified");
        }
        return branch;
    }

    private boolean deleteBranchFile(String... branchCommandArgs){
        if (branchCommandArgs.length > 0) {
            log.info("deleting  branch from file with name " + branchCommandArgs[0]);
            return branchRepository.deleteBranch(branchCommandArgs[0]);
        }else {
            log.info("branch name is not defined");
            view.showInfo("branch.name.is.not.specified");
        }
        return false;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void setHeadRepository(HeadRepository headRepository) {
        this.headRepository = headRepository;
    }

    public void setCommitService(CommitService commitService) {
        this.commitService = commitService;
    }

    public void setBranchRepository(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }
}
