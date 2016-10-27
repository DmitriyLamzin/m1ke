package com.github.dmitriylamzin.view;


import java.util.ArrayList;
import java.util.Scanner;

public class TerminalView implements View {

    @Override
    public void showInfo(ArrayList<String> info) {
        for (String infoString : info){
            System.out.println(infoString);
        }
    }

    @Override
    public void showInfo(String info) {
        System.out.println(info);
    }


    @Override
    public String processDialog(String question) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(question);
        String answer = scanner.next();
        scanner.close();
        return answer;
    }
}
