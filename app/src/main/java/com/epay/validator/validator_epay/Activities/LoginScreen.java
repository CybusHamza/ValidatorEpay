package com.epay.validator.validator_epay.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.epay.validator.validator_epay.R;

public class LoginScreen extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String driverName,operatorId,PinCode;
    EditText etDriverName,etPass;
    Button btnLogin;

    Toolbar toolbar;

    String isLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Login Screen");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPreferences = getSharedPreferences("OperatorInfo", MODE_PRIVATE);
        editor=sharedPreferences.edit();
        driverName=sharedPreferences.getString("driverName","");
        PinCode=sharedPreferences.getString("Pincode","");
        operatorId=sharedPreferences.getString("operator","");
        isLogin=sharedPreferences.getString("login","");
        etDriverName= (EditText) findViewById(R.id.etdriverName);
        etPass= (EditText) findViewById(R.id.etdriverPass);
        btnLogin= (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(driverName.equals(etDriverName.getText().toString())&& PinCode.equals(etPass.getText().toString())){
                    editor.putString("login","true");
                    editor.commit();
                    Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(),"Incorrect Name or Password",Toast.LENGTH_LONG).show();
                }
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(LoginScreen.this, SetupScreen.class);
        finish();
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
