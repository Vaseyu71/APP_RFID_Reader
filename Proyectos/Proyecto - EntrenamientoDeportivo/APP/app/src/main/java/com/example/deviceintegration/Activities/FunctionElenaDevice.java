package com.example.deviceintegration.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deviceintegration.Devices.DeviceElena;
import com.example.deviceintegration.Models.ElenaModel;
import com.example.deviceintegration.MySQLiteDatabase.MySQLiteDB;
import com.example.deviceintegration.R;
import com.example.deviceintegration.RecyclerView.ElenaAdapter;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Timer;


public class FunctionElenaDevice extends AppCompatActivity {

    private static final long START_TIME_IN_MILLIS = 30000;
    private TextView textViewTime;
    private TextView textViewSpeed;

    private String ledColor = "RED";
    private String functionDevice = "TOGGLE_LED";

    private CountDownTimer countDownTimer;
    private volatile boolean stopThread = false;
    private long mTimeLeftMillis = START_TIME_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_elena_device);

        textViewTime = findViewById(R.id.textViewTime);
        textViewSpeed = findViewById(R.id.textViewSpeed);

        updateTextViewTime();
    }

    public void chooseLed(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.rbRed:
                if (checked)
                    ledColor = "RED";
                    break;

            case R.id.rbGreen:
                if (checked)
                    ledColor = "GREEN";
                    break;

            case R.id.rbBlue:
                if (checked)
                    ledColor = "BLUE";
                    break;
        }
        Toast.makeText(getApplicationContext(), ledColor, Toast.LENGTH_SHORT).show();
    }

    public void chooseFunction(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.rbFunctionOne:
                if (checked)
                    functionDevice = "TOGGLE_LED";
                    break;

            case R.id.rbFunctionTwo:
                if (checked)
                    functionDevice = "TOGGLE_BUZZER";
                    break;

            case R.id.rbFunctionThree:
                if (checked)
                    functionDevice = "TOGGLE_DOUBLE";
                    break;

            case R.id.rbFunctionFour:
                if (checked)
                    functionDevice = "TOUCH";
                break;
        }
        Toast.makeText(getApplicationContext(), functionDevice, Toast.LENGTH_SHORT).show();
    }

    public void startFunction(View view) {
        if (!stopThread) {
            MySQLiteDB mySQLiteDB = new MySQLiteDB(getApplicationContext());
            List<ElenaModel> list = mySQLiteDB.selectDeviceActive("Enable");
            if (!list.isEmpty()) {
                PrimeThread primeThread = new PrimeThread(list);
                primeThread.start();
                mTimeLeftMillis = Long.parseLong(textViewTime.getText().toString())*1000;
                startTimer();
            }
            else {
                Toast.makeText(getApplicationContext(), "No hay dispositivos habilitados", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Sistema en proceso", Toast.LENGTH_SHORT).show();
        }
    }

    public void stopFunction(View view) {
        resetTimer();
    }

    public void addSpeed(View view) {
        int speed = Integer.parseInt(textViewSpeed.getText().toString());
        speed += 50;
        if (speed > 5000) {
            speed = 5000;
        }
        textViewSpeed.setText(Integer.toString(speed));
    }

    public void addTime(View view) {
        int time = Integer.parseInt(textViewTime.getText().toString());
        time += 5;
        if (time > 300) {
            time = 300;
        }
        else if (time < 30) {
            time = 30;
        }
        textViewTime.setText(Integer.toString(time));
    }

    public void subSpeed(View view) {
        int speed = Integer.parseInt(textViewSpeed.getText().toString());
        speed -= 100;
        if (speed < 500) {
            speed = 500;
        }
        textViewSpeed.setText(Integer.toString(speed));
    }

    public void subTime(View view) {
        int time = Integer.parseInt(textViewTime.getText().toString());
        time -= 5;
        if (time < 30) {
            time = 30;
        }
        textViewTime.setText(Integer.toString(time));
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(mTimeLeftMillis, 1000) {
            @Override
            public void onTick(long l) {
                mTimeLeftMillis = l;
                updateTextViewTime();
            }

            @Override
            public void onFinish() {
                mTimeLeftMillis = 30000;
                updateTextViewTime();
                stopThread = false;
            }
        }.start();

        stopThread = true;
    }

    private void resetTimer() {
        if (stopThread) {
            countDownTimer.cancel();
            mTimeLeftMillis = 30000;
            updateTextViewTime();
            stopThread = false;
        }
    }

    private void updateTextViewTime() {
        int seconds = (int) (mTimeLeftMillis/1000)%60;
        textViewTime.setText(Integer.toString(seconds).toString());
    }

    class PrimeThread extends Thread {

        private List<ElenaModel> list;
        private String[] myIp;

        PrimeThread(List<ElenaModel> list) {
            this.list = list;
            myIp = getIP().split("\\.");
            stopThread = true;
        }

        @Override
        public void run() {
            List<DeviceElena> listDeviceElena = new ArrayList<>();
            for (int x = 0; x < list.size(); x++){
                String ipDevice = myIp[0] + "." + myIp[1] + "." + myIp[2] + "." + list.get(x).getIpHost();
                //ipDevice = "192.168.1.150";
                listDeviceElena.add(new DeviceElena(ipDevice, list.get(x).getHostName(), list.get(x).getReference()));
            }
            String url = "";
            while (stopThread) {
                try {
                    int value = (int) (Math.random()*list.size() + 1);
                    if (functionDevice.equals("TOGGLE_BUZZER")) {
                        url = listDeviceElena.get(value - 1).toggleBuzzer();
                    }
                    else if (functionDevice.equals("TOGGLE_DOUBLE")) {
                        url = listDeviceElena.get(value - 1).toggleDouble(ledColor);
                    }
                    else if (functionDevice.equals("TOUCH")) {
                        url = listDeviceElena.get(value - 1).setStateLED(ledColor, "ON");
                    }
                    else {
                        url = listDeviceElena.get(value - 1).toggleLED(ledColor);
                    }
                    listDeviceElena.get(value - 1).requestToDevice(getApplicationContext(), url);
                    int speed = Integer.parseInt(textViewSpeed.getText().toString());
                    Thread.sleep(speed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public String getIP(){
            List<InetAddress> listAddress;
            String address = "";
            try{
                List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
                for(NetworkInterface networkInterface : interfaces){
                    listAddress = Collections.list(networkInterface.getInetAddresses());
                    for(InetAddress inetAddress : listAddress){
                        if(!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address){
                            address = inetAddress.getHostAddress().toUpperCase(new Locale("es", "MX"));
                        }
                    }
                }
            }catch (Exception e){
                String TAG = "Message";
                Log.w(TAG, "Ex getting IP value " + e.getMessage());
            }
            return address;
        }
    }
}