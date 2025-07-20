package com.example.blfatsamples;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.blfatsamples.model.UserLoginResultModel;
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
                loginUserAsync();
            }
        });
    }


    @SuppressLint("StaticFieldLeak")
    private void loginUserAsync() {
        setLoading(true);
        String login = ((EditText)findViewById(R.id.emailEditText)).getText().toString();
        String password = ((EditText)findViewById(R.id.passwordEditText)).getText().toString();
        new AsyncTask<Void, Void, UserLoginResultModel>() {
            @Override
            protected UserLoginResultModel doInBackground(Void... voids) {
                try {
                    return new IdentityService().login(login, password).get();
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(UserLoginResultModel userInfo) {
                setLoading(false);
                startMainActivity(userInfo);
            }
        }.execute();
    }

    private void setLoading(boolean isLoading) {
        Button btn = findViewById(R.id.loginButton);
        if (isLoading){
            btn.setEnabled(false);
            btn.setText("Logando...");
        }else{
            btn.setEnabled(true);
            btn.setText("Login");
        }
    }

    private void startMainActivity(UserLoginResultModel userInfo) {

        if (userInfo == null) {
            Toast.makeText(LoginActivity.this,
                    "Login ou senha inválidos...",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(LoginActivity.this,
                "Logado com sucesso!!!",
                Toast.LENGTH_SHORT).show();
        // Start next activity with the user info
        // Intent intent = new Intent(this, MainActivity.class);
        // intent.putExtra("USER_INFO", userInfo);
        // startActivity(intent);
    }
}
