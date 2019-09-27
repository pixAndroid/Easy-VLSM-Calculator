package com.androcitizen.easyvlsmcalculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class OutputFragment extends Fragment {
    private RecyclerView mRecyclerViewVlsmList;
    private LinearLayoutManager mLayoutManager;
    private VLSMadapter mVLASMadapter;

    private List<VlsmModel> mVlsmList;
    private ArrayList<Long> hostsList;

    public OutputFragment() {
    }

    public void setmVlsmList(List<VlsmModel> mVlsmList) {
        this.mVlsmList = mVlsmList;
    }

    public void setHostsList(ArrayList<Long> hostsList) {
        this.hostsList = hostsList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_output, container, false);
        mRecyclerViewVlsmList = root.findViewById(R.id.recylerlist_vlsm_list);

        if (mVlsmList != null) {
            mLayoutManager = new LinearLayoutManager(getContext());
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mVLASMadapter = new VLSMadapter(getContext(), mVlsmList, hostsList);
            mRecyclerViewVlsmList.setLayoutManager(mLayoutManager);
            mRecyclerViewVlsmList.setAdapter(mVLASMadapter);
        }

        return root;
    }
}
