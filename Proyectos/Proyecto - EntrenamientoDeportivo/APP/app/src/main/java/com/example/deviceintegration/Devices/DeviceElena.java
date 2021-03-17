package com.example.deviceintegration.Devices;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class DeviceElena {
    final String ip;
    private String name;
    private String reference;

    public DeviceElena(String ip, String name, String reference) {
        this.ip = ip;
        this.name = name;
        this.reference = reference;
    }

    public DeviceElena(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getStateLed(){
        return "http://" + ip + "/stateLedBuiltin";
    }

    public String setStateLED(String ledName, String state){
        return "http://" + ip + "/setStateLED?LedName=" + ledName + "&State=" + state + "";
    }

    public String toggleLED(String ledName){
        return "http://" + ip + "/toggleLED?LedName=" + ledName;
    }

    public String toggleBuzzer(){
        return "http://" + ip + "/toggleBuzzer";
    }

    public String toggleDouble(String ledName){
        return "http://" + ip + "/toggleDouble?LedName=" + ledName;
    }

    public String resetModule(){
        return "http://" + ip + "/resetModule";
    }

    public void requestToDevice(@org.jetbrains.annotations.NotNull Context context , String url){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //Toast.makeText(context.getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        System.out.println(response);
                        return;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context.getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                System.out.println(error.toString());
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}