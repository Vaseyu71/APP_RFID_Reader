package com.example.deviceintegration.Models;

public class ElenaModel {
    private String hostName;
    private String ipHost;
    private String reference;
    private String state;

    public ElenaModel() {
    }

    public ElenaModel(String hostName, String ipHost, String reference, String state) {
        this.hostName = hostName;
        this.ipHost = ipHost;
        this.reference = reference;
        this.state = state;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getIpHost() {
        return ipHost;
    }

    public void setIpHost(String ipHost) {
        this.ipHost = ipHost;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
