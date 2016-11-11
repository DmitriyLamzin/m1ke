package com.github.dmitriylamzin.service;

import com.github.dmitriylamzin.domain.Branch;
import com.github.dmitriylamzin.domain.Head;
import com.github.dmitriylamzin.repository.BranchRepository;
import com.github.dmitriylamzin.repository.HeadRepository;
import com.github.dmitriylamzin.view.View;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BranchServiceImplTest {

    private static final String BRANCH_NAME = "test_branch";
    private static final String BRANCH_NOT_CREATED = "branch.has.not.been.created";
    private static final String NAME_NOT_SPECIFIED = "branch.name.is.not.specified";
    private static final String BRANCH_CREATED = "branch.has.been.created";
    private static final String BRANCH_IS_CHOSEN = "branch.is.chosen";
    private static final String BRANCH_IS_NOT_CHOSEN = "branch.is.not.chosen";
    private static final String BRANCH_IS_NOT_DELETED = "branch.is.not.deleted";
    private static final String BRANCH_IS_ACTIVE = "branch.is.active";
    private static final String BRANCH_IS_DELETED = "branch.is.deleted";
    @Mock
    private View view;
    @Mock
    private HeadRepository headRepository;
    @Mock
    private CommitService commitService;
    @Mock
    private BranchRepository branchRepository;

    private BranchServiceImpl branchService;

    public BranchServiceImplTest() {
        this.branchService = new BranchServiceImpl();

    }

    @Before
    public void setUp(){
        initMocks(this);
        branchService.setBranchRepository(branchRepository);
        branchService.setHeadRepository(headRepository);
        branchService.setCommitService(commitService);
        branchService.setView(view);
    }
    @Test
    public void createBranchReturnBranchNotCreated(){
        when(branchRepository.saveBranch((Branch)anyObject())).thenReturn(false);
        when(headRepository.getHead()).thenReturn(new Head());

        String result = branchService.createBranch(BRANCH_NAME);

        assertEquals(BRANCH_NOT_CREATED, result);
    }

    @Test
    public void createBranchReturnNameIsNotSpecified(){

        String result = branchService.createBranch();

        assertEquals(NAME_NOT_SPECIFIED, result);
    }

    @Test
    public void createBranchReturnBranchHasBeenCreated(){
        when(branchRepository.saveBranch((Branch)anyObject())).thenReturn(true);
        when(headRepository.getHead()).thenReturn(new Head());

        String result = branchService.createBranch(BRANCH_NAME);

        assertEquals(BRANCH_CREATED, result);
    }

    @Test
    public void getBranchReturnBranchHasBeenCreated(){
        when(branchRepository.getBranch(BRANCH_NAME)).thenReturn(new Branch());
        when(headRepository.getHead()).thenReturn(new Head());
        when(headRepository.saveHead((Head)any())).thenReturn(true);

        String result = branchService.getBranch(BRANCH_NAME);

        assertEquals(BRANCH_IS_CHOSEN, result);
    }

    @Test
    public void getBranchReturnBranchIsNotChosen(){
        when(branchRepository.getBranch(BRANCH_NAME)).thenReturn(new Branch());
        when(headRepository.getHead()).thenReturn(new Head());

        String result = branchService.getBranch(BRANCH_NAME);

        assertEquals(BRANCH_IS_NOT_CHOSEN, result);
    }

    @Test
    public void removeBranchReturnBranchIsNotDeleted(){
        when(branchRepository.getBranch(BRANCH_NAME)).thenReturn(null);
        when(headRepository.getHead()).thenReturn(new Head());

        String result = branchService.removeBranch(BRANCH_NAME);

        assertEquals(BRANCH_IS_NOT_DELETED, result);
    }

    @Test
        public void removeBranchReturnBranchIsActive(){
        Branch branch = new Branch(BRANCH_NAME);
        Head head = new Head();
        head.setCurrentBranch(branch);
        when(branchRepository.getBranch(BRANCH_NAME)).thenReturn(branch);
        when(headRepository.getHead()).thenReturn(head);

        String result = branchService.removeBranch(BRANCH_NAME);

        assertEquals(BRANCH_IS_ACTIVE, result);
    }

    @Test
    public void removeBranchReturnBranchIsDeleted(){
        Branch branchInHead = new Branch("");
        Head head = new Head();
        head.setCurrentBranch(branchInHead);
        when(branchRepository.deleteBranch(BRANCH_NAME)).thenReturn(true);
        when(branchRepository.getBranch(BRANCH_NAME)).thenReturn(new Branch(BRANCH_NAME));
        when(headRepository.getHead()).thenReturn(head);

        String result = branchService.removeBranch(BRANCH_NAME);

        assertEquals(BRANCH_IS_DELETED, result);
    }
}
