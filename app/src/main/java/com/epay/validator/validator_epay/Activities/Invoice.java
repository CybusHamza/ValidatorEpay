package com.epay.validator.validator_epay.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.epay.validator.validator_epay.R;

/**
 * Created by Rizwan Butt on 26-May-17.
 */

public class Invoice extends AppCompatActivity {

    Toolbar toolbar;
    String customer_id,fare,fareType,routeId,transId,transStatusId,dat,from,to;
    TextView CustomerId,date,CustomerName,Contact,From,To,TransactionId;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice);
        toolbar = (Toolbar) findViewById(R.id.app_bar_history);
        toolbar.setTitle("Invoice");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TransactionId= (TextView) findViewById(R.id.t_trans_id);
        date= (TextView) findViewById(R.id.t_date);
        CustomerName= (TextView) findViewById(R.id.t_cust_name);
        Contact= (TextView) findViewById(R.id.t_contact);
        From= (TextView) findViewById(R.id.t_from);
        To= (TextView) findViewById(R.id.t_to);
        // CustomerId= (TextView) findViewById(R.id.transId);
        Intent i=getIntent();
        customer_id=i.getStringExtra("customer_id");
        fare=i.getStringExtra("fare");
        fareType=i.getStringExtra("fareType");
        routeId=i.getStringExtra("routeId");
        transId=i.getStringExtra("transId");
        transStatusId=i.getStringExtra("transStatusId");
        dat=i.getStringExtra("date");
        from=i.getStringExtra("from");
        to=i.getStringExtra("to");
        // CustomerId.setText(customer_id);
        TransactionId.setText(transId);
        date.setText(dat);
        From.setText(from);
        To.setText(to);

    }
}
