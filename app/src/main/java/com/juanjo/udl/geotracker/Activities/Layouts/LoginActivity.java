package com.juanjo.udl.geotracker.Activities.Layouts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.juanjo.udl.geotracker.Activities.GlobalActivity.GlobalActivity;
import com.juanjo.udl.geotracker.R;

public class LoginActivity extends GlobalActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(LoginActivity.this, GeneralMapActivity.class);
                startActivity(in);
            }
        });
    }
}
