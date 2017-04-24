package com.plebs.plebbit.plebbit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import com.plebbit.plebbit.IPlebbit;

/**
 * Created by Morten on 4/18/2017.
 */

public class LoginActivity extends Activity implements View.OnClickListener{

    private Button loginButton;
    private Button forgotButton;
    private EditText usernameEdit;
    private EditText passwordEdit;
    private IPlebbit plebbit;


    public LoginActivity(){
        try{
            URL plebbitUrl = new URL("http://gibbo.dk:9427/plebbit?wsdl");
            QName plebbitQName = new QName("http://plebbit.plebbit.com/", "PlebbitLogicService");
            Service plebbitService = Service.create(plebbitUrl, plebbitQName);
            plebbit = plebbitService.getPort(IPlebbit.class);
        } catch(MalformedURLException e){
            e.printStackTrace();
            System.exit(1); //just end if no internet for now
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_layout);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        forgotButton = (Button) findViewById(R.id.forgotPassword);
        forgotButton.setOnClickListener(this);

        usernameEdit = (EditText) findViewById(R.id.usernameTextEdit);
        passwordEdit = (EditText) findViewById(R.id.passwordTextEdit);

    }

    @Override
    public void onClick(View v) {
        if(v == loginButton){
            login();
        } else if(v == forgotButton){
            forgotPassword();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void login(){
        if(usernameEdit.getText().toString().length() > 0 && passwordEdit.getText().toString().length() > 0){
            String token = plebbit.login(usernameEdit.getText().toString(), passwordEdit.getText().toString());
            if(token != null){
                Intent mainIntent = new Intent(this, MainActivity.class);
                mainIntent.putExtra("token", token);
                startActivity(mainIntent);
                //save and go to next scene
            } else {
                //inform user it was incorrect
            }
        }
    }

    private void forgotPassword(){
        Intent forgotIntent = new Intent(this, LoginActivity.class);
        forgotIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(forgotIntent);
    }

}
