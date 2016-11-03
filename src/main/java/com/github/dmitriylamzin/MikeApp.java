package com.github.dmitriylamzin;

import com.github.dmitriylamzin.controller.CommandController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class MikeApp {


    public static void main(String ... args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("appContext.xml");
        CommandController commandController = applicationContext.getBean(CommandController.class);

        commandController.processCommand(args);
    }
}
