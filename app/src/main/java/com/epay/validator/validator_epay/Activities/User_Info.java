package com.epay.validator.validator_epay.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class User_Info extends AppCompatActivity {

    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    Spinner stakeholder, opperator, terminal,allTerminals;
    String client_id, operator_id;
    Button Save;
    EditText pincode,managercode;
    Toolbar toolbar;
    String is_first;
    ProgressDialog ringProgressDialog;
    private List<String> stake_list;
    private List<String> stakeID_list;
    private List<String> op_list;
    private List<String> opId_list;
    private List<String> buss_list;
    private List<String> bussId_list;
    private List<String> pincodelist;
    private List<String> busRegistrationList;
    private List<String> commissionList;
    private List<String> commissionTypeList;
    private List<String> terminalList;
    private List<String> terminalIdList;
    private List<String> terminalStanList;
    private List<String> managerCodeList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__info);

        SharedPreferences sharedPreferences = getSharedPreferences("OperatorInfo", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        is_first = sharedPreferences.getString("is_first", "false");

        if (is_first.equals("true")) {
            Intent intent = new Intent(User_Info.this, MainActivity.class);
            finish();
            startActivity(intent);
        } else {

            stakeholder = (Spinner) findViewById(R.id.stakeholder);
            opperator = (Spinner) findViewById(R.id.opperator);
            terminal = (Spinner) findViewById(R.id.terminal);
            allTerminals = (Spinner) findViewById(R.id.terminalSpinner);
            Save = (Button) findViewById(R.id.button);
            pincode = (EditText) findViewById(R.id.pincode);
            managercode = (EditText) findViewById(R.id.pincodeTManager);


            toolbar = (Toolbar) findViewById(R.id.app_bar);
            toolbar.setTitle("Select Information");
            setSupportActionBar(toolbar);
            toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

            Save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = (int) terminal.getSelectedItemId();
                    if (!opperator.getSelectedItem().toString().equals("No Records Founds")) {
                        if (!terminal.getSelectedItem().toString().equals("No Records Founds")) {
                            if (!allTerminals.getSelectedItem().toString().equals("No Records Founds")) {
                            if (pincodelist.get(id).equals(pincode.getText().toString())) {
                                if(managerCodeList.get((int) allTerminals.getSelectedItemId()).equals(managercode.getText().toString())){
                                    editor.putString("stakeholder", stakeID_list.get((int) stakeholder.getSelectedItemId()));
                                    editor.putString("commission",commissionList.get((int) stakeholder.getSelectedItemId()));
                                    editor.putString("commissionType",commissionTypeList.get((int) stakeholder.getSelectedItemId()));
                                    editor.putString("operator", opId_list.get((int) opperator.getSelectedItemId()));
                                    editor.putString("buss", bussId_list.get((int) terminal.getSelectedItemId()));
                                    editor.putString("Pincode", pincodelist.get(id));
                                    editor.putString("busNumber",busRegistrationList.get(id));
                                    editor.putString("terminalName",allTerminals.getSelectedItem().toString());
                                    editor.putString("terminalStan",terminalStanList.get((int) allTerminals.getSelectedItemId()));
                                    editor.putString("managerCode",managerCodeList.get((int) allTerminals.getSelectedItemId()));
                                    editor.putString("is_first", "true");
                                    editor.apply();

                                Intent intent = new Intent(User_Info.this, MainActivity.class);
                                finish();
                                startActivity(intent);
                            }else {
                                    Toast.makeText(User_Info.this, "Please Enter Valid Terminal Manager Pin Code", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(User_Info.this, "Please Enter Valid Pin Code", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                                Toast.makeText(User_Info.this, "Please Select Terminal", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(User_Info.this, "Please Select Driver", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(User_Info.this, "Please Select Operator", Toast.LENGTH_SHORT).show();

                    }


                }
            });
            stakeholder.setOnItemSelectedListener(new CustomOnItemSelectedListener_stake());
            opperator.setOnItemSelectedListener(new CustomOnItemSelectedListener_operator());

            Allstakeholder();
        }
    }

    public void Allstakeholder() {

        ringProgressDialog = ProgressDialog.show(User_Info.this, "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.GETSTAKE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();
                        JSONArray jsonArray = null;
                        try {
                            stake_list = new ArrayList<>();
                            stakeID_list = new ArrayList<>();
                            commissionList = new ArrayList<>();
                            commissionTypeList = new ArrayList<>();

                            jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                                stake_list.add(jsonObject.getString("first_name") + jsonObject.getString("last_name"));
                                stakeID_list.add(jsonObject.getString("stakeholder_id"));
                                commissionList.add(jsonObject.getString("commission"));
                                commissionTypeList.add(jsonObject.getString("commission_type"));

                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                    (User_Info.this, R.layout.spinner_item, stake_list);

                            dataAdapter.setDropDownViewResource
                                    (android.R.layout.simple_spinner_dropdown_item);

                            stakeholder.setAdapter(dataAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    Toast.makeText(User_Info.this, "No connection Error", Toast.LENGTH_SHORT).show();

                } else if (error instanceof TimeoutError) {

                    Toast.makeText(User_Info.this, " connection Time out Error", Toast.LENGTH_SHORT).show();

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

        RequestQueue requestQueue = Volley.newRequestQueue(User_Info.this);
        requestQueue.add(request);

    }

    public void getTemplates() {

        ringProgressDialog = ProgressDialog.show(User_Info.this, "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.GETOPERATORS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();
                        JSONArray jsonArray = null;
                        try {
                            String resp = response.trim();

                            op_list = new ArrayList<>();
                            opId_list = new ArrayList<>();

                            if (resp.equals("false")) {
                                op_list.add("No Records Founds");
                                opId_list.add("0");

                            } else {


                                jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                                    op_list.add(jsonObject.getString("opr_name"));
                                    opId_list.add(jsonObject.getString("id"));

                                }

                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                    (User_Info.this, R.layout.spinner_item, op_list);

                            dataAdapter.setDropDownViewResource
                                    (android.R.layout.simple_spinner_dropdown_item);

                            opperator.setAdapter(dataAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    Toast.makeText(User_Info.this, "No connection Error", Toast.LENGTH_SHORT).show();

                } else if (error instanceof TimeoutError) {

                    Toast.makeText(User_Info.this, " connection Time out Error", Toast.LENGTH_SHORT).show();

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("id", client_id);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(User_Info.this);
        requestQueue.add(request);

    }

    public void getvehicles() {

        ringProgressDialog = ProgressDialog.show(User_Info.this, "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.GETBUSES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();
                        getTerminals();
                        JSONArray jsonArray = null;

                        String resp = response.trim();

                        buss_list = new ArrayList<>();
                        bussId_list = new ArrayList<>();
                        pincodelist = new ArrayList<>();
                        busRegistrationList = new ArrayList<>();


                        if (resp.equals("false")) {
                            buss_list.add("No Records Founds");
                            bussId_list.add("0");
                            pincodelist.add("0");

                        } else {
                            try {

                                jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                                    buss_list.add(jsonObject.getString("driver_name"));
                                    bussId_list.add(jsonObject.getString("driver_id"));
                                    pincodelist.add(jsonObject.getString("driver_pin_code"));
                                    busRegistrationList.add(jsonObject.getString("Bus_Number"));

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                (User_Info.this, R.layout.spinner_item, buss_list);

                        dataAdapter.setDropDownViewResource
                                (android.R.layout.simple_spinner_dropdown_item);

                        terminal.setAdapter(dataAdapter);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    Toast.makeText(User_Info.this, "No connection Error", Toast.LENGTH_SHORT).show();

                } else if (error instanceof TimeoutError) {

                    Toast.makeText(User_Info.this, " connection Time out Error", Toast.LENGTH_SHORT).show();

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("Terminal_id", operator_id);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(User_Info.this);
        requestQueue.add(request);

    }

    public void getTerminals() {

        ringProgressDialog = ProgressDialog.show(User_Info.this, "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_TERMINALS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();
                        JSONArray jsonArray = null;

                        String resp = response.trim();
                        terminalList = new ArrayList<>();
                        terminalIdList = new ArrayList<>();
                        terminalStanList = new ArrayList<>();
                        managerCodeList = new ArrayList<>();
                        if (resp.equals("false")) {
                            terminalList.add("No Records Founds");
                            terminalIdList.add("0");
                            terminalStanList.add("0");
                            managerCodeList.add("0");

                        }else {
                            try {

                                jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                                   terminalList.add(jsonObject.getString("terminal_name"));
                                   terminalIdList.add(jsonObject.getString("terminal_id"));
                                   terminalStanList.add(jsonObject.getString("terminal_stan"));
                                   managerCodeList.add(jsonObject.getString("manager_password"));

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                (User_Info.this, R.layout.spinner_item, terminalList);

                        dataAdapter.setDropDownViewResource
                                (android.R.layout.simple_spinner_dropdown_item);

                        allTerminals.setAdapter(dataAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    Toast.makeText(User_Info.this, "No connection Error", Toast.LENGTH_SHORT).show();

                } else if (error instanceof TimeoutError) {

                    Toast.makeText(User_Info.this, " connection Time out Error", Toast.LENGTH_SHORT).show();

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
               params.put("Terminal_id", operator_id);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(User_Info.this);
        requestQueue.add(request);

    }

    private class CustomOnItemSelectedListener_stake implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, final int pos,
                                   long id) {

            if (stake_list.get(pos).equals("No Records Founds")) {
                client_id = "0";
            } else {
                client_id = stakeID_list.get(pos);

            }
            if (!stake_list.get(pos).equals("Select")) {
                getTemplates();
            } else
                Toast.makeText(User_Info.this, "Plz select Stake Holder", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }

    }

    private class CustomOnItemSelectedListener_operator implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, final int pos,
                                   long id) {

            if (op_list.get(pos).equals("No Records Founds")) {
                operator_id = "0";
            } else {
                operator_id = opId_list.get(pos);

            }
            if (!op_list.get(pos).equals("Select")) {
                getvehicles();
            } else
                Toast.makeText(User_Info.this, "Plz select client", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }

    }


}
