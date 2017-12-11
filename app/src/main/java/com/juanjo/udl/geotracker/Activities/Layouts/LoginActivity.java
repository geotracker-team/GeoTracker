package com.juanjo.udl.geotracker.Activities.Layouts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.juanjo.udl.geotracker.Activities.GlobalActivity.GlobalAppCompatActivity;
import com.juanjo.udl.geotracker.Management.DataManagement;
import com.juanjo.udl.geotracker.R;

public class LoginActivity extends GlobalAppCompatActivity {

    EditText mail, pass;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.hideActionBar();

        setContentView(R.layout.activity_login);
        dataManagement = new DataManagement(this);

        btnLogin = findViewById(R.id.btnLogin);
        mail = findViewById(R.id.mail);
        pass = findViewById(R.id.pass);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected()){
                    Intent in = new Intent(LoginActivity.this, ProjectSelectActivity.class);
                    if(checkFields()
                            && dataManagement.login(mail.getText().toString(), pass.getText().toString())) startActivity(in);
                }//If there are connection
                else {
                    noConectionError();
                }
            }
        });
    }//onCreate

    private boolean checkFields(){
        boolean ret = false;

        if(mail.getText().toString().isEmpty()) mail.setError(getString(R.string.txtNoFields));
        else if (pass.getText().toString().isEmpty())  pass.setError(getString(R.string.txtNoFields));
        else ret = true;

        return ret;
    }//checkFields
}
