package com.github.dmitriylamzin.service;

import com.github.dmitriylamzin.domain.Branch;
import com.github.dmitriylamzin.domain.Head;
import com.github.dmitriylamzin.view.View;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class BranchServiceImpl implements BranchService {

    private final Logger log = Logger.getLogger(this.getClass());

    private Path branchStorage;

    @Autowired
    private View view;
    @Autowired
    private HeadService headService;


    @Value("${directory.branches}")
    private String branchDirectoryString;

    @Value("${directory.main}")
    private String mikeDirectoryString;

    public BranchServiceImpl() {
    }

    @Override
    public String createBranch(String... branchCommandArgs) {
        getBranchStoragePath();

        if (branchCommandArgs.length > 0){
            String branchName = branchCommandArgs[0];
            Path branchFile = branchStorage.resolve(branchName);
            if (Files.notExists(branchFile)){
                Branch creatingBranch = new Branch(branchName);
                try (FileOutputStream fileOutputStream = new FileOutputStream(branchFile.toString());
                     ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
                    Files.createFile(branchFile);
                    objectOutputStream.writeObject(creatingBranch);
                    objectOutputStream.close();
                }catch (FileNotFoundException e){
                    log.error(e.getMessage(), e);
                    return "branch.has.not.been.created";
                }catch (IOException e){
                    log.error(e.getMessage(), e);
                    return "branch.has.not.been.created";
                }
            }
        }else {
            return "branch.name.is.not.specified";
        }
        return "branch.has.been.created";
    }

    @Override
    public String getBranch(String... branchCommandArgs) {
        getBranchStoragePath();
        Head head = headService.getHead();
        Branch branch = getBranchFromFile(branchCommandArgs);
        head.setCurrentBranch(branch);
        if (branch == null || !headService.saveHead(head)){
            return "branch.is.not.chosen";
        }
        return "branch.is.chosen" ;
    }

    @Override
    public String removeBranch(String... branchCommandArgs) {
        log.info("removing branch is proceeded " + branchCommandArgs[0]);
        getBranchStoragePath();
        Head head = headService.getHead();
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


    private void getBranchStoragePath() {
        Path currentWorkingDirectory = Paths.get(System.getProperty("user.dir"));
        this.branchStorage = currentWorkingDirectory.
                resolve(mikeDirectoryString).
                resolve(branchDirectoryString);
        log.info("Branch storage Directory is " + branchStorage.toString());
    }

    private Branch getBranchFromFile(String... branchCommandArgs){
        Branch branch = null;
        if (branchCommandArgs.length > 0) {
            log.info("getting  branch from file with name " + branchCommandArgs[0]);
            Path branchFile = branchStorage.resolve(branchCommandArgs[0]);
            try (FileInputStream fin = new FileInputStream(branchFile.toString());
                 ObjectInputStream ois = new ObjectInputStream(fin)) {
                try {
                    branch = (Branch) ois.readObject();
                    ois.close();
                } catch (ClassNotFoundException e) {
                    log.debug("class was not found" + e.getMessage() + e);
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                view.showInfo("branch.file.is.lost");
            }
        }else {
            log.info("branch name is not defined");
            view.showInfo("branch.name.is.not.defined");
        }
        return branch;
    }

    private boolean deleteBranchFile(String... branchCommandArgs){
        log.info("deleting file with name " + branchCommandArgs[0]);
        try {
            Files.deleteIfExists(branchStorage.resolve(branchCommandArgs[0]));
            log.info("File with name " + branchCommandArgs[0] + " is deleted");
            return true;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }
}
