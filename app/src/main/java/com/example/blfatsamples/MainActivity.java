package com.example.blfatsamples;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.blfatsamples.constants.Constant;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        String email = Constant.getUserInfo() != null  ? Constant.getUserInfo().getEmail()  : null;

        if (email == null)
        {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivities(new Intent[]{ intent });
            return;
        }

        findViewById(R.id.closeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });
    }
}