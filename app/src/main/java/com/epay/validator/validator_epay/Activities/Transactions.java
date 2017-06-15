package com.epay.validator.validator_epay.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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
    private List<TransactionData> TransactionList = new ArrayList<TransactionData>();
    CustomTransactionListAdapter adapter;
    Button sync;
    ProgressDialog ringProgressDialog;
    String route_id,from,to,no_of_persons;
    static  int count =0;
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

            TransactionData hd = new TransactionData();
            hd.setCustomer_id(customer_id[0]);
            hd.setTrans_id(trans_id.get(i));
            hd.setFare_Price(amountPaid.get(i));
            hd.setRouteStart(from);
            hd.setRoute_destinition(to);
            hd.setPersonTravelling(no_of_persons);
            TransactionList.add(hd);


            // movieList.add(b);
        }

        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isNetworkAvailable())
                {

                    data=  dbManager.fetch_trans();
                    if(data.size() >0)
                    {

                            syncData(data.get(count));

                    }
                    else {
                        Toast.makeText(Transactions.this, "No Data To Sycn", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
        adapter = new CustomTransactionListAdapter(Transactions.this,TransactionList);
        ListView.setAdapter(adapter);

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
                map.put("customer_id",transId.get(1));
                map.put("fare_type_id",transId.get(2));
                map.put("route_id",transId.get(3));
                map.put("bus_type_id",transId.get(4));
                map.put("terminal_id",transId.get(5));
                map.put("operator_id",transId.get(6));
                map.put("amount_paid",transId.get(7));
                map.put("fee_paid",transId.get(8));
                map.put("currency",transId.get(9));
                map.put("trans_status_id",transId.get(10));
                map.put("paid_date",transId.get(11));
                map.put("trans_date",transId.get(12));
                map.put("cancel_date",transId.get(13));
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Transactions.this);
        requestQueue.add(request);

    }


}
