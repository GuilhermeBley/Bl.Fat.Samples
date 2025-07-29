package com.example.blfatsamples;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

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
        // TODO: get user info
        inputName.setText("João Silva");
        inputEmail.setText("joao@exemplo.com");
        inputPhone.setText("(11) 99999-9999");
        inputAddress.setText("Rua Exemplo, 123 - São Paulo/SP");
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

    }
}