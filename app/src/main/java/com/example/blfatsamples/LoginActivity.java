package com.example.blfatsamples;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.blfatsamples.constants.ApiQueue;
import com.example.blfatsamples.constants.Constant;
import com.example.blfatsamples.constants.ConstantUrl;
import com.example.blfatsamples.model.UserLoginResultModel;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences preference;

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

        findViewById(R.id.registerUserText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCreateAccount = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intentCreateAccount);
            }
        });

        preference = getSharedPreferences(Constant.SharedPreferenceLogin, MODE_PRIVATE);
        if (Constant.getUserInfo(preference) != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            return;
        }
    }

    private void loginUserAsync() {
        setLoading(true);
        String login = ((EditText)findViewById(R.id.emailEditText)).getText().toString();
        String password = ((EditText)findViewById(R.id.passwordEditText)).getText().toString();


        // If all validations pass, create the user
        try {
            JSONObject userJson = new JSONObject();
            userJson.put("Email ", login);
            userJson.put("Password", password);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    ConstantUrl.PostUserLogin,
                    userJson,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            setLoading(false);
                            try {
                                String token = response.getString("Token");
                                String email = response.getString("Email");
                                String name = response.getString("Name");
                                int id = response.getInt("Id");

                                Constant.setUserInfo(
                                        new UserLoginResultModel(id, name, token, email),
                                        preference);

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));

                            } catch (JSONException e) {
                                Toast.makeText(LoginActivity.this, "Falha no login. Tente novamente.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            setLoading(false);

                            if (error.networkResponse != null) {
                                int statusCode = error.networkResponse.statusCode;

                                switch (statusCode) {
                                    case 401: // bad request
                                        Toast.makeText(LoginActivity.this, "Login ou senha inválidos. Tente outro ou crie uma conta.", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        break;
                                }

                                Toast.makeText(LoginActivity.this, "Falha no login. Tente novamente.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            // Add the request to the RequestQueue using the singleton
            ApiQueue.getInstance(this).addToRequestQueue(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Falha ao logar.", Toast.LENGTH_SHORT).show();
        }

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
            Toast.makeText(LoginActivity.this, "Login ou senha inválidos...",  Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(LoginActivity.this, "Logado com sucesso!!!", Toast.LENGTH_SHORT).show();
        Intent intentLogin = new Intent(this, MainActivity.class);
        startActivities(new Intent[]{ intentLogin });
    }
}
