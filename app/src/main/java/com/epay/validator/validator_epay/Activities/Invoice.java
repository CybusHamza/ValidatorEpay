package com.epay.validator.validator_epay.Activities;

import android.app.AlertDialog;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.epay.validator.validator_epay.Qr_Genrator.Contents;
import com.epay.validator.validator_epay.Qr_Genrator.QRCodeEncoder;
import com.epay.validator.validator_epay.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import java.util.Arrays;

/**
 * Created by Rizwan Butt on 26-May-17.
 */

public class Invoice extends AppCompatActivity {

    Toolbar toolbar;
    String customer_id,fare,fareType,routeId,transId,transStatusId,dat,from,to,no_of_persons,name,number;
    TextView CustomerId,date,CustomerName,Contact,From,To,TransactionId,FareType,No_Of_Persons,FarePrice,Total,Net;

    ImageView QrCode;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice);

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3/4;
        toolbar = (Toolbar) findViewById(R.id.app_bar_history);
        toolbar.setTitle("Invoice");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        QrCode= (ImageView) findViewById(R.id.imageView);
        TransactionId= (TextView) findViewById(R.id.t_trans_id);
        date= (TextView) findViewById(R.id.t_date);
        CustomerName= (TextView) findViewById(R.id.t_cust_name);
        Contact= (TextView) findViewById(R.id.t_contact);
        From= (TextView) findViewById(R.id.t_from);
        To= (TextView) findViewById(R.id.t_to);
        FareType= (TextView) findViewById(R.id.fareType);
        No_Of_Persons= (TextView) findViewById(R.id.no_of_persons);
        FarePrice= (TextView) findViewById(R.id.farePrice);
        Total= (TextView) findViewById(R.id.total);
        Net= (TextView) findViewById(R.id.net);
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
        no_of_persons=i.getStringExtra("person_traveling");
        if (no_of_persons==null || no_of_persons.equals("") || no_of_persons==""){
            no_of_persons="1";
        }
        name=i.getStringExtra("name");
        number=i.getStringExtra("number");
        int total=Integer.valueOf(fare) * Integer.valueOf(no_of_persons);
        String Qrsting=customer_id+","+fare+","+fareType+","+routeId+","+transId+","+transStatusId+","+dat+","+from+","+to+","+no_of_persons+","+name+","+number;
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(Qrsting,
                null,
                Contents.Type.TEXT,
                BarcodeFormat.QR_CODE.toString(),
                smallerDimension);
        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            //ImageView myImage = (ImageView) findViewById(R.id.imageView1);
            QrCode.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
        // CustomerId.setText(customer_id);
        TransactionId.setText(transId);
        date.setText(dat);
        From.setText(from);
        To.setText(to);
        FareType.setText(fareType);
        FarePrice.setText(fare);
        No_Of_Persons.setText(no_of_persons);
        Total.setText("$"+String.valueOf(total));
        Net.setText("$"+String.valueOf(total));
        CustomerName.setText(name);
        Contact.setText(number);
        if (checkPrerequisites()) {
            org.altbeacon.beacon.Beacon beacon = new org.altbeacon.beacon.Beacon.Builder()
                    .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")
                    .setId2(customer_id)
                    .setId3("4")
                    .setManufacturer(0x0118) // Radius Networks.  Change this for other beacon layouts
                    .setTxPower(-59)
                    .setDataFields(Arrays.asList(new Long[]{0l})) // Remove this for beacon layouts without d: fields
                    .build();
            // Change the layout below for other beacon types
            BeaconParser beaconParser = new BeaconParser()
                    .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");
            BeaconTransmitter beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
            beaconTransmitter.startAdvertising(beacon);
        }
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
            builder.setMessage("Please enable Bluetooth and restart this app.");
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
