package com.github.dmitriylamzin.service;

import com.github.dmitriylamzin.domain.Head;
import com.github.dmitriylamzin.service.helper.PathResolver;
import com.github.dmitriylamzin.view.View;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Path;
@Service
public class HeadServiceImpl implements HeadService {

    @Autowired
    private View view;
    private final Logger log = Logger.getLogger(this.getClass());

    public HeadServiceImpl() {
    }

    @Override
    public boolean saveHead(Head head) {
        Path headPath = getPath();
        try (FileOutputStream fileOutputStream = new FileOutputStream(headPath.toString());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)){
            objectOutputStream.writeObject(head);
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

    private Path getPath() {
        return PathResolver.getHeadFilePath();
    }

    public Head getHead() {
        Path headPath = getPath();
        Head head = new Head();
        try {
            FileInputStream fin = new FileInputStream(headPath.toString());
            ObjectInputStream ois = new ObjectInputStream(fin);
            try {
                head = (Head) ois.readObject();
                return head;
            } catch (ClassNotFoundException e) {
                log.debug("class was not found" + e.getMessage() + e);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            view.showInfo("head.file.is.lost");
        }
        return head;
    }
}