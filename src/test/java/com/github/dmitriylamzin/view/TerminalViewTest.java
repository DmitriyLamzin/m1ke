package com.github.dmitriylamzin.view;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

/**
 * Created by Dmitriy on 27.10.2016.
 */
public class TerminalViewTest {
    View view = new TerminalView();

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Rule
    public final TextFromStandardInputStream systemInMock
            = emptyStandardInputStream();

    @Test
    public void showInfoTest() {
        ArrayList<String> testInfo = new ArrayList<>();
        String firstString = "test String 1";
        String secondString = "test String 2";

        testInfo.add(firstString);
        testInfo.add(secondString);
        view.showInfo(testInfo);
        assertEquals("test String 1\r\ntest String 2\r\n", systemOutRule.getLog());
    }

    @Test
    public void processDialogTest(){
        systemInMock.provideLines("y");
        String questionString = "question String";
        String returnValue = view.processDialog(questionString);

        assertEquals(questionString + "\r\n", systemOutRule.getLog());
        assertEquals(returnValue, "y");


    }
}
