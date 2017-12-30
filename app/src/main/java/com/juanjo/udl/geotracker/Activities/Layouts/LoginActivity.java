package com.juanjo.udl.geotracker.Activities.Layouts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.juanjo.udl.geotracker.Activities.GlobalActivity.GlobalAppCompatActivity;
import com.juanjo.udl.geotracker.JSONObjects.JSONUser;
import com.juanjo.udl.geotracker.Management.DataManagement;
import com.juanjo.udl.geotracker.R;
import com.juanjo.udl.geotracker.Utilities.DataHandler;

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
                showDialog();
                DataHandler h = new DataHandler(LoginActivity.this) {
                    @Override
                    protected void isOk(Object obj) throws Exception {
                        JSONUser user = new JSONUser(LoginActivity.this, mail.getText().toString(), pass.getText().toString());
                        //User this non-used class to pass the user login information between activities
                        //It works as a temporal use, before establish a more secure system
                        Intent in = new Intent(LoginActivity.this, ProjectSelectActivity.class);
                        in.putExtra("user", user);
                        startActivity(in);
                    }
                };
                if(isConnected()){
                    if(checkFields()){
                        try {
                            dataManagement.login(mail.getText().toString(), pass.getText().toString(), h);
                        } catch (Exception e) {
                            processException(e);
                        }
                    }//If the fields are ok
                    else {
                        dismissDialog();
                    }
                }//If there are connection
                else {
                    noConectionError();
                    dismissDialog();
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
