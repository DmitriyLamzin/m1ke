package com.github.dmitriylamzin.service.helper;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Dmitriy on 27.10.2016.
 */
public class CurrentWorkingDirectoryRetriever {
    public static Path getCurrentWorkingDirectory(){
        return Paths.get(System.getProperty("user.dir"));
    }
}
