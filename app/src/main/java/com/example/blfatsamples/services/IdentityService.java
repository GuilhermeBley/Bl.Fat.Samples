package com.example.blfatsamples.services;

import com.example.blfatsamples.model.UserLoginResultModel;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class IdentityService {
    public CompletableFuture<UserLoginResultModel> login(String email, String password) {
        CompletableFuture<UserLoginResultModel> future = new CompletableFuture<>();

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            try {
                if ("guilherme@email.com".equals(email) && "123".equals(password)) {
                    future.complete(new UserLoginResultModel("Guilherme", "Bley", "guilherme@email.com"));
                } else {
                    future.complete(null);
                }
            } finally {
                scheduler.shutdown();
            }
        }, 2, TimeUnit.SECONDS);

        return future;
    }
}
