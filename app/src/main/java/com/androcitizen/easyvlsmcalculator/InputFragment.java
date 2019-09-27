package com.androcitizen.easyvlsmcalculator;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InputFragment extends Fragment implements MyInterface{
    private EditText etHost, etIp, etPrefix;
    private Button buttonAdd, btnCalculate,btnClear;

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

    private InputFragment mInputFragment;

    private OnOutputResult outputResultListener;

    private SharedPrefConfig mSharedPrefConfig;

    public InputFragment() {
    }

    public interface OnOutputResult{
        void getResult(List<VlsmModel> list, ArrayList<Long> hostsList);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        outputResultListener = (OnOutputResult) context;
        mSharedPrefConfig = new SharedPrefConfig(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root =inflater.inflate(R.layout.fragment_input, container, false);
        etHost = root.findViewById(R.id.et_hosts);
        etIp = root.findViewById(R.id.et_ip);
        etPrefix = root.findViewById(R.id.et_prefix);
        buttonAdd = root.findViewById(R.id.btn_add_hosts);
        btnClear = root.findViewById(R.id.btn_clear);
        btnCalculate = root.findViewById(R.id.btn_calculate);
        mRecyclerView = root.findViewById(R.id.recylerlist_hosts);

        mInputFragment = new InputFragment();

        if (!isValidIP) {
            buttonAdd.setEnabled(false);
            btnCalculate.setEnabled(false);
        } else {
            buttonAdd.setEnabled(true);
            btnCalculate.setEnabled(true);
        }

        /*if (savedInstanceState != null) {
            mVlsmList = (List<VlsmModel>) savedInstanceState.getSerializable("vlsmlist");
            getVLSMList();
        }*/

        mHostsList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mAdapter = new HostAdapter(getContext(), mHostsList, mInputFragment);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mMaskList = Arrays.asList(getResources().getStringArray(R.array.subnetMaskList));

        loadFromSharedPref();

        //Listeners
        editTextIpListener();
        editTextPrefixListener();
        editTextHostListener();
        buttonAddHosts();
        buttonCalculate();
        buttonClear();

        return root;
    }

    private void buttonClear() {
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSharedPrefConfig.writeNetworkAddress("");
                mSharedPrefConfig.writePrefix("");
                mSharedPrefConfig.writeHostsList("");

                etIp.setText("");
                etPrefix.setText("");
                etHost.setText("");

                if (!mHostsList.isEmpty()) {
                    mHostsList.clear();
                    mAdapter.setHostsList(mHostsList);
                }
            }
        });
    }

    private void loadFromSharedPref() {
        //Load from Shared Preference
        etIp.setText(mSharedPrefConfig.getNetworkAddress());
        etPrefix.setText(mSharedPrefConfig.getPrefix());
        //Set<String> hostsList;
        String stringHosts = mSharedPrefConfig.getHostsList();
        Gson gson = new Gson();

        if (!stringHosts.equals("")) {
            ArrayList<String> stringArrayList = gson.fromJson(stringHosts, ArrayList.class);
            for (String s : stringArrayList) {
                mHostsList.add(Long.valueOf(s));
            }
            mAdapter.setHostsList(mHostsList);
            if (!TextUtils.isEmpty(etIp.getText()) &&
                    !TextUtils.isEmpty(etPrefix.getText())) {
                validateNetworkAddress();
                validatePrefix();
                btnCalculate.setEnabled(true);
            } else {btnCalculate.setEnabled(false);}

            //getVLSMList();
        }
    }

    private void validatePrefix() {
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

    private void validateNetworkAddress() {
        ipOctetsList = DataConfig.getConvertedStringArrayList(etIp.getText().toString());
        isValidIP = DataConfig.validationOfIpAddress(ipOctetsList);
        if (isValidIP) {
            firstOctIP = ipOctetsList.get(0);
            secondOctIP = ipOctetsList.get(1);
            thirdOctIP = ipOctetsList.get(2);
            fourthOctIP = ipOctetsList.get(3);


            if (isValidPrefix)
                btnCalculate.setEnabled(true);
            Toast.makeText(getContext(), "Valid IP", Toast.LENGTH_LONG).show();
        } else {
            firstOctIP = 0;
            secondOctIP = 0;
            thirdOctIP = 0;
            fourthOctIP = 0;
            btnCalculate.setEnabled(false);
//            Toast.makeText(VlsmActivity.this, "Invalid IP " + DataConfig.ErrorMessageIP, Toast.LENGTH_LONG).show();
        }
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
                    Toast.makeText(getContext(), "Valid IP", Toast.LENGTH_LONG).show();
                } else {
                    firstOctIP = 0;
                    secondOctIP = 0;
                    thirdOctIP = 0;
                    fourthOctIP = 0;
                    btnCalculate.setEnabled(false);
                    Toast.makeText(getContext(), "Invalid IP " + DataConfig.ErrorMessageIP, Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getContext(), "Oct1 == 0 || Oct1 >= 224", Toast.LENGTH_SHORT).show();
                    return;
                }

                /*ArrayList<Long> sortedHostsList = new ArrayList<>();
                sortedHostsList = mHostsList;  this is wrong*/

                mVlsmList = CalculateVLSM.getVLSM(mHostsList,
                        mMaskList,
                        maskOctetsList,
                        firstOctIP,
                        secondOctIP,
                        thirdOctIP,
                        firstOctMASK,
                        secondOctMASK,
                        thirdOctMASK,
                        fourthOctMASK);

                if (!mVlsmList.isEmpty()) {
                    outputResultListener.getResult(mVlsmList, mHostsList);

                    ArrayList<String> stringHosts = new ArrayList<>();
                    for (Long host: mHostsList) {
                        stringHosts.add(String.valueOf(host));
                    }
                    if (!stringHosts.isEmpty()) {
                        Gson go = new Gson();
                        String ho = go.toJson(stringHosts);
                        mSharedPrefConfig.writeHostsList(ho);
                    }

                    mSharedPrefConfig.writeNetworkAddress(etIp.getText().toString());
                    mSharedPrefConfig.writePrefix(etPrefix.getText().toString());
                }


            }
        });
    }

    private void buttonAddHosts() {
        buttonAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                try {
                    if (Long.parseLong(etHost.getText().toString()) > 0 &&
                            Long.parseLong(etHost.getText().toString()) <= 16777214) {

                        mHostsList.add(Long.valueOf(etHost.getText().toString()));
                        mAdapter.setHostsList(mHostsList);

                        if (mHostsList.size() >= 2)
                            mRecyclerView.smoothScrollToPosition(mHostsList.size()-1);

                        etHost.setText("");
                    } else {
                        Toast.makeText(getContext(), "Invalid Host: " + etHost.getText().toString(), Toast.LENGTH_SHORT).show();
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

}
