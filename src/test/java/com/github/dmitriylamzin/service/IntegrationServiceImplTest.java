package com.github.dmitriylamzin.service;

import com.github.dmitriylamzin.domain.Branch;
import com.github.dmitriylamzin.domain.Commit;
import com.github.dmitriylamzin.domain.Head;
import com.github.dmitriylamzin.domain.IntegrationResult;
import com.github.dmitriylamzin.repository.HeadRepositoryForFileSystem;
import com.github.dmitriylamzin.repository.IntegrationResultRepository;
import com.github.dmitriylamzin.view.View;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FileUtils.class)
public class IntegrationServiceImplTest {
    private static final String DEFAULT_BRANCH = "master";
    private static final String NO_CHANGES = "no.changes";
    private static final String SAVE_MESSAGE = "test save message";
    private static final String SAVE_MESSAGE_RESULT = "committed test save message";
    @Mock
    private View view;
    @Mock
    private HeadRepositoryForFileSystem headRepositoryForFileSystem;
    @Mock
    private BranchService branchService;
    @Mock
    private CommitService commitService;
    @Mock
    private IntegrationResultRepository integrationResultRepository;

    private IntegrationServiceImpl integrationService;

    public IntegrationServiceImplTest() {
        PowerMockito.mockStatic(FileUtils.class);
    }

    @Before
    public void setUp(){
        initMocks(this);
        this.integrationService = new IntegrationServiceImpl();
        integrationService.setBranchService(branchService);
        integrationService.setCommitService(commitService);
        integrationService.setHeadRepositoryForFileSystem(headRepositoryForFileSystem);
        integrationService.setIntegrationResultRepository(integrationResultRepository);
        integrationService.setView(view);
    }

    @Test
    public void integrationMethodCreatesDefaultBranch(){
        when(headRepositoryForFileSystem.getHead()).thenReturn(new Head());
        integrationService.integrate();
        verify(branchService).createBranch(DEFAULT_BRANCH);
        verify(branchService).getBranch(DEFAULT_BRANCH);
    }


    @Test
    public void saveMethodReturnsNoChanges(){
        Head head = new Head();
        Branch branch = new Branch();
        branch.setLastCommit(new Commit());
        head.setLastCommitNumber(0L);
        head.setCurrentBranch(branch);
        when(integrationResultRepository.getIntegrationResult()).thenReturn(new IntegrationResult());
        when(headRepositoryForFileSystem.getHead()).thenReturn(head);

        String result = integrationService.save();
        assertEquals(NO_CHANGES, result);
    }

    @Test
    public void saveMethodSavesCommit(){
        Head head = new Head();
        Branch branch = new Branch();
        head.setLastCommitNumber(0L);
        head.setCurrentBranch(branch);
        IntegrationResult integrationResult = new IntegrationResult();
        integrationResult.setNewFiles(Arrays.asList("1", "2", "3"));
        when(integrationResultRepository.getIntegrationResult()).thenReturn(integrationResult);
        when(headRepositoryForFileSystem.getHead()).thenReturn(head);


        Commit commit = new Commit();
        when(commitService.saveTrackedFile(commit, "1")).thenReturn("1");
        when(commitService.saveTrackedFile(commit, "2")).thenReturn("1");
        when(commitService.saveTrackedFile(commit, "3")).thenReturn("1");

        String result = integrationService.save("-m", SAVE_MESSAGE);


        assertEquals(SAVE_MESSAGE_RESULT, result);
    }

    @Test(expected = NullPointerException.class)
    public void saveMethodThrowsNullPointerException(){
        Head head = new Head();
        Branch branch = new Branch();
        head.setLastCommitNumber(0L);
        head.setCurrentBranch(branch);
        IntegrationResult integrationResult = new IntegrationResult();
        integrationResult.setNewFiles(Arrays.asList("1", "2", "3"));
        when(integrationResultRepository.getIntegrationResult()).thenReturn(integrationResult);
        when(headRepositoryForFileSystem.getHead()).thenReturn(head);


        Commit commit = new Commit();
        when(commitService.saveTrackedFile(commit, "1")).thenReturn("1");
        when(commitService.saveTrackedFile(commit, "2")).thenReturn("1");
        when(commitService.saveTrackedFile(commit, "3")).thenReturn("1");

        String result = integrationService.save(SAVE_MESSAGE);
    }


}
