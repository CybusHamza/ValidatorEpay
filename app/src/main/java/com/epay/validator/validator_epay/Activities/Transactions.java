package com.epay.validator.validator_epay.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.epay.validator.validator_epay.Adapter.CustomTransactionListAdapter;
import com.epay.validator.validator_epay.R;
import com.epay.validator.validator_epay.localDatabase.DBManager;
import com.epay.validator.validator_epay.pojo.TransactionData;

import java.util.ArrayList;
import java.util.List;

public class Transactions extends AppCompatActivity {
    Toolbar toolbar;
    private DBManager dbManager;
    String[] customer_id,trans_id,amountPaid;
    private ListView ListView;
    private List<TransactionData> TransactionList = new ArrayList<TransactionData>();
    CustomTransactionListAdapter adapter;
    Button sync;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        ListView = (ListView) findViewById(R.id.history_list);
        toolbar = (Toolbar) findViewById(R.id.app_bar_history);
        sync= (Button) findViewById(R.id.sync);
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


        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
   
    // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

    adapter = new CustomTransactionListAdapter(Transactions.this,TransactionList);
                ListView.setAdapter(adapter);

    }
}
