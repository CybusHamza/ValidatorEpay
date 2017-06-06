package com.epay.validator.validator_epay.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
    String[] customer_id;
    ArrayList<String> trans_id,amountPaid;
    private ListView ListView;
    private List<TransactionData> TransactionList = new ArrayList<TransactionData>();
    CustomTransactionListAdapter adapter;
    String route_id,from,to,no_of_persons,date;

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
        for (int i=0; i < trans_id.size(); i++) {
            route_id=dbManager.fetch_route_id(trans_id.get(i));
            to=dbManager.h_fetch_route_table_dest(route_id);
            from=dbManager.h_fetch_route_table_start(route_id);
            no_of_persons=dbManager.fetch_no_of_persons(trans_id.get(i));
            date=dbManager.fetch_date(trans_id.get(i));

            TransactionData hd = new TransactionData();
            hd.setCustomer_id(customer_id[0]);
            hd.setTrans_id(trans_id.get(i));
            hd.setFare_Price(amountPaid.get(i));
            hd.setRouteStart(from);
            hd.setRoute_destinition(to);
            hd.setPersonTravelling(no_of_persons);
            hd.setDate(date);
            TransactionList.add(hd);
            // movieList.add(b);
        }
        // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
        adapter = new CustomTransactionListAdapter(Transactions.this,TransactionList);
        ListView.setAdapter(adapter);

    }
}
