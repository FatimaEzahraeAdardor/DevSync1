package org.example.devsync1.scheduler;

import org.example.devsync1.entities.Request;
import org.example.devsync1.entities.Token;
import org.example.devsync1.enums.RequestStatus;
import org.example.devsync1.services.RequestService;
import org.example.devsync1.services.TokenService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TokenModifyScheduler {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final TokenService tokenService;
    private final RequestService requestService;

    public TokenModifyScheduler() {
        this.tokenService = new TokenService();
        this.requestService = new RequestService();
    }

    public void startScheduler() {
        scheduler.scheduleAtFixedRate(this::checkAndUpdateTokenCount, 0, 1, TimeUnit.DAYS);
    }

    private void checkAndUpdateTokenCount() {
        try {
            List<Token> tokens = tokenService.findAll();
            for (Token token : tokens) {
                List<Request> requests = requestService.findByUserId(token.getUser().getId());

                boolean hasOldPendingRequests = requests.stream()
                        .filter(request -> request.getStatus() == RequestStatus.PENDING)
                        .anyMatch(request -> Duration.between(request.getCreationDate(), LocalDateTime.now()).toHours() >= 12);
                if (hasOldPendingRequests) {
                    token.setModifyTokenCount(token.getModifyTokenCount() * 2);
                    System.out.println("Doubling modifyTokenCount for user: " + token.getUser().getId() + " due to pending requests for over 12 hours.");
                } else {
                    token.setModifyTokenCount(2);
                    System.out.println("Resetting modifyTokenCount to 2 for user: " + token.getUser().getId());
                }

                tokenService.update(token);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void stopScheduler() {
        scheduler.shutdown();
    }
}
