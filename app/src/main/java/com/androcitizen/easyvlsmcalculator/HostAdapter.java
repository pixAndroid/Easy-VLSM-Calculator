package com.androcitizen.easyvlsmcalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HostAdapter extends RecyclerView.Adapter<HostAdapter.HostHolder> {
    private Context mContext;
    private ArrayList<Long> mHostsList;
    private MyInterface myInterface;

    public HostAdapter(Context mContext, ArrayList<Long> mHostsList, MyInterface listener) {
        this.mContext = mContext;
        this.mHostsList = mHostsList;

        this.myInterface = listener;
    }

    @NonNull
    @Override
    public HostAdapter.HostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HostHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.layout_host_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HostAdapter.HostHolder holder, final int position) {
        long hosts = mHostsList.get(position);
        holder.txtHost.setText(String.valueOf(hosts));
        holder.txtNetNo.setText("N" + (position+1) + " - ");
        if (hosts > 0 && hosts <255) {
            holder.bg.setBackground(mContext.getResources().getDrawable(R.drawable.bg_host_c));
        } else if (hosts>254 && hosts < 65535) {
            holder.bg.setBackground(mContext.getResources().getDrawable(R.drawable.bg_hosts_b));
        } else if (hosts >65534) {
            holder.bg.setBackground(mContext.getResources().getDrawable(R.drawable.bg_hosts_a));
        }

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
        private RelativeLayout bg;
        public HostHolder(@NonNull View itemView) {
            super(itemView);
            txtHost = itemView.findViewById(R.id.txt_host);
            txtNetNo = itemView.findViewById(R.id.txt_net_number);
            btnRemoveHost = itemView.findViewById(R.id.btn_remove_host);
            bg = itemView.findViewById(R.id.rl_host_bg_color);
        }
    }
}
