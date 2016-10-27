package com.github.dmitriylamzin.service;

import com.github.dmitriylamzin.view.View;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AppServiceImpl implements AppService {
    private final Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private View view;

    private final Path currentWorkingDirectory;

    @Value("${directory.main}")
    private String mainDirectory;

    @Value("${directory.branches}")
    private String branchDirectory;

    @Value("${directory.commits}")
    private String commitDirectory;

    @Value("${directory.objects}")
    private String objectDirectory;

    @Value("${file.head}")
    private String headFilePathString;

    private Path mikeMainDirectory;

    public AppServiceImpl() {
        this.currentWorkingDirectory = Paths.get(System.getProperty("user.dir"));
        log.info("Current Working Directory is " + currentWorkingDirectory.toString());
    }

    @Override
    public boolean init() {
        log.info("init() is called");
        try {
            createM1keDirectories(currentWorkingDirectory);
            createHeadFile();
        } catch (IOException e) {
            view.showInfo("m1ke.initialization.error");
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }



    private void createM1keDirectories(Path currentWorkingDirectory) throws IOException {
        mikeMainDirectory = currentWorkingDirectory.resolve(mainDirectory);
        Path branchDirectoryToCreate = mikeMainDirectory.resolve(branchDirectory);
        Path commitDirectoryToCreate = mikeMainDirectory.resolve(commitDirectory);
        Path objectDirectoryToCreate = mikeMainDirectory.resolve(objectDirectory);
        Files.setAttribute(mikeMainDirectory, "dos:hidden", true);
        Files.createDirectories(objectDirectoryToCreate);
        Files.createDirectories(commitDirectoryToCreate);
        Files.createDirectories(branchDirectoryToCreate);

    }

    private void createHeadFile() throws IOException {
        Path headFilePath = mikeMainDirectory.resolve(headFilePathString);
        Files.createFile(headFilePath);

    }

    @Override
    public boolean quit() {
        return false;
    }

}
