package com.example.deviceintegration.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deviceintegration.Activities.FunctionElenaDevice;
import com.example.deviceintegration.Devices.DeviceElena;
import com.example.deviceintegration.Models.ElenaModel;
import com.example.deviceintegration.MySQLiteDatabase.MySQLiteDB;
import com.example.deviceintegration.R;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ElenaAdapter extends RecyclerView.Adapter<ElenaAdapter.elenaHolder> {

    private List<ElenaModel> list;

    public ElenaAdapter(List<ElenaModel> list) {
        this.list = list;
    }

    public void setList(List<ElenaModel> list) {
        this.list = list;
    }

    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(String hostName);
        void onItemDelete(String hostName);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public elenaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_elena_device, parent, false);
        return new elenaHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull elenaHolder holder, int position) {
        holder.hostName.setText(String.valueOf(list.get(position).getHostName()));
        holder.ipHost.setText(String.valueOf(list.get(position).getIpHost()));
        holder.reference.setText(list.get(position).getReference());

        String state = list.get(position).getState();
        if (state.equals("Enable")) {
            holder.enableDevice.setChecked(true);
        }
        else {
            holder.enableDevice.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class elenaHolder extends RecyclerView.ViewHolder{

        private TextView hostName;
        private TextView ipHost;
        private TextView reference;
        private ImageButton infoDevice;
        private ImageButton deleteDevice;
        private SwitchCompat enableDevice;
        private String[] myIp;

        public elenaHolder(@NonNull View view, OnItemClickListener onItemClickListener){
            super(view);
            configView(view, onItemClickListener);
            myIp = getIP().split("\\.");
            System.out.println(myIp);
        }

        private void configView(View view, OnItemClickListener onItemClickListener){
            hostName = view.findViewById(R.id.host_name);
            ipHost = view.findViewById(R.id.ip_host);
            reference = view.findViewById(R.id.reference);

            infoDevice = view.findViewById(R.id.configDevice);
            deleteDevice = view.findViewById(R.id.deleteDevice);
            enableDevice = view.findViewById(R.id.enableDevice);

            final MySQLiteDB mySQLiteDB = new MySQLiteDB(view.getContext());

            infoDevice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String ipDevice = myIp[0] + "." + myIp[1] + "." + myIp[2] + "." + ipHost.getText().toString();
                    //ipDevice = "192.168.1.150";
                    DeviceElena deviceElena = new DeviceElena(ipDevice);
                    deviceElena.requestToDevice(view.getContext(), deviceElena.toggleLED("GREEN"));
                }
            });

            deleteDevice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mySQLiteDB.deleteDevice(hostName.getText().toString());
                    onItemClickListener.onItemDelete(hostName.getText().toString());
                }
            });

            enableDevice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String state = "Disable";
                    if (enableDevice.isChecked()) {
                        state = "Enable";
                    }
                    mySQLiteDB.updateDevice(hostName.getText().toString(), ipHost.getText().toString(), reference.getText().toString(), state);
                }
            });
        }
    }

    public static String getIP(){
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
