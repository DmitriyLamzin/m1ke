package com.github.dmitriylamzin.service;

import com.github.dmitriylamzin.domain.Branch;
import com.github.dmitriylamzin.domain.Commit;
import com.github.dmitriylamzin.domain.Head;
import com.github.dmitriylamzin.service.helper.PathResolver;
import com.github.dmitriylamzin.view.View;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class CommitServiceImpl implements CommitService{
    private final Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private HeadService headService;
    @Autowired
    private BranchService branchService;
    @Autowired
    private View view;

    public boolean saveCommit(Commit newCommit) {
        Head head = headService.getHead();
        head.setLastCommitNumber(newCommit.getId());
        Branch branch = head.getCurrentBranch();
        branch.setLastCommit(newCommit);
        saveCommitFile(newCommit);
        branchService.saveBranch(branch);
        headService.saveHead(head);
        return false;
    }

    public String saveTrackedFile(Commit newCommit, String changedFile) {
        String commitPath = String.valueOf(newCommit.getId());
        Path relativePath = PathResolver.getCurrentWorkingDirectory().relativize(Paths.get(changedFile));
        log.debug(relativePath);
        try {
            Path directory = Files.createDirectories(PathResolver.getObjectsDirectoryPath().resolve(relativePath));
            Path createdFile = directory.resolve(commitPath);
            Files.copy(Paths.get(changedFile),
                    createdFile);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return commitPath;
    }

    @Override
    public boolean checkout(Commit commit) {
        log.info("checkout");
        try {
            Files.walkFileTree(PathResolver.getCurrentWorkingDirectory(),
                    new DeletingFileVisitor());
        }catch (AccessDeniedException e){
            log.error(e.getMessage(), e);
            view.showInfo("access.is.denied");
            view.showInfo(e.getMessage());
        }catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        for (String path : commit.getFilePathKeys()){
            Path relativePath = PathResolver.getCurrentWorkingDirectory().relativize(Paths.get(path));
            log.debug(relativePath);
            try {
                Path committedDirectory = PathResolver.getObjectsDirectoryPath().resolve(relativePath);
                Path createdFile = committedDirectory.resolve(commit.getFilePaths().get(path));
                Files.createDirectories(Paths.get(path).getParent());
                Files.copy(createdFile,
                        Paths.get(path));
            }catch (AccessDeniedException e){
                log.error(e.getMessage(), e);
                view.showInfo("access.is.denied");
                view.showInfo(e.getMessage());
            }catch (IOException e) {
                log.error(e.getMessage(), e);
            }

        }
        return false;
    }

    private boolean saveCommitFile(Commit commit){
        try (FileOutputStream fileOutputStream = new FileOutputStream(
                new File(PathResolver.getCommitsDirectoryPath().
                        resolve(String.valueOf(commit.getId())).toString()));
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)){
            log.debug("writing changed File list to the file");
            objectOutputStream.writeObject(commit);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            view.showInfo("commit.file.is.lost");
            return false;
        }
        return true;
    }
}
