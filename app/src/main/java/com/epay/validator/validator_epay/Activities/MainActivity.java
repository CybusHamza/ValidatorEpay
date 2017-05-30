package com.epay.validator.validator_epay.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.epay.validator.validator_epay.R;
import com.epay.validator.validator_epay.localDatabase.DBManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

//implementing onclicklistener
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String customer_id;
    String fare;
    String fareType;
    String routeId;
    String transId;
    String transStatusId;
    String operator_id;
    String terminal;

    //View Objects
    private Button buttonScan,buttonGetLiveData,buttonTransactions;
    private TextView textViewName, textViewAddress;

    //qr code scanner object
    private IntentIntegrator qrScan;
    private DBManager dbManager;
    android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         toolbar = (Toolbar) findViewById(R.id.app_bar_history);
        toolbar.setTitle("MainScreen");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        dbManager = new DBManager(MainActivity.this);
        dbManager.open();

        //View objects
        buttonScan = (Button) findViewById(R.id.buttonScan);
        buttonGetLiveData= (Button) findViewById(R.id.get_live_data_btn);
        buttonTransactions= (Button) findViewById(R.id.transactions);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);

        //intializing scan object
        qrScan = new IntentIntegrator(this);

        //attaching onclick listener
        buttonScan.setOnClickListener(this);
        buttonGetLiveData.setOnClickListener(this);
        buttonTransactions.setOnClickListener(this);
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    //textViewAddress.setText(obj.getString("address"));
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                   // textViewName.setText(result.getContents());
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
                ////final qr string customer_id,fare,fareType,routeId,transId,transStatusId////////
                String[] qrData=result.getContents().split(",");
                customer_id=qrData[0];
                fare=qrData[1];
                fareType=qrData[2];
                routeId=qrData[3];
                transId=qrData[4];
                transStatusId=qrData[5];
               // textViewName.setText(customer_id+" "+fare+" "+fareType+" "+routeId+" "+transId+" "+transStatusId);
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
                String strDate = mdformat.format(cal.getTime());

                SharedPreferences  sharedPreferences=  getSharedPreferences("OperatorInfo",MODE_PRIVATE);
                operator_id = sharedPreferences.getString("operator","");
                terminal = sharedPreferences.getString("buss","");
                dbManager.insert_into_transactions(operator_id,terminal,customer_id,fareType,routeId,"pending",fare,"pending",transStatusId,transId,strDate,strDate,strDate);

                Intent intent=new Intent(this,Invoice.class);
                intent.putExtra("customer_id",customer_id);
                intent.putExtra("fareType",fareType);
                intent.putExtra("routeId",routeId);
                intent.putExtra("fare",fare);
                intent.putExtra("transStatusId",transStatusId);
                intent.putExtra("transId",transId);
                intent.putExtra("date",strDate);
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onClick(View view) {
        //initiating the qr code scan
        if(view==buttonScan)
        qrScan.initiateScan();
        if(view==buttonGetLiveData)
            getLiveData();
        if(view==buttonTransactions){
            Intent i=new Intent(this,Transactions.class);
            startActivity(i);
        }
    }

    private void getLiveData() {
        startService(new Intent(MainActivity.this, HelloService.class));

    }
}
