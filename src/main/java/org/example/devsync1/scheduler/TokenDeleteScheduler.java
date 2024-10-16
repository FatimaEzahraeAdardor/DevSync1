package org.example.devsync1.scheduler;

import org.example.devsync1.entities.Token;
import org.example.devsync1.services.TokenService;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TokenDeleteScheduler {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final TokenService tokenService;


    public TokenDeleteScheduler() {
        this.tokenService = new TokenService();

    }

    public void startScheduler() {
        scheduler.scheduleAtFixedRate(this::checkAndUpdateDeleteTokenCounts, 0, 30, TimeUnit.DAYS);
    }

    private void checkAndUpdateDeleteTokenCounts(){
        List<Token> tokens = tokenService.findAll();

        for(Token token : tokens){
            token.setDeleteTokenCount(1);
            tokenService.update(token);
        }
    }

    public void stopScheduler() {
        scheduler.shutdown();
    }
}
