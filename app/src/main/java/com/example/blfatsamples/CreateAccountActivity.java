package com.example.blfatsamples;

import android.text.TextUtils;
import android.os.Bundle;
import android.util.Patterns;
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
import com.example.blfatsamples.constants.ConstantUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.create_account_activity);

        findViewById(R.id.registerButton)
            .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createUser();
                }
            });
    }

    private  void createUser(){
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        EditText nameEditText = findViewById(R.id.nameEditText);
        EditText confirmPassowrdEditText = findViewById(R.id.confirmPasswordEditText);
        Button submitButton = findViewById(R.id.registerButton);
        Button redirectButton = findViewById(R.id.redirectLogin);

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();
        String confirmPassowrd = confirmPassowrdEditText.getText().toString().trim();

        boolean containsAnyError = false;
        // Validate email
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("E-mail é obrigatório.");
            containsAnyError = true;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Coloque um e-mail válido.");
            containsAnyError = true;
        }

        // Validate password
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Insira uma senha.");
            containsAnyError = true;
        } else if (password.length() < 8) {
            passwordEditText.setError("A senha deve conter no mínimo 8 caracteres.");
            containsAnyError = true;
        } else if (!confirmPassowrd.equals(password)){
            confirmPassowrdEditText.setError("As senhas não são iguais.");
            containsAnyError = true;
        }

        // Validate name
        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Nome é obrigatório.");
            containsAnyError = true;
        } else if (name.length() < 3) {
            nameEditText.setError("Nome muito curto, tente um mais longo.");
            containsAnyError = true;
        }

        if (containsAnyError) return;

        // If all validations pass, create the user
        try {
            JSONObject userJson = new JSONObject();
            userJson.put("email", email);
            userJson.put("password", password);
            userJson.put("name", name);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    ConstantUrl.PostUser,
                    userJson,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (response.has("user")) {
                                Toast.makeText(CreateAccountActivity.this, "Usuário registrado com sucesso! Efetue o login.", Toast.LENGTH_SHORT).show();
                                var inputsToDisable = new EditText[] {
                                  emailEditText, nameEditText, confirmPassowrdEditText, passwordEditText
                                };
                                for (EditText e : inputsToDisable) {
                                    e.setFocusable(false);
                                }
                                submitButton.setVisibility(View.INVISIBLE);
                                findViewById(R.id.loginText).setVisibility(View.INVISIBLE);
                                redirectButton.setVisibility(View.VISIBLE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.networkResponse != null) {
                                int statusCode = error.networkResponse.statusCode;

                                switch (statusCode) {
                                    case 400: // bad request

                                        break;
                                    case 409: // conflict
                                        EditText emailEditText = findViewById(R.id.emailEditText);
                                        emailEditText.setError("E-mail já registrado, tente outro.");
                                        return;
                                    default:
                                        break;
                                }

                                Toast.makeText(CreateAccountActivity.this,
                                        "Falha no registro de usuário.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            // Add the request to the RequestQueue using the singleton
            ApiQueue.getInstance(this).addToRequestQueue(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating user data", Toast.LENGTH_SHORT).show();
        }
    }
}
