package com.github.dmitriylamzin.service;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class DeletingFileVisitor extends SimpleFileVisitor<Path> {
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        if (dir.endsWith(".m1ke")){
            return FileVisitResult.SKIP_SUBTREE;
        }
        return super.preVisitDirectory(dir, attrs);
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Files.delete(file);
//        System.out.println("Path: " + PathResolver.getCurrentWorkingDirectory().relativize(file) + " dir: " + file.getFileName());
//        System.out.println(PathResolver.getObjectsDirectoryPath().resolve(PathResolver.getCurrentWorkingDirectory().relativize(file)));
        return super.visitFile(file, attrs);
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        Files.delete(dir);
        return super.postVisitDirectory(dir, exc);
    }
}
