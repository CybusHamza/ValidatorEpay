package com.epay.validator.validator_epay.Activities;

import android.content.Intent;
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

    }
}
