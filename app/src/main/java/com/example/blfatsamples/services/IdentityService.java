package com.example.blfatsamples.services;

import com.example.blfatsamples.model.UserLoginResultModel;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class IdentityService {
    public CompletableFuture<UserLoginResultModel> login(String email, String password){
        CompletableFuture<UserLoginResultModel> future = new CompletableFuture<>();

        // fake loading simulating external api
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            future.complete(new UserLoginResultModel("Guilherme", "Bley", "guilherme@email.com")); // Complete after delay
            scheduler.shutdown();
        }, 2, TimeUnit.SECONDS);
        // ALTERAÇÃO TESTE
        return future;
    }
}
