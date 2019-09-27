package com.androcitizen.easyvlsmcalculator;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class FragmentAbout extends Fragment {

    public FragmentAbout() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        TextView txtVersion = root.findViewById(R.id.txt_app_version);
        TextView txtInstallDate = root.findViewById(R.id.txt_install_date);
        TextView txtPrivacy = root.findViewById(R.id.txt_privacy_policy);

        txtVersion.setText(getVersionInfo());
        txtInstallDate.setText(getAppInstallDateTime());
        txtPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String licenseUrl = "https://sites.google.com/view/awesomestatuscollection";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(licenseUrl));
                startActivity(i);
            }
        });

        return root;

    }

    private String getVersionInfo() {
        String versionName = "";

        try {
            PackageInfo packageInfo = getActivity().getPackageManager()
                    .getPackageInfo(getActivity().getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return String.format("%s", versionName);
    }

    private String getAppInstallDateTime() {
        long installDate;
        String formattedDate = "";
        try {
            installDate = getContext().getPackageManager()
                    .getPackageInfo(this.getActivity().getPackageName(), 0)
                    .firstInstallTime;
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
            formattedDate=dateFormat.format(installDate);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //OUTPUT
        return "Installed Date ➤ " + formattedDate;
    }
}
