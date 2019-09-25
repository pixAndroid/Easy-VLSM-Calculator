package com.androcitizen.easyvlsmcalculator;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Scanner;

public class DataConfig {
    private static ArrayList<Integer> dataMask = new ArrayList<>();
    public static String ErrorMessageIP = "";

    public static ArrayList<Integer> getConvertedStringArrayList(String stringData) {
        if (dataMask.size() != 0)
            dataMask.clear();

        Scanner fromString = new Scanner(stringData);
        fromString.useDelimiter("\\.");
        while (fromString.hasNext()) {
            String temp = fromString.next();
            try {
                dataMask.add(Integer.valueOf(temp));
            }catch (NumberFormatException ignored) {}
        }
        return dataMask;
    }

    public static boolean validationOfIpAddress(ArrayList<Integer> ipOctetData) {
        if (ipOctetData.size() < 4) {
            ErrorMessageIP = "Need 4 Octets";
            return false;
        }else if (ipOctetData.size() > 4) {
            ErrorMessageIP = "invalid Octet";
            return false;
        }

        int octetValue;
        boolean validIp = false;
        for (int c = 0; c < ipOctetData.size(); c++) {
            try {
                if (ipOctetData.get(0) == 0) {
                    ErrorMessageIP = "Error! 1stOctet 0 :(";
                    break;
                }

                octetValue = ipOctetData.get(c);
                if (octetValue >= 0 && octetValue <= 255) {
                    //Valid IP & get Class
                    ErrorMessageIP = "";
                    validIp = true;
                } else {
                    ErrorMessageIP = "Oct." + (c+1) + " too large value :(";
                    validIp = false;
                    return validIp;
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return validIp;
    }
}
