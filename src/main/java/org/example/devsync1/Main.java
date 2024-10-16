package org.example.devsync1;

import org.example.devsync1.scheduler.TokenModifyScheduler;

public class Main {
    public static void main(String[] args) {
        TokenModifyScheduler tokenScheduler = new TokenModifyScheduler();
        tokenScheduler.startScheduler();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            tokenScheduler.stopScheduler();
        }));
    }}
