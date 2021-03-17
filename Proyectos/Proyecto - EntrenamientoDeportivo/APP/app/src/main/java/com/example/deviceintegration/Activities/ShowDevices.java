package com.example.deviceintegration.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;
import android.widget.Toast;

import com.example.deviceintegration.Models.ElenaModel;
import com.example.deviceintegration.MySQLiteDatabase.MySQLiteDB;
import com.example.deviceintegration.R;
import com.example.deviceintegration.RecyclerView.ElenaAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowDevices extends AppCompatActivity {

    private ElenaAdapter elenaAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    private FloatingActionButton floatingActionButtonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_devices);

        floatingActionButtonAdd = findViewById(R.id.idFabAddDevice);

        buildRecyclerView();
    }

    public void buildRecyclerView() {
        recyclerView = findViewById(R.id.rvDevices);
        layoutManager = new GridLayoutManager(this, 1);
        elenaAdapter = new ElenaAdapter(new ArrayList<>());
        recyclerView.setAdapter(elenaAdapter);
        recyclerView.setLayoutManager(layoutManager);

        final MySQLiteDB mySQliteDB = new MySQLiteDB(getApplicationContext());
        List<ElenaModel> elenaModelList =  mySQliteDB.selectDevices();

        elenaAdapter.setList(elenaModelList);
        elenaAdapter.notifyDataSetChanged();

        elenaAdapter.setOnItemClickListener(new ElenaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String hostName) {
                Toast.makeText(getApplicationContext(), hostName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemDelete(String hostName) {
                final MySQLiteDB mySQliteDB = new MySQLiteDB(getApplicationContext());
                List<ElenaModel> elenaModelList =  mySQliteDB.selectDevices();

                elenaAdapter.setList(elenaModelList);
                elenaAdapter.notifyDataSetChanged();
            }
        });
    }

    public void addDevice(View view) {
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        String myQR = intentResult.getContents();

        try {
            if (myQR != null) {
                try {
                    JSONObject jsonObject = new JSONObject(myQR);
                    MySQLiteDB mySQLiteDB = new MySQLiteDB(getApplicationContext());
                    mySQLiteDB.insertDevice(jsonObject.get("name").toString(), jsonObject.get("idAddress").toString(), jsonObject.get("reference").toString(), "Disable");
                    List<ElenaModel> elenaModelList =  mySQLiteDB.selectDevices();
                    elenaAdapter.setList(elenaModelList);
                    elenaAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}