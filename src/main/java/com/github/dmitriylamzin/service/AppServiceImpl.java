package com.github.dmitriylamzin.service;

import com.github.dmitriylamzin.domain.Head;
import com.github.dmitriylamzin.service.helper.PathResolver;
import com.github.dmitriylamzin.view.View;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;

@Service
public class AppServiceImpl implements AppService {
    private final Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private View view;

    @Autowired
    private HeadService headService;

    public AppServiceImpl() {
    }

    @Override
    public boolean init() {
        log.info("init() is called");
        try {
            createM1keDirectories();
            createHeadFile();
        } catch (IOException e) {
            view.showInfo("m1ke.initialization.error");
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }



    private void createM1keDirectories() throws IOException {
        Files.createDirectories(PathResolver.getObjectsDirectoryPath());
        Files.createDirectories(PathResolver.getCommitsDirectoryPath());
        Files.createDirectories(PathResolver.getBranchesDirectoryPath());
        Files.setAttribute(PathResolver.getMainDirectoryPath(), "dos:hidden", true);

    }

    private void createHeadFile() throws IOException {
        Head head = new Head();
        headService.saveHead(head);

    }

    @Override
    public boolean quit() {
        return false;
    }

}
