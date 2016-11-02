package com.github.dmitriylamzin.service;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;


public class IntegrationFileVisitor extends SimpleFileVisitor<Path> {
    private ArrayList<String> existedFiles;

    public IntegrationFileVisitor(ArrayList<String> existedFiles) {
        super();
        this.existedFiles = existedFiles;

    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        if (dir.endsWith(".m1ke")){
            return FileVisitResult.SKIP_SUBTREE;
        }
        return super.preVisitDirectory(dir, attrs);
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        existedFiles.add(file.toString());
//        System.out.println("Path: " + PathResolver.getCurrentWorkingDirectory().relativize(file) + " dir: " + file.getFileName());
//        System.out.println(PathResolver.getObjectsDirectoryPath().resolve(PathResolver.getCurrentWorkingDirectory().relativize(file)));
        return super.visitFile(file, attrs);
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return super.visitFileFailed(file, exc);
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return super.postVisitDirectory(dir, exc);
    }

}
