package com.example.deviceintegration.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.deviceintegration.MySQLiteDatabase.MySQLiteDB;
import com.example.deviceintegration.R;

public class MainActivity extends AppCompatActivity {

    Button buttonConfig;
    Button buttonElenaDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonConfig = findViewById(R.id.btnConfig);
        buttonElenaDevice = findViewById(R.id.btnDeviceElena);

        /*
        MySQLiteDB mySQLiteDB = new MySQLiteDB(getApplicationContext());
        try {
            mySQLiteDB.insertDevice("DEV_001", "150", "QA", "Disable");
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

        buttonConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getApplicationContext(), ConfigDevices.class);
                //startActivity(intent);

                Intent intent = new Intent(getApplicationContext(), ShowDevices.class);
                startActivity(intent);
            }
        });

        buttonElenaDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FunctionElenaDevice.class);
                startActivity(intent);
            }
        });

    }
}