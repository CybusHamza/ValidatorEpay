package com.epay.validator.validator_epay.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.epay.validator.validator_epay.Adapter.CustomTransactionListAdapter;
import com.epay.validator.validator_epay.Network.End_Points;
import com.epay.validator.validator_epay.R;
import com.epay.validator.validator_epay.localDatabase.DBManager;
import com.epay.validator.validator_epay.pojo.TransactionData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Transactions extends AppCompatActivity  {
    Toolbar toolbar;
    private DBManager dbManager;
    String[] customer_id;
    ArrayList<String> trans_id,amountPaid;
    private ListView ListView;
    AlertDialog myalertdialog;
    private List<TransactionData> TransactionList = new ArrayList<TransactionData>();
    CustomTransactionListAdapter adapter;
    Button sync;
    ProgressDialog ringProgressDialog;
    String route_id,from,to,no_of_persons,date;
    static  int count =0;
    String stanNumber,managerCode;
    HashMap<Integer,ArrayList<String>> data = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        ListView = (ListView) findViewById(R.id.history_list);
        sync = (Button) findViewById(R.id.sync);
        toolbar = (Toolbar) findViewById(R.id.app_bar_history);
        toolbar.setTitle("Transactions");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences("OperatorInfo", MODE_PRIVATE);
        stanNumber=sharedPreferences.getString("terminalStan","");
        managerCode=sharedPreferences.getString("managerCode","");
        dbManager = new DBManager(Transactions.this);
        dbManager.open();
        customer_id=dbManager.fetch();
        trans_id=dbManager.fetch_trans_id();
        amountPaid=dbManager.fetch_fare();
        for (int i=0; i < trans_id.size(); i++) {
            route_id=dbManager.fetch_route_id(trans_id.get(i));
            to=dbManager.h_fetch_route_table_dest(route_id);
            from=dbManager.h_fetch_route_table_start(route_id);
            no_of_persons=dbManager.fetch_no_of_persons(trans_id.get(i));
            date=dbManager.fetch_date(trans_id.get(i));

            count = 0;
            TransactionData hd = new TransactionData();
            hd.setCustomer_id(customer_id[0]);
            hd.setTrans_id(trans_id.get(i));
            hd.setFare_Price(amountPaid.get(i));
            hd.setRouteStart(from);
            hd.setDate(date);
            hd.setRoute_destinition(to);
            hd.setPersonTravelling(no_of_persons);
            TransactionList.add(hd);


            // movieList.add(b);
        }

        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getSharedPreferences("OperatorInfo",MODE_PRIVATE);
                final String  pin = sharedPreferences.getString("Pincode","");

                AlertDialog.Builder builder = new AlertDialog.Builder(Transactions.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dailog_pincode, null);
                builder.setView(dialogView);

                final EditText code = (EditText) dialogView.findViewById(R.id.code);
                final EditText mngrCode = (EditText) dialogView.findViewById(R.id.managercode);
                Button send = (Button) dialogView.findViewById(R.id.send);

                send.setOnClickListener(new View.OnClickListener() {
                    @Override

                    public void onClick(View view) {

                        if (code.getText().toString().equals(pin)) {
                            if (mngrCode.getText().toString().equals(managerCode)) {
                            if (isNetworkAvailable()) {

                                count = 0;
                                myalertdialog.dismiss();
                                data = dbManager.fetch_trans();
                                if (data.size() > 0) {

                                    syncData(data.get(0));

                                } else {
                                    Toast.makeText(Transactions.this, "No Data To Sycn", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(Transactions.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                                Toast.makeText(Transactions.this, "Wrong Manger Pin Entered", Toast.LENGTH_SHORT).show();
                            }
                        }
                       else
                        {
                            Toast.makeText(Transactions.this, "Wrong Pin Enter", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                myalertdialog = builder.create();
                myalertdialog.show();


            }
        });

        // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
        adapter = new CustomTransactionListAdapter(Transactions.this,TransactionList);
        ListView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void syncData(final ArrayList<String> transId) {

        ringProgressDialog = ProgressDialog.show(this, "", "please wait", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.SYNCDATA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                ringProgressDialog.dismiss();
                Toast.makeText(Transactions.this,"Sync Successful", Toast.LENGTH_SHORT).show();
                int size = data.size()-1;
                if(size >= count)
                {   ++count;
                    syncData(data.get(count));
                    dbManager.deletetrans(Long.parseLong(transId.get(0)));

                    //if()
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                ringProgressDialog.dismiss();
                finish();
                if (error instanceof NetworkError) {
                    Toast.makeText(Transactions.this, "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_LONG).show();
                }
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                SharedPreferences sharedPreferences = getSharedPreferences("OperatorInfo",MODE_PRIVATE);
                float commission = 0;
                float acquirer_com=0;
                float ptsp_com=0;
                float opr_comm=0;
                float switch_com=0;
                float processor_com=0;
                float issuer_com=0;
                float mngr_comm=0;

                map.put("customer_id",transId.get(1));
                map.put("fare_type_id",transId.get(2));
                map.put("route_id",transId.get(3));
                map.put("bus_type_id",sharedPreferences.getString("abc","1"));
                map.put("terminal_id",sharedPreferences.getString("terminalId",""));
                map.put("operator_id",sharedPreferences.getString("operator",""));
                map.put("bus_id",sharedPreferences.getString("busId",""));
                map.put("amount_paid",transId.get(7));
                map.put("fee_paid",transId.get(8));
                map.put("currency","171");
                map.put("trans_status_id",transId.get(10));
                map.put("paid_date",transId.get(11));
                map.put("trans_date",transId.get(12));
                map.put("cancel_date",transId.get(13));
                String personTraveling=dbManager.fetch_no_of_persons(transId.get(10));
                if(sharedPreferences.getString("commissionType","").equals("1")){


                    commission=Integer.parseInt(sharedPreferences.getString("commission",""));//*Integer.parseInt(personTraveling);
                    acquirer_com=Integer.parseInt(sharedPreferences.getString("acquirer_com",""))*Integer.parseInt(personTraveling);
                    ptsp_com=Integer.parseInt(sharedPreferences.getString("ptsp_com",""))*Integer.parseInt(personTraveling);
                    opr_comm=Integer.parseInt(sharedPreferences.getString("opr_comm",""))*Integer.parseInt(personTraveling);
                    switch_com=Integer.parseInt(sharedPreferences.getString("switch_com",""))*Integer.parseInt(personTraveling);
                    processor_com=Integer.parseInt(sharedPreferences.getString("processor_com",""))*Integer.parseInt(personTraveling);
                    issuer_com=Integer.parseInt(sharedPreferences.getString("issuer_com",""))*Integer.parseInt(personTraveling);
                    mngr_comm=Integer.parseInt(sharedPreferences.getString("mngr_comm",""))*Integer.parseInt(personTraveling);
                }else if(sharedPreferences.getString("commissionType","").equals("2")){
                    commission=Integer.parseInt(sharedPreferences.getString("commission",""));//Integer.parseInt(transId.get(7))*(Integer.parseInt(sharedPreferences.getString("commission",""))/100);
                    float percacquirerCommission=(Integer.parseInt(sharedPreferences.getString("acquirer_com",""))*commission)/100;
                    float percptspCommission=(Integer.parseInt(sharedPreferences.getString("ptsp_com",""))*commission)/100;
                    float percoprCommission=(Integer.parseInt(sharedPreferences.getString("opr_comm",""))*commission)/100;
                    float percswitchCommission=(Integer.parseInt(sharedPreferences.getString("switch_com",""))*commission)/100;
                    float percprocessorCommission=(Integer.parseInt(sharedPreferences.getString("processor_com",""))*commission)/100;
                    float percissuerCommission=(Integer.parseInt(sharedPreferences.getString("issuer_com",""))*commission)/100;
                    float permngrCommission=(Integer.parseInt(sharedPreferences.getString("mngr_comm",""))*commission)/100;
                    acquirer_com=Integer.parseInt(personTraveling)*percacquirerCommission;
                    ptsp_com=Integer.parseInt(personTraveling)*percptspCommission;
                    opr_comm=Integer.parseInt(personTraveling)*percoprCommission;
                    switch_com=Integer.parseInt(personTraveling)*percswitchCommission;
                    processor_com=Integer.parseInt(personTraveling)*percprocessorCommission;
                    issuer_com=Integer.parseInt(personTraveling)*percissuerCommission;
                    mngr_comm=Integer.parseInt(personTraveling)*permngrCommission;
                }
                map.put("commission_type",sharedPreferences.getString("commissionType",""));
                map.put("commission", String.valueOf(commission));
                map.put("acquirer_com", String.valueOf(acquirer_com));
                map.put("ptsp_com", String.valueOf(ptsp_com));
                map.put("opr_comm", String.valueOf(opr_comm));
                map.put("switch_com", String.valueOf(switch_com));
                map.put("processor_com", String.valueOf(processor_com));
                map.put("issuer_com", String.valueOf(issuer_com));
                map.put("mngr_comm", String.valueOf(mngr_comm));
               /* map.put("mngr_comm", String.valueOf(managerCommission));
                map.put("opr_comm", String.valueOf(operatorCommission));*/
                map.put("terminal_stan", transId.get(14));
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Transactions.this);
        requestQueue.add(request);

    }


}
