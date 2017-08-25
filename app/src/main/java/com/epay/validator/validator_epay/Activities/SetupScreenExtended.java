package com.epay.validator.validator_epay.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class SetupScreenExtended extends AppCompatActivity {

    private static final int MY_SOCKET_TIMEOUT_MS = 10000;

    AlertDialog myalertdialog;
    Spinner terminal;
    Spinner stakeholder,vehicleRegSpinner;

    ProgressDialog ringProgressDialog;

    private List<String> stake_list;
    private List<String> stakeID_list;
    private List<String> bank_id_List;
    String selectedstakeID_list;
    private List<String> op_list;
    private List<String> opId_list;
    String selectedop_list;
    String selectedopId;
    private List<String> buss_list;
    private List<String> bussId_list;
    private List<String> bussIds_original_list;
    private List<String> pincodelist;
    private List<String> busRegistrationList;
    private List<String> commissionList;
    private List<String> commissionIdList;
    private List<String> stakeholder_CommissionList;
    private List<String> manager_CommissionList;
    private List<String> operater_CommissionList;
    private List<String> commissionTypeList;
    private List<String> terminalList;
    private List<String> terminalIdList;
    private List<String> terminalStanList;
    private List<String> managerCodeList;
    private List<String> participantAcquirerComm;
    private List<String> participantPtspComm;
    private List<String> participantOprComm;
    private List<String> participantSwitchComm;
    private List<String> participantProcessorComm;
    private List<String> participantIssuerComm;
    private List<String> participantMngrComm;
    private List<String> routeCodeList;
    private List<String> routeIdList;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Button SaveSettings;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_screen_extended);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Setup Screen");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPreferences = getSharedPreferences("OperatorInfo", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        stakeholder= (Spinner) findViewById(R.id.stakeholder);
        //vehicleRegSpinner= (Spinner) findViewById(R.id.vehicleRegSpinner);
        SaveSettings= (Button) findViewById(R.id.saveSettings);
        SaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String stakeholderName= stakeholder.getSelectedItem().toString();
                int position=stakeholder.getSelectedItemPosition();
               // int position1=vehicleRegSpinner.getSelectedItemPosition();
                //String vehicleRegName= vehicleRegSpinner.getSelectedItem().toString();
                    if (position!=-1) {
                        int id = (int) stakeholder.getSelectedItemId();
                       // if (position1!=-1){
                            editor.putString("stakeholder", stakeID_list.get((int) stakeholder.getSelectedItemId()));
                            editor.putString("commission", commissionList.get((int) stakeholder.getSelectedItemId()));
                            editor.putString("commissionType", commissionTypeList.get((int) stakeholder.getSelectedItemId()));
                           /* editor.putString("stakeholder_Commission", stakeholder_CommissionList.get((int) stakeholder.getSelectedItemId()));
                            editor.putString("manager_Commission", manager_CommissionList.get((int) stakeholder.getSelectedItemId()));
                            editor.putString("operater_Commission", operater_CommissionList.get((int) stakeholder.getSelectedItemId()));*/
                            editor.putString("acquirer_com", participantAcquirerComm.get(0));
                            editor.putString("ptsp_com", participantPtspComm.get(0));
                            editor.putString("opr_comm", participantOprComm.get(0));
                            editor.putString("switch_com", participantSwitchComm.get(0));
                            editor.putString("processor_com", participantProcessorComm.get(0));
                            editor.putString("issuer_com", participantIssuerComm.get(0));
                            editor.putString("mngr_comm", participantMngrComm.get(0));
                            editor.putString("operator", sharedPreferences.getString("operatorId", ""));
                          //  editor.putString("routeId",routeIdList.get((int) vehicleRegSpinner.getSelectedItemId()));
                           /* editor.putString("driverName", buss_list.get((int) vehicleRegSpinner.getSelectedItemId()));
                            editor.putString("buss", bussId_list.get((int) vehicleRegSpinner.getSelectedItemId()));
                            editor.putString("Pincode", pincodelist.get((int) vehicleRegSpinner.getSelectedItemId()));
                            editor.putString("busNumber", busRegistrationList.get((int) vehicleRegSpinner.getSelectedItemId()));
                            editor.putString("busId", bussIds_original_list.get((int) vehicleRegSpinner.getSelectedItemId()));*/
                            editor.putString("is_first", "true");
//                            editor.putString("login","true");
                            editor.apply();

                        Intent intent = new Intent(SetupScreenExtended.this, LoginScreen.class);
                        finish();
                        startActivity(intent);
                   /* }else {
                            Toast.makeText(SetupScreenExtended.this, "No Route Found", Toast.LENGTH_SHORT).show();

                        }*/

                    } else {
                        Toast.makeText(SetupScreenExtended.this, "Please Select Stakeholder", Toast.LENGTH_SHORT).show();

                    }
            }
        });


        dialog_Setup();
    }
    public void dialog_Setup(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(SetupScreenExtended.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_terminal_manager_pin_code, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        //final EditText code = (EditText) dialogView.findViewById(R.id.code);
        terminal = (Spinner) dialogView.findViewById(R.id.terminalSpinner);
        final EditText mngrCode = (EditText) dialogView.findViewById(R.id.managercode);
        Button send = (Button) dialogView.findViewById(R.id.send);
        ImageView cancel = (ImageView) dialogView.findViewById(R.id.btn_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SetupScreenExtended.this,SetupScreen.class);
                finish();
                startActivity(intent);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=terminal.getSelectedItemPosition();
               // String terminalName= terminal.getSelectedItem().toString();
                if(position!=-1 && !terminalList.isEmpty()) {

                    int id = (int) terminal.getSelectedItemId();
                    if ((managerCodeList.get(id).equals(mngrCode.getText().toString()))) {
                       // myalertdialog.dismiss();
                        editor.putString("terminalName", terminal.getSelectedItem().toString());
                        editor.putString("terminalId", terminalIdList.get(id));
                        editor.putString("bankId", bank_id_List.get(id));
                        editor.putString("terminalStan", terminalStanList.get(id));
                        editor.putString("managerCode", managerCodeList.get(id));
                        editor.putString("operatorId", opId_list.get(id));
                        editor.commit();
                        getOperators(opId_list.get(id));
                    } else {
                        Toast.makeText(SetupScreenExtended.this, "Please Enter Valid Terminal Manager Pin Code", Toast.LENGTH_SHORT).show();

                    }
                }else {
                    Toast.makeText(SetupScreenExtended.this, "No Terminal found", Toast.LENGTH_SHORT).show();

                }

            }
        });

        myalertdialog = builder.create();
        myalertdialog.show();
       getTerminals();
    }
    public void Allstakeholder(final String operatorId) {

        ringProgressDialog = ProgressDialog.show(SetupScreenExtended.this, "", "Please wait ...", true);
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
                           // bank_id_List = new ArrayList<>();
                            commissionList = new ArrayList<>();
                            commissionIdList = new ArrayList<>();
                            /*stakeholder_CommissionList = new ArrayList<>();
                            manager_CommissionList = new ArrayList<>();
                            operater_CommissionList = new ArrayList<>();*/
                            commissionTypeList = new ArrayList<>();

                            jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                                stake_list.add(jsonObject.getString("first_name") );
                                stakeID_list.add(jsonObject.getString("stakeholder_id"));
                                commissionList.add(jsonObject.getString("total_Commission"));
                                commissionIdList.add(jsonObject.getString("Commission_ID"));
                               /* stakeholder_CommissionList.add(jsonObject.getString("stakeholder_Commission"));
                                manager_CommissionList.add(jsonObject.getString("manager_Commission"));
                                operater_CommissionList.add(jsonObject.getString("operater_Commission"));*/
                                commissionTypeList.add(jsonObject.getString("Commission_type"));

                            }

                           /* ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                    (SetupScreenExtended.this, R.layout.spinner_item, stake_list);

                            dataAdapter.setDropDownViewResource
                                    (android.R.layout.simple_spinner_dropdown_item);

                            stakeholder.setAdapter(dataAdapter);*/
                            //getvehicles();
                          //  getRoutes();
                            getParticipantCommission();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    Toast.makeText(SetupScreenExtended.this, "No connection Error", Toast.LENGTH_SHORT).show();

                } else if (error instanceof TimeoutError) {

                    Toast.makeText(SetupScreenExtended.this, " connection Time out Error", Toast.LENGTH_SHORT).show();

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("stake_holder_id", operatorId);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(SetupScreenExtended.this);
        requestQueue.add(request);

    }

    public void getParticipantCommission() {
        //int id = (int) stakeholder.getSelectedItemId();
        final String commissionId=commissionIdList.get(0);
        ringProgressDialog = ProgressDialog.show(SetupScreenExtended.this, "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.GETPARTICIPANTCOMMISSION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();
                        JSONArray jsonArray = null;
                        try {
                            participantAcquirerComm = new ArrayList<>();
                            participantIssuerComm = new ArrayList<>();
                            participantMngrComm = new ArrayList<>();
                            participantOprComm = new ArrayList<>();
                            participantProcessorComm = new ArrayList<>();
                            participantPtspComm = new ArrayList<>();
                            participantSwitchComm = new ArrayList<>();

                            jsonArray = new JSONArray(response);
                            if(jsonArray.length()<1){
                                participantAcquirerComm.add(0,"0");
                                participantPtspComm.add(0,"0");
                                participantOprComm.add(0,"0");
                                participantSwitchComm.add(0,"0");
                                participantProcessorComm.add(0,"0");
                                participantIssuerComm.add(0,"0");
                                participantMngrComm.add(0,"0");
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                                if(jsonObject.getString("part_type").equals("acquirer_com")){
                                    participantAcquirerComm.add(jsonObject.getString("part_comm"));
                                }else if(jsonObject.getString("part_type").equals("ptsp_com")){
                                    participantPtspComm.add(jsonObject.getString("part_comm"));
                                }else if(jsonObject.getString("part_type").equals("opr_comm")){
                                    participantOprComm.add(jsonObject.getString("part_comm"));
                                }else if(jsonObject.getString("part_type").equals("switch_com")){
                                    participantSwitchComm.add(jsonObject.getString("part_comm"));
                                }else if(jsonObject.getString("part_type").equals("processor_com")){
                                    participantProcessorComm.add(jsonObject.getString("part_comm"));
                                }else if(jsonObject.getString("part_type").equals("issuer_com")){
                                    participantIssuerComm.add(jsonObject.getString("part_comm"));
                                }else if(jsonObject.getString("part_type").equals("mngr_comm")){
                                    participantMngrComm.add(jsonObject.getString("part_comm"));
                                }
                            }
                            editor.putString("stakeholder", stakeID_list.get(0));
                            editor.putString("commission", commissionList.get(0));
                            editor.putString("commissionType", commissionTypeList.get(0));
                            editor.putString("acquirer_com", participantAcquirerComm.get(0));
                            editor.putString("ptsp_com", participantPtspComm.get(0));
                            editor.putString("opr_comm", participantOprComm.get(0));
                            editor.putString("switch_com", participantSwitchComm.get(0));
                            editor.putString("processor_com", participantProcessorComm.get(0));
                            editor.putString("issuer_com", participantIssuerComm.get(0));
                            editor.putString("mngr_comm", participantMngrComm.get(0));
                            editor.putString("operator", sharedPreferences.getString("operatorId", ""));

                            editor.putString("is_first", "true");
//                            editor.putString("login","true");
                            editor.apply();

                            Intent intent = new Intent(SetupScreenExtended.this, LoginScreen.class);
                            finish();
                            startActivity(intent);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    Toast.makeText(SetupScreenExtended.this, "No connection Error", Toast.LENGTH_SHORT).show();

                } else if (error instanceof TimeoutError) {

                    Toast.makeText(SetupScreenExtended.this, " connection Time out Error", Toast.LENGTH_SHORT).show();

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("com_id", commissionId);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(SetupScreenExtended.this);
        requestQueue.add(request);

    }
    public  void getRoutes(){
        int id = (int) stakeholder.getSelectedItemId();
        final String organizationId=stakeID_list.get(id);
        final SharedPreferences sharedPreferences = getSharedPreferences("OperatorInfo", MODE_PRIVATE);
        ringProgressDialog = ProgressDialog.show(SetupScreenExtended.this, "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_VEHICLE_ROUTES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ringProgressDialog.dismiss();
                        routeCodeList=new ArrayList<>();
                        routeIdList=new ArrayList<>();
                        if(response.equals("false")) {
                            routeCodeList.add(0, "");
                            routeIdList.add(0, "");
                        }
                        try {
                            JSONArray jsonArray=new JSONArray(response.trim());
                            for (int i=0;i<jsonArray.length();i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                routeCodeList.add(object.getString("route_code"));
                                routeIdList.add(object.getString("id"));
                            }
                            if(jsonArray.length()>0) {
                                getParticipantCommission();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                (SetupScreenExtended.this, R.layout.spinner_item, routeCodeList);

                        dataAdapter.setDropDownViewResource
                                (android.R.layout.simple_spinner_dropdown_item);

                        vehicleRegSpinner.setAdapter(dataAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    Toast.makeText(SetupScreenExtended.this, "No connection Error", Toast.LENGTH_SHORT).show();

                } else if (error instanceof TimeoutError) {

                    Toast.makeText(SetupScreenExtended.this, " connection Time out Error", Toast.LENGTH_SHORT).show();

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("vehicle_id",organizationId);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(SetupScreenExtended.this);
        requestQueue.add(request);
    }
   /* public void AllTerminals() {

        ringProgressDialog = ProgressDialog.show(SetupScreenExtended.this, "", "Please wait ...", true);
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
                            terminalIdList = new ArrayList<>();
                            //  stakeID_list = new ArrayList<>();
                            // commissionList = new ArrayList<>();
                            //  stakeholder_CommissionList = new ArrayList<>();
                              managerCodeList = new ArrayList<>();
                            //  operater_CommissionList = new ArrayList<>();
                            //  commissionTypeList = new ArrayList<>();

                            jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                                terminalList.add(jsonObject.getString("terminal_name"));
                                managerCodeList.add(jsonObject.getString("manager_password"));
                                terminalIdList.add(jsonObject.getString("terminal_id"));
                               *//* stake_list.add(jsonObject.getString("first_name") + jsonObject.getString("last_name"));
                                stakeID_list.add(jsonObject.getString("stakeholder_id"));
                                commissionList.add(jsonObject.getString("total_Commission"));
                                stakeholder_CommissionList.add(jsonObject.getString("stakeholder_Commission"));
                                manager_CommissionList.add(jsonObject.getString("manager_Commission"));
                                operater_CommissionList.add(jsonObject.getString("operater_Commission"));
                                commissionTypeList.add(jsonObject.getString("Commission_type"));*//*

                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                    (SetupScreenExtended.this, R.layout.spinner_item_dialog, terminalList);

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

                    Toast.makeText(SetupScreenExtended.this, "No connection Error", Toast.LENGTH_SHORT).show();

                } else if (error instanceof TimeoutError) {

                    Toast.makeText(SetupScreenExtended.this, " connection Time out Error", Toast.LENGTH_SHORT).show();

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

        RequestQueue requestQueue = Volley.newRequestQueue(SetupScreenExtended.this);
        requestQueue.add(request);

    }*/

    public void getOperators(final String operatorId) {

        ringProgressDialog = ProgressDialog.show(SetupScreenExtended.this, "", "Please wait ...", true);
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
                            //stakeID_list=new ArrayList<>();

                            if (resp.equals("false")) {
                                op_list.add("No Records Founds");
                                opId_list.add("0");

                            } else {
                                
                                String stakId = null;

                                jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                                    op_list.add(jsonObject.getString("opr_name"));
                                    selectedop_list=jsonObject.getString("opr_name");
                                    opId_list.add(jsonObject.getString("id"));
                                   // stakeID_list.add(jsonObject.getString("stake_holder_id"));
                                    stakId=jsonObject.getString("stake_holder_id");
                                   
                                }
                                Allstakeholder(stakId);
                            }


                           /* ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                    (SetupScreenExtended.this, R.layout.spinner_item, op_list);

                            dataAdapter.setDropDownViewResource
                                    (android.R.layout.simple_spinner_dropdown_item);*/

                           // opperator.setAdapter(dataAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    Toast.makeText(SetupScreenExtended.this, "No connection Error", Toast.LENGTH_SHORT).show();

                } else if (error instanceof TimeoutError) {

                    Toast.makeText(SetupScreenExtended.this, " connection Time out Error", Toast.LENGTH_SHORT).show();

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("id", operatorId);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(SetupScreenExtended.this);
        requestQueue.add(request);

    }
    public void getTerminals() {

        ringProgressDialog = ProgressDialog.show(SetupScreenExtended.this, "", "Please wait ...", true);
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
                        bank_id_List = new ArrayList<>();
                        opId_list = new ArrayList<>();
                        if (resp.equals("false")) {
                            terminalList.add("No Records Founds");
                            terminalIdList.add("0");
                            terminalStanList.add("0");
                            managerCodeList.add("0");
                            opId_list.add("0");

                        }else {
                            try {

                                jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                                    terminalList.add(jsonObject.getString("terminal_name"));
                                    terminalIdList.add(jsonObject.getString("terminal_id"));
                                    terminalStanList.add(jsonObject.getString("terminal_stan"));
                                    managerCodeList.add(jsonObject.getString("manager_password"));
                                    opId_list.add(jsonObject.getString("operator_id"));
                                    bank_id_List.add(jsonObject.getString("bank_id"));

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                (SetupScreenExtended.this, R.layout.spinner_item_dialog, terminalList);

                        dataAdapter.setDropDownViewResource
                                (android.R.layout.simple_spinner_dropdown_item);

                        terminal.setAdapter(dataAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();

                if (error instanceof NoConnectionError) {

                    Toast.makeText(SetupScreenExtended.this, "No connection Error", Toast.LENGTH_SHORT).show();

                } else if (error instanceof TimeoutError) {


                    Toast.makeText(SetupScreenExtended.this, " connection Time out Error", Toast.LENGTH_SHORT).show();

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(SetupScreenExtended.this);
        requestQueue.add(request);

    }

    public void getvehicles() {

        ringProgressDialog = ProgressDialog.show(SetupScreenExtended.this, "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.GETBUSES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();
                        //getTerminals();
                        JSONArray jsonArray = null;

                        String resp = response.trim();

                        buss_list = new ArrayList<>();
                        bussId_list = new ArrayList<>();
                        bussIds_original_list = new ArrayList<>();
                        pincodelist = new ArrayList<>();
                        busRegistrationList = new ArrayList<>();


                        if (resp.equals("false")) {
                            buss_list.add("No Records Founds");
                            bussId_list.add("0");
                            bussIds_original_list.add("0");
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
                                    bussIds_original_list.add(jsonObject.getString("Bus_ID"));

                                }
                                if(jsonArray.length()>0) {
                                    getParticipantCommission();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                (SetupScreenExtended.this, R.layout.spinner_item, busRegistrationList);

                        dataAdapter.setDropDownViewResource
                                (android.R.layout.simple_spinner_dropdown_item);

                        vehicleRegSpinner.setAdapter(dataAdapter);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    Toast.makeText(SetupScreenExtended.this, "No connection Error", Toast.LENGTH_SHORT).show();

                } else if (error instanceof TimeoutError) {

                    Toast.makeText(SetupScreenExtended.this, " connection Time out Error", Toast.LENGTH_SHORT).show();

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("Terminal_id", sharedPreferences.getString("operatorId",""));
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(SetupScreenExtended.this);
        requestQueue.add(request);

    }
}
