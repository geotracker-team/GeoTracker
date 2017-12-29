package com.juanjo.udl.geotracker.Activities.Layouts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
                showDialog();
                Handler h = new Handler(){
                    @Override
                    public void handleMessage(Message msg){
                        if(msg.what == 0){
                            Intent in = new Intent(LoginActivity.this, ProjectSelectActivity.class);
                            startActivity(in);
                        } else {
                            processException(new Exception((String)msg.obj));
                        }//check login
                        dismissDialog();
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
