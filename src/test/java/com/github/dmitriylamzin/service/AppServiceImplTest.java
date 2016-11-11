package com.github.dmitriylamzin.service;

import com.github.dmitriylamzin.domain.Head;
import com.github.dmitriylamzin.repository.HeadRepositoryForFileSystem;
import com.github.dmitriylamzin.service.helper.PathResolver;
import com.github.dmitriylamzin.view.View;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AppServiceImpl.class})
public class AppServiceImplTest {

    @Mock
    private HeadRepositoryForFileSystem headRepositoryForFileSystem;

    @Mock
    private View view;

    private AppService appService;

    public AppServiceImplTest() {
        PowerMockito.mockStatic(Files.class);
    }

    @Before
    public void setUp(){
        initMocks(this);
        this.appService = new AppServiceImpl(headRepositoryForFileSystem, view);
    }
    @Test
    public void initReturnTrueTest(){
        when(headRepositoryForFileSystem.saveHead(new Head())).thenReturn(true);

        boolean result = appService.init();

        assertTrue(result);
    }

    @Test
    public void initReturnFalseTest() throws IOException {
        when(Files.createDirectories(PathResolver.getObjectsDirectoryPath())).
                thenThrow(new IOException());

        boolean result = appService.init();
        PowerMockito.verifyStatic();
        Files.createDirectories(PathResolver.getObjectsDirectoryPath());
        assertFalse(result);
    }
}
