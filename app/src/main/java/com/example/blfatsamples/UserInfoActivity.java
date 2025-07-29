package com.example.blfatsamples;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.blfatsamples.adapter.ProductAdapter;
import com.example.blfatsamples.constants.ApiQueue;
import com.example.blfatsamples.constants.Constant;
import com.example.blfatsamples.constants.ConstantUrl;
import com.example.blfatsamples.model.ProductModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserInfoActivity extends AppCompatActivity {

    private TextInputEditText inputName, inputEmail, inputPhone, inputAddress;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_info);

        inputName = findViewById(R.id.userinfo_input_name);
        inputEmail = findViewById(R.id.userinfo_input_email);
        inputPhone = findViewById(R.id.userinfo_input_phone);
        inputAddress = findViewById(R.id.userinfo_input_address);
        Button btnSave = findViewById(R.id.userinfo_btn_save);

        setUpNavigation();

        loadUserData();

        btnSave.setOnClickListener(v -> saveUserData());
    }

    private void loadUserData() {

        String url = ConstantUrl.GetCurrentUser;

        // Create a StringRequest

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject userObject) {
                        try {
                            // Get the nested User object
                            JSONObject user = userObject.getJSONObject("User");

                            // Extract user information
                            String name = user.optString("Name", "");
                            String email = user.optString("Email", "");
                            String phone = user.optString("PhoneNumber", "");
                            String address = user.optString("Address", "");

                            // Set the values to the input fields
                            inputName.setText(name.isEmpty() ? "Nome não informado" : name);
                            inputEmail.setText(email.isEmpty() ? "E-mail não informado" : email);
                            inputPhone.setText(phone.isEmpty() ? "Telefone não informado" : formatPhoneNumber(phone));
                            inputAddress.setText(address.isEmpty() ? "Endereço não informado" : address);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSONError", "Error parsing JSON: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null) {
                        int statusCode = error.networkResponse.statusCode;

                        switch (statusCode) {
                            case 401: // unauthorized
                                Toast.makeText(UserInfoActivity.this, "Usuário não autorizado.", Toast.LENGTH_SHORT).show();
                                logout();
                            default:
                                break;
                        }

                        findViewById(R.id.userinfo_btn_save).setActivated(false);
                        Toast.makeText(UserInfoActivity.this, "Falha em coleta de dados do usuário.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        // Add the request to the RequestQueue using the singleton
        ApiQueue.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    private void logout(){
        Constant.setUserInfo(null);
        startActivity(new Intent(this, LoginActivity.class));
    }

    private  void setUpNavigation()
    {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setSelectedItemId(R.id.menu_profile);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_home){
                startActivity(new Intent(this, MainActivity.class));
                return true;
            }
            if (itemId == R.id.menu_menu){
                return true;
            }
            if (itemId == R.id.menu_cart){
                return true;
            }
            if (itemId == R.id.menu_profile){
                // do nothing, you're already in this screen
                return true;
            }
            return false;
        });
    }

    private void saveUserData()
    {
        // TODO: Update user data
    }

    private String formatPhoneNumber(String phone) {
        if (phone == null || phone.isEmpty()) return "";

        // Simple formatting - adjust according to your needs
        if (phone.length() == 11) {
            return String.format("(%s) %s-%s",
                    phone.substring(0, 2),
                    phone.substring(2, 7),
                    phone.substring(7));
        }
        return phone;
    }
}