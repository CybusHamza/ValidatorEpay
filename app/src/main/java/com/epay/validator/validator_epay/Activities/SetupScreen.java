package com.epay.validator.validator_epay.Activities;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.epay.validator.validator_epay.R;

public class SetupScreen extends AppCompatActivity {

    Button btnSetup,btnLogon;
    AlertDialog myalertdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_screen);
        btnSetup= (Button) findViewById(R.id.btn_setup);
        btnLogon= (Button) findViewById(R.id.btn_logon);
        btnSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_Setup();
            }
        });
        btnLogon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void dialog_Setup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SetupScreen.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_terminal_manager_pin_code, null);
        builder.setView(dialogView);

        //final EditText code = (EditText) dialogView.findViewById(R.id.code);
        final EditText mngrCode = (EditText) dialogView.findViewById(R.id.managercode);
        Button send = (Button) dialogView.findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

            }
        });

        myalertdialog = builder.create();
        myalertdialog.show();
    }
}
