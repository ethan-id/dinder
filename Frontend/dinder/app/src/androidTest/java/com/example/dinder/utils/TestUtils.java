package com.example.dinder.utils;

public class TestUtils {
    public static void awaitTransition(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
