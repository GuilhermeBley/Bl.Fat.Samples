package com.example.blfatsamples;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
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
import com.example.blfatsamples.model.UserLoginResultModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserInfoActivity extends AppCompatActivity {
    private UserLoginResultModel userInfo;
    private TextInputEditText inputName, inputEmail, inputPhone, inputAddress;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_info);

        userInfo = Constant.getUserInfo();

        if (userInfo == null){
            startActivity(new Intent(this, LoginActivity.class));
        }

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
        String token = userInfo.getToken();

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
                            JSONObject user = userObject.getJSONObject("user");

                            // Extract user information
                            String name = user.isNull("name") ? "" : user.optString("name", "");
                            String email = user.isNull("email") ? "" : user.optString("email", "");
                            String phone = user.isNull("phoneNumber") ? "" : user.optString("phoneNumber", "");
                            String address = user.isNull("address") ? "" : user.optString("address", "");

                            // Set the values to the input fields
                            inputName.setText(name.isEmpty() ? "" : name);
                            inputEmail.setText(email.isEmpty() ? "" : email);
                            inputPhone.setText(phone.isEmpty() ? "" : formatPhoneNumber(phone));
                            inputAddress.setText(address.isEmpty() ? "" : address);
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
            }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

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