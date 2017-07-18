package com.epay.validator.validator_epay.Activities;

import android.app.AlertDialog;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.pt.printer.Printer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epay.validator.validator_epay.Qr_Genrator.Contents;
import com.epay.validator.validator_epay.Qr_Genrator.QRCodeEncoder;
import com.epay.validator.validator_epay.R;
import com.epay.validator.validator_epay.localDatabase.DBManager;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

//implementing onclicklistener
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String customer_id;
    String fare;
    String fareType;
    String routeId;
    String transId;
    String transStatusId;
    String from;
    String to;
    String no_of_persons;
    String name;
    String number;

    Printer printer = null;
    String line = "==============================";
    boolean open_flg = false;

    //View Objects
    private LinearLayout buttonScan,buttonTransactions;
    private TextView textViewName, textViewAddress;

    //qr code scanner object
    private IntentIntegrator qrScan;
    private DBManager dbManager;
    android.support.v7.widget.Toolbar toolbar;
     String Qrsting;
    String busNumber,stanNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences("OperatorInfo", MODE_PRIVATE);
        busNumber=sharedPreferences.getString("busNumber","");

        getLiveData();

        checkPrerequisites();
        dbManager = new DBManager(MainActivity.this);
        dbManager.open();

        //View objects
        buttonScan = (LinearLayout) findViewById(R.id.buttonScan);
       // buttonGetLiveData= (Button) findViewById(R.id.get_live_data_btn);
        buttonTransactions= (LinearLayout) findViewById(R.id.transactions);

        //intializing scan object

        qrScan = new IntentIntegrator(this);


        //attaching onclick listener
        buttonScan.setOnClickListener(this);
       // buttonGetLiveData.setOnClickListener(this);
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
                SharedPreferences sharedPreferences = getSharedPreferences("OperatorInfo", MODE_PRIVATE);
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                stanNumber=sharedPreferences.getString("terminalStan","");

                String substrstan1=stanNumber.substring(0,1);
                int substrstan2=Integer.parseInt(stanNumber.substring(1,6));
                substrstan2++;
                String finalstanNumber=substrstan1+String.valueOf(substrstan2);
                editor.putString("terminalStan",finalstanNumber);
                editor.commit();
                ////final qr string customer_id,fare,fareType,routeId,transId,transStatusId////////
                String[] qrData=result.getContents().split(",");
                customer_id=qrData[0];
                fare=qrData[1];
                fareType=qrData[2];
                routeId=qrData[3];
                transId=qrData[4];
                transStatusId=qrData[5];
                from=qrData[6];
                to=qrData[7];
                no_of_persons=qrData[8];
                name=qrData[9];
                number=qrData[10];
                int total = Integer.valueOf(fare) /Integer.valueOf(no_of_persons);

                // textViewName.setText(customer_id+" "+fare+" "+fareType+" "+routeId+" "+transId+" "+transStatusId);
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
                String strDate = mdformat.format(cal.getTime());

                dbManager.insert_into_transactions(customer_id,fareType,routeId,"pending",fare,"pending",transStatusId,transId,strDate,strDate,strDate);
                dbManager.insert_into_history_travel(routeId,customer_id,transId,no_of_persons,strDate,"0000-00-00");

                Qrsting = customer_id + "," + fare + "," + fareType + "," + routeId + "," + transId + "," + transStatusId + "," + from + "," + to + "," + no_of_persons + "," + name + "," + number;
                final Beacon beacon = new Beacon.Builder()
                        .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")
                        .setId2(customer_id)
                        .setId3("5")
                        .setManufacturer(0x0118) //for altBeacon
                        .setTxPower(-59)
                        .setDataFields(Arrays.asList(0l))
                        .build();
                // Change the layout below for other beacon types
                final BeaconParser beaconParser = new BeaconParser()
                        .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");
                final BeaconTransmitter beaconTransmitter = new BeaconTransmitter(MainActivity.this, beaconParser);
                beaconTransmitter.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);
                beaconTransmitter.setAdvertiseTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
                beaconTransmitter.startAdvertising(beacon, new AdvertiseCallback() {

                    @Override
                    public void onStartFailure(int errorCode) {
                        Log.e("Beacon", "Advertisement start failed with code: " + errorCode);
                    }

                    @Override
                    public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                        Log.i("Beacon", "Advertisement start succeeded. " + settingsInEffect);
                    }
                });

                new CountDownTimer(20000, 1000) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        beaconTransmitter.stopAdvertising();
                    }
                }.start();

                printer = new Printer();


                int ret = printer.open();


                if (ret == 0) {

                    Toast.makeText(MainActivity.this, "open success!!", Toast.LENGTH_SHORT).show();

                    open_flg = true;

                } else {


                    Toast.makeText(MainActivity.this, "open fail !!", Toast.LENGTH_SHORT).show();
                    printer.setBold(true);
                    open_flg = false;

                }


                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                printer.printString(line);
                printer.setBold(true);
                printer.setAlignment(1);
                printer.setFontSize(2);
                printer.printString("Epay Receipt");
                printer.setFontSize(0);
                printer.setBold(false);
                printer.printPictureByRelativePath("/res/drawable/trans.png", 70, 70);
                printer.printString(line);
                printer.setAlignment(1);
                printer.printQR(Qrsting, 4);
                printer.printString(" ");
                printer.setAlignment(0);
                printer.printString("TRXN Date : " + strDate);
                printer.setLeftMargin(2);
                printer.printString("Customer : " + name);
                printer.printString("TRXN Id : " +transId);
                printer.printString("Tariff :  NGN " + total);
                printer.printString("No. of Tickets : " + no_of_persons);
                printer.printString("Total : NGN " + fare);
                printer.printString("From : " + from);
                printer.printString("To   : " + to);
                printer.printString("Bus No. : " + busNumber);
                printer.printString("STAN : " + finalstanNumber);
                printer.setLeftMargin(0);
                printer.printString(line);
                printer.setAlignment(0);
                printer.printString(" ");
                printer.setAlignment(1);
                printer.setBold(true);
                printer.printString("powered by Epayplus");
                printer.printString(" ");
                printer.printString(" ");


                printer.close();
                /*Intent intent=new Intent(this,Invoice.class);
                intent.putExtra("customer_id",customer_id);
                intent.putExtra("fareType",fareType);
                intent.putExtra("routeId",routeId);
                intent.putExtra("fare",fare);
                intent.putExtra("transStatusId",transStatusId);
                intent.putExtra("transId",transId);
                intent.putExtra("date",strDate);
                intent.putExtra("from",from);
                intent.putExtra("to",to);
                intent.putExtra("person_traveling",no_of_persons);
                intent.putExtra("name",name);
                intent.putExtra("number",number);
                startActivity(intent);*/
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);

        }
    }
    @Override
    public void onClick(View view) {
        //initiating the qr code scan
        if(view==buttonScan) {
            //qrScan.setCameraId(1);
            if (checkPrerequisites()) {
           qrScan.setOrientationLocked(true);
            qrScan.initiateScan();
            }
        }
        if(view==buttonTransactions){
            Intent i=new Intent(this,Transactions.class);
            startActivity(i);
        }
    }

    private void getLiveData() {
        startService(new Intent(MainActivity.this, HelloService.class));

    }

    private boolean checkPrerequisites() {

        if (android.os.Build.VERSION.SDK_INT < 18) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth LE not supported by this device's operating system");
            builder.setMessage("You will not be able to transmit as a Beacon");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }

            });
            builder.show();
            return false;
        }
        if (!getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth LE not supported by this device");
            builder.setMessage("You will not be able to transmit as a Beacon");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }

            });
            builder.show();
            return false;
        }
        if (!((BluetoothManager) getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter().isEnabled()){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth not enabled");
            builder.setMessage("Please enable Bluetooth and then Scan.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }

            });
            builder.show();
            return false;

        }

        try {
            // Check to see if the getBluetoothLeAdvertiser is available.  If not, this will throw an exception indicating we are not running Android L
            ((BluetoothManager) this.getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter().getBluetoothLeAdvertiser();
        }
        catch (Exception e) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth LE advertising unavailable");
            builder.setMessage("Sorry, the operating system on this device does not support Bluetooth LE advertising.  As of July 2014, only the Android L preview OS supports this feature in user-installed apps.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }

            });
            builder.show();
            return false;

        }

        return true;
    }
}
