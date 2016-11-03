package com.github.dmitriylamzin.view;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

@Component
public class TerminalView implements View {
    private final static String MESSAGE_PROPERTIES = "msg.properties";
    private final Logger logger = Logger.getLogger(this.getClass());


    @Override
    public void showInfo(ArrayList<String> info) {
        List<String> resultList = getListStringFromProperties(info);
        for (String infoString : resultList){
            System.out.println(infoString);
        }
    }

    @Override
    public void showInfo(String info) {
        System.out.println(getStringFromProperties(info));
    }


    @Override
    public String processDialog(String question) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(getStringFromProperties(question));
        String answer = scanner.next();
        scanner.close();
        return answer;
    }

    public String getStringFromProperties(String stringToProceed){
        String resultString = null;
        Properties properties = new Properties();
        InputStream inputStream;
        try {
            inputStream = this.getClass().getClassLoader().getResourceAsStream(MESSAGE_PROPERTIES);
            properties.load(inputStream);
            String result = properties.getProperty(stringToProceed);
            if (result == null){
                resultString = stringToProceed;
            }else {
                resultString = result;
            }

            inputStream.close();
        }catch (IOException e){
            logger.error(e.getMessage(), e);
        }
        return resultString;
    }

    public List<String> getListStringFromProperties(List<String> stringsToProceed){
        List<String> resultList = new ArrayList<>();
        Properties properties = new Properties();
        InputStream inputStream;
        try {
            inputStream = this.getClass().getClassLoader().getResourceAsStream(MESSAGE_PROPERTIES);
            properties.load(inputStream);
            for (String stringToProceed : stringsToProceed){
                String result = properties.getProperty(stringToProceed);
                if (result == null){
                    resultList.add(stringToProceed);
                }else {
                    resultList.add(result);
                }
            }
            inputStream.close();
        }catch (IOException e){
            logger.error(e.getMessage(), e);
        }
        return resultList;
    }

}
