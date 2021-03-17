package com.example.deviceintegration.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.deviceintegration.Devices.DeviceElena;
import com.example.deviceintegration.Models.ElenaModel;
import com.example.deviceintegration.MySQLiteDatabase.MySQLiteDB;
import com.example.deviceintegration.R;

public class ConfigDevices extends AppCompatActivity {

    EditText editTextName;
    EditText editTextIpAddress;
    EditText editTextReference;

    SwitchCompat switchCompatEnable;

    Button buttonSearch;
    Button buttonInsert;
    Button buttonUpdate;
    Button buttonDelete;
    Button buttonTest;
    Button buttonShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_devices);

        buttonSearch = findViewById(R.id.search_device);
        buttonInsert = findViewById(R.id.insert_device);
        buttonUpdate = findViewById(R.id.edit_device);
        buttonDelete = findViewById(R.id.delete_device);
        buttonTest = findViewById(R.id.test_device);
        buttonShow = findViewById(R.id.show_device);

        editTextName = findViewById(R.id.device_name);
        editTextIpAddress = findViewById(R.id.ip_device);
        editTextReference = findViewById(R.id.reference_device);

        switchCompatEnable = findViewById(R.id.enable_device);

        final MySQLiteDB mySQLiteDB = new MySQLiteDB(getApplicationContext());

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextName.getText().length()!=0) {
                    ElenaModel elenaModel = new ElenaModel();
                    mySQLiteDB.selectDevice(elenaModel, editTextName.getText().toString());

                    editTextIpAddress.setText(elenaModel.getIpHost());
                    editTextReference.setText(elenaModel.getReference());

                    String state = elenaModel.getState();
                    if (state.equals("Enable")) {
                        switchCompatEnable.setChecked(true);
                    } else {
                        switchCompatEnable.setChecked(false);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please enter device name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextName.getText().length()!=0 && editTextIpAddress.getText().length()!=0 && editTextReference.getText().length()!=0) {
                    String state = "Disable";
                    if (switchCompatEnable.isChecked()) {
                        state = "Enable";
                    }
                    try {
                        mySQLiteDB.insertDevice(editTextName.getText().toString(), editTextIpAddress.getText().toString(), editTextReference.getText().toString(), state);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "Insert Ok", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Incomplete data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextName.getText().length()!=0) {
                    String state = "Disable";
                    if (switchCompatEnable.isChecked()) {
                        state = "Enable";
                    }
                    mySQLiteDB.updateDevice(editTextName.getText().toString(), editTextIpAddress.getText().toString(), editTextReference.getText().toString(), state);
                    Toast.makeText(getApplicationContext(), "Update Ok", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please enter device name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextName.getText().length()!=0) {
                    mySQLiteDB.deleteDevice(editTextName.getText().toString());
                    Toast.makeText(getApplicationContext(), "Delete Ok", Toast.LENGTH_SHORT).show();
                    editTextName.setText("");
                    editTextIpAddress.setText("");
                    editTextReference.setText("");
                    switchCompatEnable.setChecked(false);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please enter device name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextName.getText().length()!=0 && editTextIpAddress.getText().length()!=0) {
                    DeviceElena deviceElena = new DeviceElena(editTextIpAddress.getText().toString());
                    deviceElena.requestToDevice(getApplicationContext(), deviceElena.toggleLED("GREEN"));
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please enter device name and ip address", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShowDevices.class);
                startActivity(intent);
            }
        });
    }
}