package com.epay.validator.validator_epay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapter.CustomTransactionListAdapter;
import localDatabase.DBManager;
import pojo.TransactionData;

public class Transactions extends AppCompatActivity {
    Toolbar toolbar;
    private DBManager dbManager;
    String[] customer_id,trans_id,amountPaid;
    private ListView ListView;
    private List<TransactionData> TransactionList = new ArrayList<TransactionData>();
    CustomTransactionListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        ListView = (ListView) findViewById(R.id.history_list);
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
        for (int i=0; i < customer_id.length; i++) {

            TransactionData hd = new TransactionData();
            hd.setCustomer_id(customer_id[i]);
            hd.setTrans_id(trans_id[i]);
            hd.setFare_Price(amountPaid[i]);
            TransactionList.add(hd);
            // movieList.add(b);
        }
   
    // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

    adapter = new CustomTransactionListAdapter(Transactions.this,TransactionList);
                ListView.setAdapter(adapter);

    }
}
