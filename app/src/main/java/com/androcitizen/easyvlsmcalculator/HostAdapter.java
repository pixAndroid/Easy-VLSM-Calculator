package com.androcitizen.easyvlsmcalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class HostAdapter extends RecyclerView.Adapter<HostAdapter.HostHolder> {
    private Context mContext;
    private ArrayList<Long> mHostsList;
    MyInterface myInterface;

    public HostAdapter(Context mContext, ArrayList<Long> mHostsList) {
        this.mContext = mContext;
        this.mHostsList = mHostsList;

        myInterface = (MyInterface) mContext;
    }

    @NonNull
    @Override
    public HostAdapter.HostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HostHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.layout_host_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HostAdapter.HostHolder holder, final int position) {
        holder.txtHost.setText(mHostsList.get(position).toString());
        holder.txtNetNo.setText("N" + (position+1) + " - ");

        holder.btnRemoveHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.removeArrayAt(position);
            }
        });
    }

    public void setHostsList(ArrayList<Long> list) {
        mHostsList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mHostsList.size();
    }

    public class HostHolder extends RecyclerView.ViewHolder {
        private TextView txtHost, txtNetNo;
        private ImageView btnRemoveHost;
        public HostHolder(@NonNull View itemView) {
            super(itemView);
            txtHost = itemView.findViewById(R.id.txt_host);
            txtNetNo = itemView.findViewById(R.id.txt_net_number);
            btnRemoveHost = itemView.findViewById(R.id.btn_remove_host);

        }
    }
}
