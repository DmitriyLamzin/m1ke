package com.github.dmitriylamzin.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class CommandDispatcherService {

    private final Logger log = Logger.getLogger(this.getClass());

    private static final String INIT = "init";
    private static final String INTEGRATE = "integrate";
    private static final String SAVE = "save";
    private static final String CREATE_BRANCH = "create-branch";
    private static final String GET_BRANCH = "get-branch";
    private static final String REMOVE_BRANCH = "remove-branch";
    private static final String QUIT = "quit";

    private static final String[] COMMANDS = {
            INIT,
            INTEGRATE,
            SAVE,
            CREATE_BRANCH,
            GET_BRANCH,
            REMOVE_BRANCH,
            QUIT
    };

    @Autowired
    private AppService appService;
//    @Autowired
    private BranchService branchService;
//    @Autowired
    private IntegrationService integrationService;


    private boolean exists(String expectedCommand){
        for (String command : COMMANDS){
            if (command.equals(expectedCommand)){
                log.debug("Command: " + expectedCommand + " exists");
                return true;
            }
        }
        log.debug("Command: " + expectedCommand + "doesn't exist");
        return false;
    }

    public ArrayList<String> dispatchCommand(String... args) throws NullPointerException{
        log.info("Dispatch command with args: " + args);
        ArrayList<String> response = new ArrayList<>();
        String command = args[0];
        String[] commandArgs = new String[0];
        if (args.length > 1){
            commandArgs = Arrays.copyOfRange(args, 1, args.length);
        }
        if (exists(command)){
            switch (command){
                case INIT:{
                    boolean isInitialized = appService.init();
                    if (isInitialized){
                        response.add("m1ke.is.initialized");
                    }else {
                        response.add("m1ke.is.not.initializes");
                    }
                    break;
                }case INTEGRATE: {
                    ArrayList<String> integrateResponse = integrationService.integrate();
                    if (integrateResponse.size() > 0){
                        response.add("m1ke.integration.success");
                        response.addAll(integrateResponse);
                    }else {
                        response.add("m1ke.integration.failed");
                    }
                    break;
                }case SAVE:{
                    String saveCommandResponse = integrationService.save(commandArgs);
                    response.add("m1ke.save.proceed.status");
                    response.add(saveCommandResponse);
                    break;
                }case CREATE_BRANCH:{
                    String createBranchResponse = branchService.createBranch(commandArgs);
                    response.add("m1ke.create.branch.proceed.status");
                    response.add(createBranchResponse);
                    break;
                }case GET_BRANCH:{
                    String getBranchResponse =  branchService.getBranch(commandArgs);
                    response.add("m1ke.get.branch.status");
                    response.add(getBranchResponse);
                    break;
                }case REMOVE_BRANCH:{
                    String removeBranchResponse = branchService.removeBranch(commandArgs);
                    response.add("m1ke.remove.branch.status");
                    response.add(removeBranchResponse);
                    break;
                }case QUIT:{
                    boolean isQuit = appService.quit();
                    if (isQuit){
                        response.add("m1ke.is.quit");
                    }else {
                        response.add("m1ke.is.not.quit");
                    }
                    break;
                }

            }
        }else {
            log.info("There is no such command " + command);
            throw new NullPointerException(command);
        }
        return response;
    }

    public void setAppService(AppService appService) {
        this.appService = appService;
    }

    public void setBranchService(BranchService branchService) {
        this.branchService = branchService;
    }

    public void setIntegrationService(IntegrationService integrationService) {
        this.integrationService = integrationService;
    }
}
