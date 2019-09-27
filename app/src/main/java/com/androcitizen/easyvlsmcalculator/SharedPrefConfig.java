package com.androcitizen.easyvlsmcalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ConcurrentModificationException;

public class SharedPrefConfig {
    private Context mContext;
    private SharedPreferences mSharedPreferences;

    public SharedPrefConfig(Context mContext) {
        this.mContext = mContext;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public void writeNetworkAddress(String address) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("prefkey_network_address", address);
        editor.apply();
    }
    public String getNetworkAddress() {
        return mSharedPreferences.getString("prefkey_network_address", "");
    }

    public void writePrefix(String prefix) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("prefkey_prefix", prefix);
        editor.apply();
    }
    public String getPrefix() {
        return mSharedPreferences.getString("prefkey_prefix", "");
    }

    public void writeHostsList(String stringHostsList) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("prefkey_hosts_list", stringHostsList);
        editor.apply();
    }
    public String getHostsList() {
        return mSharedPreferences.getString("prefkey_hosts_list", "");
    }



}
