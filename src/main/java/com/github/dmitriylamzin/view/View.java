package com.github.dmitriylamzin.view;


import java.util.ArrayList;

public interface View {
    void showInfo(ArrayList<String> info);
    void showInfo(String info);

    String processDialog(String question);
}
