package com.epay.validator.validator_epay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

/**
 * Created by Rizwan Butt on 26-May-17.
 */

public class Invoice extends AppCompatActivity {

    Toolbar toolbar;
    String customer_id,fare,fareType,routeId,transId,transStatusId,dat;
    TextView CustomerId,date,CustomerName,Contact,From,To;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice);
        toolbar = (Toolbar) findViewById(R.id.app_bar_history);
        toolbar.setTitle("Invoice");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CustomerId= (TextView) findViewById(R.id.t_trans_id);
        date= (TextView) findViewById(R.id.t_date);
        CustomerName= (TextView) findViewById(R.id.t_cust_name);
        Contact= (TextView) findViewById(R.id.t_contact);
       // CustomerId= (TextView) findViewById(R.id.transId);
        Intent i=getIntent();
        customer_id=i.getStringExtra("customer_id");
        fare=i.getStringExtra("fare");
        fareType=i.getStringExtra("fareType");
        routeId=i.getStringExtra("routeId");
        transId=i.getStringExtra("transId");
        transStatusId=i.getStringExtra("transStatusId");
        dat=i.getStringExtra("date");
        CustomerId.setText(customer_id);
        date.setText(dat);

    }
}
