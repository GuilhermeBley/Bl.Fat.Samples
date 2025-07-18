package com.example.blfatsamples;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.blfatsamples.services.IdentityService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private final Executor backgroundExecutor = Executors.newSingleThreadExecutor();
    private final Executor mainThreadExecutor = runnable -> runOnUiThread(runnable);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.login_activity);

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the user info async here
            }
        });
    }

    private void loginUserAsync() {
        // Show loading state
        setLoading(true);

        CompletableFuture.supplyAsync(() -> {
            // Background work (network call)
            return new IdentityService().("username", "password");
        }, backgroundExecutor)
        .thenApplyAsync(userInfo -> {
            // Process result on background thread if needed
            return userInfo;
        }, backgroundExecutor)
        .thenAcceptAsync(userInfo -> {
            // Update UI on main thread
            setLoading(false);
            startMainActivity(userInfo);
        }, mainThreadExecutor)
        .exceptionally(ex -> {

            setLoading(false);
            Toast.makeText(LoginActivity.this,
                    "Falha ao realizar o login.",
                    Toast.LENGTH_SHORT).show();
            return null;
        }, mainThreadExecutor);
    }

    private void setLoading(boolean isLoading) {
        findViewById(R.id.loginButton).setEnabled(!isLoading);
    }

    private void startMainActivity(UserInfo userInfo) {
        // Start next activity with the user info
        // Intent intent = new Intent(this, MainActivity.class);
        // intent.putExtra("USER_INFO", userInfo);
        // startActivity(intent);
    }
}
