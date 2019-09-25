package com.androcitizen.easyvlsmcalculator;

import java.io.Serializable;

public class VlsmModel implements Serializable {

    private String network;
    private String firstHost;
    private String lastHost;
    private String broadcast;
    private int prefix;
    private String mask;
    private String wildcard;
    private long hosts_available;
    private long hosts_needed;
    private long hosts_unused;
    private double percentage;

    /*public VlsmModel() {
    }*/

    public VlsmModel(String network, String firstHost, String lastHost, String broadcast,
                     int prefix, String mask, String wildcard, long hosts_available,
                     long hosts_needed, long hosts_unused, double percentage) {
        this.network = network;
        this.firstHost = firstHost;
        this.lastHost = lastHost;
        this.broadcast = broadcast;
        this.prefix = prefix;
        this.mask = mask;
        this.wildcard = wildcard;
        this.hosts_available = hosts_available;
        this.hosts_needed = hosts_needed;
        this.hosts_unused = hosts_unused;
        this.percentage = percentage;
    }

    public String getNetwork() {
        return network;
    }

    public String getFirstHost() {
        return firstHost;
    }

    public String getLastHost() {
        return lastHost;
    }

    public String getBroadcast() {
        return broadcast;
    }

    public int getPrefix() {
        return prefix;
    }

    public String getMask() {
        return mask;
    }

    public String getWildcard() {
        return wildcard;
    }

    public long getHosts_available() {
        return hosts_available;
    }

    public long getHosts_needed() {
        return hosts_needed;
    }

    public long getHosts_unused() {
        return hosts_unused;
    }

    public double getPercentage() {
        return percentage;
    }
}
