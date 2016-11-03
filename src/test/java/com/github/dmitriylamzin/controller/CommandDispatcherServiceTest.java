package com.github.dmitriylamzin.controller;

import com.github.dmitriylamzin.service.AppService;
import com.github.dmitriylamzin.service.BranchService;
import com.github.dmitriylamzin.service.CommandDispatcherService;
import com.github.dmitriylamzin.service.IntegrationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


@RunWith(Parameterized.class)
public class CommandDispatcherServiceTest {
    private CommandDispatcherService dispatcher = new CommandDispatcherService();

    @Mock
    private AppService appService;
    @Mock
    private BranchService branchService;
    @Mock
    private IntegrationService integrationService;

    private String command;
    private ArrayList<String> expectedResult;


    public CommandDispatcherServiceTest(String command, ArrayList<String> expectedResult) {
        this.command = command;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        return Arrays.asList(new Object[][]{
                {"init", new ArrayList<>(Arrays.asList("m1ke.is.initialized"))},
                {"integrate",
                        new ArrayList<>(Arrays.asList("m1ke.integration.success", "file1", "file2"))},
                {"save",
                        new ArrayList<>(Arrays.asList("m1ke.save.proceed.status", "success"))},
                {"create-branch",
                        new ArrayList<>(Arrays.asList("m1ke.branch.status", "branch created"))},
                {"get-branch",
                        new ArrayList<>(Arrays.asList("m1ke.branch.status", "branch got"))},
                {"remove-branch",
                        new ArrayList<>(Arrays.asList("m1ke.branch.status", "branch removed"))},
                {"quit", new ArrayList<>(Arrays.asList("m1ke.is.not.quit"))},

        });
    }

    @Before
    public void setUp(){
        initMocks(this);
        this.dispatcher = new CommandDispatcherService();
        dispatcher.setAppService(appService);
        dispatcher.setBranchService(branchService);
        dispatcher.setIntegrationService(integrationService);

        when(appService.init()).thenReturn(true);
        when(appService.quit()).thenReturn(false);

        when(branchService.createBranch()).thenReturn("branch created");

        when(branchService.getBranch()).thenReturn("branch got");
        when(branchService.removeBranch()).thenReturn("branch removed");
        when(integrationService.integrate()).thenReturn(
                new ArrayList<>(Arrays.asList("file1", "file2")));
        when(integrationService.save()).thenReturn("success");

    }

    @Test
    public void dispatchCommandTest(){
        ArrayList<String> result = dispatcher.dispatchCommand(command);

        assertEquals(expectedResult, result);
    }

    @Test(expected = NullPointerException.class)
    public void dispatchCommandTestThrowsException(){
        dispatcher.dispatchCommand("NOT EXISTED COMMAND");
    }
}
