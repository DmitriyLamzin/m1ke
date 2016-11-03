package com.github.dmitriylamzin.service.helper;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class PathResolver {
    private final static String PROPERTIES_FILE = "fileSystem.properties";
    private final static Logger LOG = Logger.getLogger(PathResolver.class);

    private static String mainDirectory;
    private static String branchesDirectory;
    private static String commitsDirectory;
    private static String objectsDirectory;
    private static String headFile;

    public static Path getCurrentWorkingDirectory(){
        LOG.info("getting current working directory path");
        Path cwd = Paths.get(System.getProperty("user.dir"));
        LOG.info("current working directory: " + cwd);
        return cwd;
    }

    public static Path getMainDirectoryPath(){
        LOG.info("getting main directory path");
        if (mainDirectory == null){
            initValues();
        }
        LOG.debug("main directory - " + mainDirectory);
        return getCurrentWorkingDirectory().resolve(mainDirectory);
    }

    public static Path getBranchesDirectoryPath(){
        return getMainDirectoryPath().resolve(branchesDirectory);
    }

    public static Path getCommitsDirectoryPath(){
        return getMainDirectoryPath().resolve(commitsDirectory);
    }


    public static Path getObjectsDirectoryPath(){
        return getMainDirectoryPath().resolve(objectsDirectory);
    }

    public static Path getHeadFilePath(){
        return getMainDirectoryPath().resolve(headFile);
    }


    private static void initValues(){
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = PathResolver.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
            properties.load(inputStream);
            mainDirectory = properties.getProperty("directory.main");
            branchesDirectory = properties.getProperty("directory.branches");
            commitsDirectory = properties.getProperty("directory.commits");
            objectsDirectory = properties.getProperty("directory.objects");
            headFile = properties.getProperty("file.head");
            inputStream.close();
        }catch (IOException e){
            LOG.error(e.getMessage(), e);
        }
    }
}
