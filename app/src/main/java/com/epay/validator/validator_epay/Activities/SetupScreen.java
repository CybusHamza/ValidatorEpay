package com.epay.validator.validator_epay.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.List;
import java.util.Map;

public class SetupScreen extends AppCompatActivity {


    String is_first;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;

    LinearLayout btnSetup,btnLogon;
    AlertDialog myalertdialog;
    Spinner terminal;
    private List<String> terminalList;
    TextView tvSetup;
    String isLogin;

    ProgressDialog ringProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_screen);
        btnSetup= (LinearLayout) findViewById(R.id.btn_setup);
        btnLogon= (LinearLayout) findViewById(R.id.btn_logon);
        tvSetup= (TextView) findViewById(R.id.btn_setup1);

        SharedPreferences sharedPreferences = getSharedPreferences("OperatorInfo", MODE_PRIVATE);
        is_first = sharedPreferences.getString("is_first", "false");
        isLogin=sharedPreferences.getString("login","");
        if (is_first.equals("true")) {
            tvSetup.setText("Change Setup");
            btnLogon.setVisibility(View.VISIBLE);
            /*Intent intent = new Intent(SetupScreen.this, MainActivity.class);
            finish();
            startActivity(intent);*/
        }
        if(isLogin.equals("true")){
            Intent intent = new Intent(SetupScreen.this, MainActivity.class);
            finish();
            startActivity(intent);
        }
        btnSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // dialog_Setup();
                Intent intent=new Intent(SetupScreen.this,SetupScreenExtended.class);
                finish();
                startActivity(intent);
            }
        });
        btnLogon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_first.equals("true")) {
                    Intent intent = new Intent(SetupScreen.this, LoginScreen.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(),"Please Setup first then login",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void dialog_Setup(){

        AlertDialog.Builder builder = new AlertDialog.Builder(SetupScreen.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_terminal_manager_pin_code, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        //final EditText code = (EditText) dialogView.findViewById(R.id.code);
        terminal = (Spinner) dialogView.findViewById(R.id.terminalSpinner);
        final EditText mngrCode = (EditText) dialogView.findViewById(R.id.managercode);
        Button send = (Button) dialogView.findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        myalertdialog = builder.create();
        myalertdialog.show();
        AllTerminals();
    }

    public void AllTerminals() {

        ringProgressDialog = ProgressDialog.show(SetupScreen.this, "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_SETUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();


                        JSONArray jsonArray = null;
                        try {
                          //  stake_list = new ArrayList<>();
                            terminalList = new ArrayList<>();
                          //  stakeID_list = new ArrayList<>();
                           // commissionList = new ArrayList<>();
                          //  stakeholder_CommissionList = new ArrayList<>();
                         //   manager_CommissionList = new ArrayList<>();
                          //  operater_CommissionList = new ArrayList<>();
                          //  commissionTypeList = new ArrayList<>();

                            jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                                terminalList.add(jsonObject.getString("terminal_name"));
                               /* stake_list.add(jsonObject.getString("first_name") + jsonObject.getString("last_name"));
                                stakeID_list.add(jsonObject.getString("stakeholder_id"));
                                commissionList.add(jsonObject.getString("total_Commission"));
                                stakeholder_CommissionList.add(jsonObject.getString("stakeholder_Commission"));
                                manager_CommissionList.add(jsonObject.getString("manager_Commission"));
                                operater_CommissionList.add(jsonObject.getString("operater_Commission"));
                                commissionTypeList.add(jsonObject.getString("Commission_type"));*/

                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                    (SetupScreen.this, R.layout.spinner_item, terminalList);

                            dataAdapter.setDropDownViewResource
                                    (android.R.layout.simple_spinner_dropdown_item);

                            terminal.setAdapter(dataAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    Toast.makeText(SetupScreen.this, "No connection Error", Toast.LENGTH_SHORT).show();

                } else if (error instanceof TimeoutError) {

                    Toast.makeText(SetupScreen.this, " connection Time out Error", Toast.LENGTH_SHORT).show();

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return new HashMap<>();
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(SetupScreen.this);
        requestQueue.add(request);

    }
}
