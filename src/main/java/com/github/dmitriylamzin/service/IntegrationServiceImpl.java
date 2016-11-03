package com.github.dmitriylamzin.service;

import com.github.dmitriylamzin.domain.Branch;
import com.github.dmitriylamzin.domain.Commit;
import com.github.dmitriylamzin.domain.IntegrationResult;
import com.github.dmitriylamzin.service.helper.PathResolver;
import com.github.dmitriylamzin.view.View;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class IntegrationServiceImpl implements IntegrationService {
    private Logger log = Logger.getLogger(this.getClass());
    private final String changedListFilePath = "changedList";
    @Autowired
    private View view;
    @Autowired
    private HeadService headService;
    @Autowired
    private BranchService branchService;
    @Autowired
    private CommitService commitService;

    @Override
    public List<String> integrate() {
        log.info("integrate command");
        ArrayList<String> existedFileArray = collectExistedFiles();
        Branch currentBranch = headService.getHead().getCurrentBranch();
        IntegrationResult integrationResult = new IntegrationResult();

        if (currentBranch == null){
            log.debug("current branch null");
            branchService.createBranch("master");
            branchService.getBranch("master");
            view.showInfo("default.branch.has.been.created");
            integrationResult.setNewFiles(existedFileArray);

        }else if (currentBranch.getLastCommit() == null || currentBranch.getLastCommit().getFilePathKeys().isEmpty()){
            log.debug(existedFileArray);
            integrationResult.setNewFiles(existedFileArray);
        }else {
            TreeMap<String, String> committedFiles = currentBranch.getLastCommit().getFilePaths();
            integrationResult = compareFiles(existedFileArray, committedFiles);
        }
        writeIntegrationResult(integrationResult);
        return integrationResult.toStringArray();
    }

    @Override
    public String save(String... args) {
        long lastCommitNumber = headService.getHead().getLastCommitNumber();
        Commit previousCommit = headService.getHead().getCurrentBranch().getLastCommit();
        IntegrationResult integrationResult = retrieveIntegrationResult();

        if (integrationResult.getChangedFiles().size() == 0 &&
                integrationResult.getRemovedFiles().size() == 0 &&
                integrationResult.getNewFiles().size() == 0){
            return "no.changes";
        }

        writeIntegrationResult(new IntegrationResult());
        Commit newCommit = new Commit();

        newCommit.setId(++lastCommitNumber);
        newCommit.setMessage(getMessage(args));
        TreeMap<String, String> pathMap = new TreeMap<>();

        if (previousCommit == null) {
            newCommit.setPreviousCommitId(0);
        }else {
            newCommit.setPreviousCommitId(headService.getHead().getCurrentBranch().getLastCommitId());
            pathMap = previousCommit.getFilePaths();
        }
        for (String changedFile : integrationResult.getChangedFiles()){
            String commitPath = commitService.saveTrackedFile(newCommit, changedFile);
            pathMap.put(changedFile, commitPath);
        }
        for (String changedFile : integrationResult.getNewFiles()){
            String commitPath = commitService.saveTrackedFile(newCommit, changedFile);
            pathMap.put(changedFile, commitPath);
        }
        for (String changedFile : integrationResult.getRemovedFiles()){
            pathMap.remove(changedFile);
        }

        newCommit.setFilePaths(pathMap);
        commitService.saveCommit(newCommit);

        return "committed " + newCommit.getMessage();
    }


    private ArrayList<String> collectExistedFiles(){
        log.debug("collect existed files");
        ArrayList<String> existedFiles = new ArrayList<>();
        try {
            Files.walkFileTree(PathResolver.getCurrentWorkingDirectory(),
                    new IntegrationFileVisitor(existedFiles));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        log.debug("existed file list " + existedFiles);
        return existedFiles;
    }

    private IntegrationResult compareFiles(ArrayList<String> existedFiles, TreeMap<String, String> committedFiles){
        log.debug("compare files");
        IntegrationResult integrationResult = new IntegrationResult();
        for (String existedFilePath : existedFiles) {
            if (committedFiles.containsKey(existedFilePath)) {
                File committedFile = concatCommittedFilesPath(existedFilePath, committedFiles.get(existedFilePath)).toFile();
                File existedFile = new File(existedFilePath);
                try {
                    if (!FileUtils.contentEquals(committedFile, existedFile)) {
                        log.debug(committedFile.toString() + " is not equal " + existedFile.toString());
                        integrationResult.addChangedFile(existedFilePath);
                    }
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            } else {
                integrationResult.addNewFile(existedFilePath);
            }
            for (String committedFile : committedFiles.keySet()){
                if (!existedFiles.contains(committedFile)){
                    integrationResult.addRemovedFile(committedFile);
                }
            }
        }
        return integrationResult;
    }

    private Path concatCommittedFilesPath(String fileForCommitting, String fileVersionPath){
        Path committedFilePath = Paths.get(fileForCommitting);
        Path relativePath = PathResolver.getCurrentWorkingDirectory().relativize(committedFilePath);
        Path directory = PathResolver.getObjectsDirectoryPath().resolve(relativePath);

        return directory.resolve(fileVersionPath);
    }

    private void writeIntegrationResult(IntegrationResult integrationResult) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(
                new File(PathResolver.getMainDirectoryPath().resolve(changedListFilePath).toString()));
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)){
            log.debug("writing changed File list to the file");
            objectOutputStream.writeObject(integrationResult);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            view.showInfo("file.is.lost");
        }
    }

    private IntegrationResult retrieveIntegrationResult(){
        IntegrationResult integrationResult = new IntegrationResult();
        try {
            FileInputStream fin = new FileInputStream(
                    PathResolver.getMainDirectoryPath().resolve(changedListFilePath).toString());
            ObjectInputStream objectInputStream = new ObjectInputStream(fin);
            integrationResult = (IntegrationResult) objectInputStream.readObject();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            view.showInfo("file.is.lost");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return integrationResult;
    }

    private String getMessage(String... args){
        String message = null;
        if (args.length == 0 || !args[0].equals("-m")){
            view.showInfo("message.is.missed");
            throw new NullPointerException();
        }else if (args.length ==  2){
               message = args[1];
        }else {
            view.showInfo("message.should.be.in.quotes");
            throw new NullPointerException();
        }
        return message;
    }
}
