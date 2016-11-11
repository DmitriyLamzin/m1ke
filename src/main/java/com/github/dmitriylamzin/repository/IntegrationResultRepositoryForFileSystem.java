package com.github.dmitriylamzin.repository;

import com.github.dmitriylamzin.domain.IntegrationResult;
import com.github.dmitriylamzin.service.helper.PathResolver;
import com.github.dmitriylamzin.view.View;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.*;

@Repository
public class IntegrationResultRepositoryForFileSystem implements IntegrationResultRepository {
    private Logger log = Logger.getLogger(this.getClass());
    private final String changedListFilePath = "changedList";

    @Autowired
    private View view;

    @Override
    public boolean saveIntegrationResult(IntegrationResult integrationResult) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(
                new File(PathResolver.getMainDirectoryPath().resolve(changedListFilePath).toString()));
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)){
            log.debug("writing changed File list to the file");
            objectOutputStream.writeObject(integrationResult);
            return true;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            view.showInfo("file.is.lost");
            return false;
        }
    }

    @Override
    public IntegrationResult getIntegrationResult() {
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
            log.debug(e.getMessage(), e);
            e.printStackTrace();
        }
        return integrationResult;
    }
}
