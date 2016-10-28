package com.github.dmitriylamzin.service.helper;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CurrentWorkingDirectoryRetriever {
    public static Path getCurrentWorkingDirectory(){
        return Paths.get(System.getProperty("user.dir"));
    }
}
