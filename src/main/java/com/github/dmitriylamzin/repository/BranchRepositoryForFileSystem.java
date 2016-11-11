package com.github.dmitriylamzin.repository;

import com.github.dmitriylamzin.domain.Branch;
import com.github.dmitriylamzin.service.helper.PathResolver;
import com.github.dmitriylamzin.view.View;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Repository
public class BranchRepositoryForFileSystem implements BranchRepository {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private View view;


    @Override
    public boolean saveBranch(Branch branchToSave) {
        Path branchFile = PathResolver.getBranchesDirectoryPath().resolve(branchToSave.getName());
        try (FileOutputStream fileOutputStream = new FileOutputStream(branchFile.toString());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(branchToSave);
            objectOutputStream.close();
        }catch (FileNotFoundException e){
            log.error(e.getMessage(), e);
            return false;
        }catch (IOException e){
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public Branch getBranch(String branchName){
        Branch branch = null;
        log.info("getting  branch from file with name " + branchName);
        Path branchFile = PathResolver.getBranchesDirectoryPath().resolve(branchName);
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
    return branch;
    }

    @Override
    public boolean deleteBranch(String branchName) {
        log.info("deleting file with name " +branchName);
        try {
            Files.deleteIfExists(PathResolver.getBranchesDirectoryPath().resolve(branchName));
            log.info("File with name " + branchName + " is deleted");
            return true;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            view.showInfo("branch.file.is.lost");
            return false;
        }
    }
}
