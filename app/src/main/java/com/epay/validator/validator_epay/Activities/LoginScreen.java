package com.epay.validator.validator_epay.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.epay.validator.validator_epay.Network.End_Points;
import com.epay.validator.validator_epay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginScreen extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String driverName,operatorId,PinCode,terminalName,terminalMangerCode;
    EditText etDriverName,etPass;
    Button btnLogin;

    Toolbar toolbar;

    String isLogin;
    ProgressDialog ringProgressDialog;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    String onlineTerminalName,onlinePassword,TerminalId;
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
        terminalName=sharedPreferences.getString("terminalName","");
        terminalMangerCode=sharedPreferences.getString("managerCode","");
        TerminalId=sharedPreferences.getString("terminalId","");
        etDriverName= (EditText) findViewById(R.id.etdriverName);
        etPass= (EditText) findViewById(R.id.etdriverPass);
        btnLogin= (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etDriverName.getText().toString().equals("") || etPass.getText().equals("")){
                    Toast.makeText(getApplicationContext(),"Please Enter Terminal Name and Manager Password",Toast.LENGTH_LONG).show();
                }else {
                    onlineTerminalName=etDriverName.getText().toString();
                    onlinePassword=etPass.getText().toString();
                    loginTerminal();
                }
                /*if(terminalName.equals(etDriverName.getText().toString())&& terminalMangerCode.equals(etPass.getText().toString())){
                    editor.putString("login","true");
                    editor.commit();
                    Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(),"Incorrect Name or Password",Toast.LENGTH_LONG).show();
                }*/
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
    public void loginTerminal() {

        ringProgressDialog = ProgressDialog.show(LoginScreen.this, "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.LOGIN_TERMINAL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();
                        String check=response.trim();
                        if(check.equals("\"false\"")) {
                            Toast.makeText(LoginScreen.this, "Incorrect Information", Toast.LENGTH_SHORT).show();
                        }else {
                            String TerminalName, ManagerPassword;

                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                //JSONObject object = new JSONObject(response);
                                TerminalName = jsonObject.getString("terminal_name");
                                ManagerPassword = jsonObject.getString("manager_password");

                                changeStatus();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    Toast.makeText(LoginScreen.this, "No connection Error", Toast.LENGTH_SHORT).show();

                } else if (error instanceof TimeoutError) {

                    Toast.makeText(LoginScreen.this, " connection Time out Error", Toast.LENGTH_SHORT).show();

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("terminal_name", onlineTerminalName);
                params.put("manager_password", onlinePassword);
                params.put("terminal_id", TerminalId);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(LoginScreen.this);
        requestQueue.add(request);

    }
    public  void changeStatus(){
        ringProgressDialog = ProgressDialog.show(LoginScreen.this, "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.LOGIN_STATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();
                        editor.putString("login","true");
                        editor.commit();
                        Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                        finish();
                        startActivity(intent);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    Toast.makeText(LoginScreen.this, "No connection Error", Toast.LENGTH_SHORT).show();

                } else if (error instanceof TimeoutError) {

                    Toast.makeText(LoginScreen.this, " connection Time out Error", Toast.LENGTH_SHORT).show();

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("live_sattus", "1");
                params.put("terminal_id", TerminalId);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(LoginScreen.this);
        requestQueue.add(request);
    }
}
