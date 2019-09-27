package com.androcitizen.easyvlsmcalculator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VLSMadapter extends RecyclerView.Adapter<VLSMadapter.Holder> {
    private Context mCOntext;
    private List<VlsmModel> mVlsmList;
    private ArrayList<Long> mHostsList;

    public VLSMadapter(Context mCOntext, List<VlsmModel> mVlsmList, ArrayList<Long> mHostsList) {
        this.mCOntext = mCOntext;
        this.mVlsmList = mVlsmList;
        this.mHostsList = mHostsList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mCOntext).inflate(R.layout.layout_vlsm_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.txtNETLABEL.setText("Network 0" + String.valueOf(mHostsList.indexOf(mVlsmList.get(position).getHosts_needed())+1));
        holder.txtNW.setText(mVlsmList.get(position).getNetwork() + " /" + mVlsmList.get(position).getPrefix());
        holder.txtFH.setText(mVlsmList.get(position).getFirstHost());
        holder.txtLH.setText(mVlsmList.get(position).getLastHost());
        holder.txtBR.setText(mVlsmList.get(position).getBroadcast());
        holder.txtMASK.setText(mVlsmList.get(position).getMask());
        holder.txtWILDCARD.setText(mVlsmList.get(position).getWildcard());
        holder.txtHOSTSAVAILABLE.setText(String.valueOf(mVlsmList.get(position).getHosts_available()));
        holder.txtHOSTsNEEDED.setText(String.valueOf(mVlsmList.get(position).getHosts_needed()));
        holder.txtHOSTSUNUSED.setText(String.valueOf(mVlsmList.get(position).getHosts_unused()));
        holder.pbUSAGE.setProgress((int) mVlsmList.get(position).getPercentage());
        holder.txtPERCENT.setText(holder.pbUSAGE.getProgress() + "%");
    }

    @Override
    public int getItemCount() {
        return mVlsmList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView txtNW, txtFH, txtLH, txtBR, txtMASK, txtWILDCARD,
                txtHOSTsNEEDED,txtHOSTSAVAILABLE, txtHOSTSUNUSED, txtPERCENT, txtNETLABEL;
        private ProgressBar pbUSAGE;

        public Holder(@NonNull View itemView) {
            super(itemView);
            txtNETLABEL = itemView.findViewById(R.id.txt_net_label);
            txtNW = itemView.findViewById(R.id.txt_NW);
            txtFH = itemView.findViewById(R.id.txt_FH);
            txtLH = itemView.findViewById(R.id.txt_LH);
            txtBR = itemView.findViewById(R.id.txt_BR);
            txtMASK = itemView.findViewById(R.id.txt_MASK);
            txtWILDCARD = itemView.findViewById(R.id.txt_WILDCARD);
            txtHOSTsNEEDED = itemView.findViewById(R.id.txt_HOSTS_NEEDED);
            txtHOSTSAVAILABLE = itemView.findViewById(R.id.txt_HOSTS_AVAILABLE);
            txtHOSTSUNUSED = itemView.findViewById(R.id.txt_HOSTS_UNUSED);
            txtPERCENT = itemView.findViewById(R.id.txt_percent);
            pbUSAGE = itemView.findViewById(R.id.pb_USAGE);
        }
    }
}
