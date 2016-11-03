package com.github.dmitriylamzin.controller;

import com.github.dmitriylamzin.service.CommandDispatcherService;
import com.github.dmitriylamzin.view.View;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CommandControllerImpl implements CommandController {
    private final Logger log = Logger.getLogger(this.getClass());

    @Autowired
    View view;
    @Autowired
    CommandDispatcherService commandDispatcherService;

    public void processCommand(String... args) {
        try {
            view.showInfo(commandDispatcherService.dispatchCommand(args));
        }catch (NullPointerException e){
            log.error(e.getMessage(), e);
            view.showInfo("m1ke.command.not.recognized");
        }
    }
}
