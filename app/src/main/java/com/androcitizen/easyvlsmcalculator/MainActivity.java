package com.androcitizen.easyvlsmcalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyInterface{
    private EditText etHost, etIp, etPrefix;
    private Button buttonAdd, btnCalculate;

    private HostAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView, mRecyclerVIewVLSM;
    private VLSMadapter mVLASMadapter;


    private ArrayList<Long> mHostsList;
    private String ipAddress = "192.168.1.19";
    private ArrayList<Integer> ipOctetsList;
    private ArrayList<Integer> maskOctetsList;
    private List<VlsmModel> mVlsmList;

    private List<String> mMaskList;
    private boolean isValidIP = false;
    private boolean isValidPrefix = false;

    int firstOctIP, secondOctIP, thirdOctIP, fourthOctIP;
    int firstOctMASK, secondOctMASK, thirdOctMASK, fourthOctMASK;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etHost = findViewById(R.id.et_hosts);
        etIp = findViewById(R.id.et_ip);
        etPrefix = findViewById(R.id.et_prefix);
        buttonAdd = findViewById(R.id.btn_add_hosts);
        btnCalculate = findViewById(R.id.btn_calculate);
        mRecyclerView = findViewById(R.id.recylerlist_hosts);
        mRecyclerVIewVLSM = findViewById(R.id.recylerlist_vlsm_list);

        if (!isValidIP) {
            buttonAdd.setEnabled(false);
            btnCalculate.setEnabled(false);
        } else {
            buttonAdd.setEnabled(true);
            btnCalculate.setEnabled(true);
        }

        if (savedInstanceState != null) {
            mVlsmList = (List<VlsmModel>) savedInstanceState.getSerializable("vlsmlist");
            getVLSMList();
        }

        mHostsList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mAdapter = new HostAdapter(this, mHostsList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

//         mMaskList = (ArrayList<String>) Arrays.asList(getResources().getStringArray(R.array.subnetMaskList));
        mMaskList = Arrays.asList(getResources().getStringArray(R.array.subnetMaskList));

        //Listeners
        editTextIpListener();
        editTextPrefixListener();
        editTextHostListener();
        buttonAddHosts();
        buttonCalculate();

    }

    private void editTextPrefixListener() {
        etPrefix.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(etPrefix.getText().toString())
                        && !TextUtils.isEmpty(etIp.getText().toString())) {

                    if (Integer.valueOf(etPrefix.getText().toString()) >= 8 &&
                            Integer.valueOf(etPrefix.getText().toString()) <= 32) {
                        //Valid Prefix
                        isValidPrefix = true;
                        if (isValidIP) {
                            btnCalculate.setEnabled(true);
                        }

                    } else {
                        isValidPrefix = false;
                        btnCalculate.setEnabled(false);
                    }
                } else {
                    btnCalculate.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void editTextIpListener() {
        etIp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ipOctetsList = DataConfig.getConvertedStringArrayList(etIp.getText().toString());
                isValidIP = DataConfig.validationOfIpAddress(ipOctetsList);
                if (isValidIP) {
                    firstOctIP = ipOctetsList.get(0);
                    secondOctIP = ipOctetsList.get(1);
                    thirdOctIP = ipOctetsList.get(2);
                    fourthOctIP = ipOctetsList.get(3);


                    if (isValidPrefix)
                        btnCalculate.setEnabled(true);
                    Toast.makeText(MainActivity.this, "Valid IP", Toast.LENGTH_LONG).show();
                } else {
                    firstOctIP = 0;
                    secondOctIP = 0;
                    thirdOctIP = 0;
                    fourthOctIP = 0;
                    btnCalculate.setEnabled(false);
                    Toast.makeText(MainActivity.this, "Invalid IP " + DataConfig.ErrorMessageIP, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void buttonCalculate() {
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstOctIP == 0 || firstOctIP >= 224) {
                    Toast.makeText(MainActivity.this, "Oct1 == 0 || Oct1 >= 224", Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<Long> sortedHostsList = new ArrayList<>();
                sortedHostsList = mHostsList;

                Collections.sort(sortedHostsList);
                Collections.reverse(sortedHostsList);
                mAdapter.setHostsList(sortedHostsList);

                if (!TextUtils.isEmpty(etIp.getText())
                        && !TextUtils.isEmpty(etPrefix.getText())
                        && !mHostsList.isEmpty()){

                    if (isValidIP && isValidPrefix) {

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

                            Toast.makeText(MainActivity.this, "/"+prefixValue + "  "+sMaskValue, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //Something going wrong
                        Toast.makeText(MainActivity.this, "Something going wrong", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    //Some fields may be empty
                    Toast.makeText(MainActivity.this, "Some fields are may be empty", Toast.LENGTH_SHORT).show();

                }

                getVLSMList();


            }
        });
    }

    private void getVLSMList() {
        if (!mVlsmList.isEmpty()) {
            mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mVLASMadapter = new VLSMadapter(getApplicationContext(), mVlsmList);
            mRecyclerVIewVLSM.setLayoutManager(mLayoutManager);
            mRecyclerVIewVLSM.setAdapter(mVLASMadapter);
        }
    }

    private int getnValue(long hostValue) {
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

    private void buttonAddHosts() {
        buttonAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                try {
                    if (Long.parseLong(etHost.getText().toString()) > 0 &&
                            Long.parseLong(etHost.getText().toString()) <= 16777214) {

                        mHostsList.add(Long.valueOf(etHost.getText().toString()));
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.scrollToPosition(mHostsList.size());
                        etHost.setText("");
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid Host: " + etHost.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                }catch (NumberFormatException ex) {
                    System.out.println(ex);
                }
            }
        });
    }

    private void editTextHostListener() {
        etHost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Validate
                if (etHost.getText().toString().equals("")) {
                    buttonAdd.setEnabled(false);
                } else {
                    buttonAdd.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void removeArrayAt(int position) {
        if (mHostsList.size() != 0) {
            mHostsList.remove(position);
            mAdapter.setHostsList(mHostsList);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("vlsmlist", (Serializable) mVlsmList);
    }
}
