package com.androcitizen.easyvlsmcalculator;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CalculateVLSM {
    private static List<VlsmModel> mVlsmList;

    public static List<VlsmModel> getVLSM(
                                          ArrayList<Long> mHostsList,
                                          List<String> mMaskList,
                                          ArrayList<Integer> maskOctetsList,
                                          int firstOctIP,
                                          int secondOctIP,
                                          int thirdOctIP,
                                          int firstOctMASK,
                                          int secondOctMASK,
                                          int thirdOctMASK,
                                          int fourthOctMASK) {
        ArrayList<Long> sortedHostsList = new ArrayList<>(mHostsList);
        //sortedHostsList = mHostsList;

        Collections.sort(sortedHostsList);
        Collections.reverse(sortedHostsList);
//        mAdapter.setHostsList(sortedHostsList);

        mVlsmList = new ArrayList<>();
        int tempNetOctet4th = 0;
        int tempNetOctet3rd = 0;
        int tempNetOctet2nd = 0;
        int tempIpOct3rd = thirdOctIP;
        int tempIpOct2nd = secondOctIP;
        int tempIpOct1st = firstOctIP;
        int BL = 0;

        for (int index = 0; index<sortedHostsList.size(); index++) {
            long neededHosts = sortedHostsList.get(index);
            //Find n
            int nValue = getnValue(neededHosts);
            //PREFIX
            int prefixValue = 32 - nValue;

            //Subnet Mask String
            String sMaskValue = mMaskList.get(prefixValue);
            maskOctetsList = DataConfig.getConvertedStringArrayList(sMaskValue);

            //Separate Mask Octet
            firstOctMASK = maskOctetsList.get(0);
            secondOctMASK = maskOctetsList.get(1);
            thirdOctMASK = maskOctetsList.get(2);
            fourthOctMASK = maskOctetsList.get(3);

            //Wildcard
            String wildcard = (255-firstOctMASK) + "." + (255-secondOctMASK) + "." + (255-thirdOctMASK) + "." + (255-fourthOctMASK);

            //Hosts Available
            long availableHosts = (long) (Math.pow(2, nValue) - 2);
            long unusedHosts = availableHosts - neededHosts;

            if (prefixValue >= 24 && prefixValue <= 32) {

                //Block Size
                BL = 256 - fourthOctMASK;

                int dNetwork = tempNetOctet4th;
                int dFirstIp = dNetwork+1;
                int dLastIp = dNetwork + BL - 2;
                int dBroadcast = dLastIp + 1;
                tempNetOctet4th = dBroadcast+1;

                String network = tempIpOct1st + "." + tempIpOct2nd + "." + tempIpOct3rd + "." + dNetwork;
                String firstIP = tempIpOct1st + "." + tempIpOct2nd + "." + tempIpOct3rd + "." + dFirstIp;
                String secondIP = tempIpOct1st + "." + tempIpOct2nd + "." + tempIpOct3rd + "." + dLastIp;
                String broadcast = tempIpOct1st + "." + tempIpOct2nd + "." + tempIpOct3rd + "." + dBroadcast;

                if (tempNetOctet4th == 256) {
                    tempNetOctet4th = 0;
                    tempIpOct3rd++;
                }

                double usagePercent = (neededHosts * 100)/ (BL-2);

                VlsmModel vlsmModel = new VlsmModel(network, firstIP, secondIP, broadcast,
                        prefixValue, sMaskValue, wildcard, availableHosts,
                        neededHosts, unusedHosts, usagePercent);
                mVlsmList.add(vlsmModel);

            }else if (prefixValue >= 16 && prefixValue <= 23) {
                //Class B
                //Block Size
                BL = 256 - thirdOctMASK;

                int dNetwork = tempNetOctet3rd;
                int dFirstIp = dNetwork;
                int dLastIp = dNetwork + BL-1;
                int dBroadcast = dLastIp;
                tempNetOctet3rd = dBroadcast+1;

                String network = tempIpOct1st + "." + tempIpOct2nd + "." + dNetwork + "." + 0;
                String firstIP = tempIpOct1st + "." + tempIpOct2nd + "." + dFirstIp + "." + 1;
                String secondIP = tempIpOct1st + "." + tempIpOct2nd + "." + dLastIp + "." + 254;
                String broadcast = tempIpOct1st + "." + tempIpOct2nd + "." + dBroadcast + "." + 255;

//                                tempIpOct3rd = dBroadcast;

                if (tempNetOctet3rd == 256) {
                    tempNetOctet3rd = 0;
                    tempIpOct3rd = 0;
                    tempIpOct2nd++;
                } else {
                    tempIpOct3rd = dBroadcast+1;
                }

                double usagePercent = (neededHosts * 100)/ (Math.pow(2, nValue));

                VlsmModel vlsmModel = new VlsmModel(network, firstIP, secondIP, broadcast,
                        prefixValue, sMaskValue, wildcard, availableHosts,
                        neededHosts, unusedHosts, usagePercent);
                mVlsmList.add(vlsmModel);

            } else if (prefixValue >= 8 && prefixValue <= 15) {
                //Class A

                //Block Size
                BL = 256 - secondOctMASK;

                int dNetwork = tempNetOctet2nd;
                int dFirstIp = dNetwork;
                int dLastIp = dNetwork + BL-1;
                int dBroadcast = dLastIp;
                tempNetOctet2nd = dBroadcast+1;

                String network = tempIpOct1st + "." + dNetwork + "." + 0 + "." + 0;
                String firstIP = tempIpOct1st + "." + dFirstIp + "." + 0 + "." + 1;
                String secondIP = tempIpOct1st + "." + dLastIp + "." + 255 + "." + 254;
                String broadcast = tempIpOct1st + "." + dBroadcast + "." + 255 + "." + 255;

                if (tempNetOctet2nd == 256) {
                    tempNetOctet2nd = 0;
                    tempIpOct2nd = 0;
                    tempIpOct1st++;
                } else {
                    tempIpOct2nd = dBroadcast+1;
                }

                double usagePercent = (neededHosts * 100)/ ((Math.pow(2, nValue))-2);

                VlsmModel vlsmModel = new VlsmModel(network, firstIP, secondIP, broadcast,
                        prefixValue, sMaskValue, wildcard, availableHosts,
                        neededHosts, unusedHosts, usagePercent);
                mVlsmList.add(vlsmModel);
            }

//            Toast.makeText(MainActivity.this, "/"+prefixValue + "  "+sMaskValue, Toast.LENGTH_SHORT).show();
        }

        return mVlsmList;
    }

    private static int getnValue(long hostValue) {
        int nValue = 0;
        for (int n = 1; n <= 24; n++) {
            long tempHost = (long) Math.pow(2, (double) n) - 2;
            if ( tempHost < hostValue) {
                nValue = n;
            } else if (tempHost == hostValue) {
                nValue = n;
                break;
            }
            nValue = n;
            if (tempHost > hostValue) {
                break;
            }
        }
        return nValue;
    }
}
