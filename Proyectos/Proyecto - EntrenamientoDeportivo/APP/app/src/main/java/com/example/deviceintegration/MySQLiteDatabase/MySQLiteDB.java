package com.example.deviceintegration.MySQLiteDatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.deviceintegration.Models.ElenaModel;

import java.util.ArrayList;
import java.util.List;

public class MySQLiteDB extends SQLiteOpenHelper {
    private static final String NAME_DB = "MySQLiteDB.db";
    private static final int VERSION_DB = 1;
    private static final String CREATE_TABLA_DEVICES = "CREATE TABLE devices(hostName TEXT PRIMARY KEY, ipHost TEXT, reference TEXT, state TEXT)";

    public MySQLiteDB(@Nullable Context context) {
        super(context, NAME_DB, null, VERSION_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLA_DEVICES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLA_DEVICES);
        sqLiteDatabase.execSQL(CREATE_TABLA_DEVICES);
    }

    public void insertDevice(String hostName, String ipHost, String reference, String state) throws Exception{
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        if (sqLiteDatabase!=null){
            try {
                sqLiteDatabase.execSQL("INSERT INTO devices VALUES('" + hostName + "','" + ipHost + "','" + reference+"','" + state + "')");
                sqLiteDatabase.close();
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }
    }

    public List<ElenaModel> selectDevices() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM devices", null);
        List<ElenaModel> elenaModels = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                elenaModels.add(new ElenaModel(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            }while (cursor.moveToNext());
        }
        return elenaModels;
    }

    public void selectDevice(ElenaModel elenaModel, String hostName) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM devices WHERE hostName='" + hostName + "'", null);
        if (cursor.moveToFirst()) {
            do {
                elenaModel.setIpHost(cursor.getString(1));
                elenaModel.setReference(cursor.getString(2));
                elenaModel.setState(cursor.getString(3));
            }while (cursor.moveToNext());
        }
    }

    public void updateDevice(String hostName, String ipHost, String reference, String state) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        if (sqLiteDatabase!=null){
            sqLiteDatabase.execSQL("UPDATE devices SET ipHost='" + ipHost + "',reference='" + reference+"',state='" + state + "' WHERE hostName='" + hostName + "'");
            sqLiteDatabase.close();
        }
    }

    public void deleteDevice(String hostName) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        if (sqLiteDatabase!=null){
            sqLiteDatabase.execSQL("DELETE FROM devices WHERE hostName='" + hostName + "'");
            sqLiteDatabase.close();
        }
    }

    public List<ElenaModel> selectDeviceActive(String state) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM devices WHERE state='" + state + "'", null);
        List<ElenaModel> elenaModels = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                elenaModels.add(new ElenaModel(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            }while (cursor.moveToNext());
        }
        return elenaModels;
    }
}
