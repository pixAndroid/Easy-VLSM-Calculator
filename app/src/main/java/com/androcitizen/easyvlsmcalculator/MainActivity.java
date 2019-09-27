package com.androcitizen.easyvlsmcalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements InputFragment.OnOutputResult, NavigationView.OnNavigationItemSelectedListener {

    private TabLayout mTabLayout;
    private FragmentManager mFragmentManager;
    private List<VlsmModel> mList;
    private ArrayList<Long> mHostsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabLayout = findViewById(R.id.tabLayout);

        Toolbar toolbar = findViewById(R.id.mtoolbar);
//        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        mList = new ArrayList<>();
        mHostsList = new ArrayList<>();

        for (int i=1; i<=2; i++) {
            switch (i) {
                case 1:
                    mTabLayout.addTab(mTabLayout.newTab().setText("Input"));
                    continue;
                case 2:
                    mTabLayout.addTab(mTabLayout.newTab().setText("Result"));
                    break;
            }
        }

        mFragmentManager = getSupportFragmentManager();

        getInputFragment();
        tabSelectListener();

    }

    private void tabSelectListener() {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabID = mTabLayout.getSelectedTabPosition();
                Toast.makeText(MainActivity.this, "ID" + tabID, Toast.LENGTH_SHORT).show();
                switch (tabID) {
                    case 0:
                        getInputFragment();
                        break;
                    case 1:
                        getOutputFragment(mList, mHostsList);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }*/

    private void getOutputFragment(List<VlsmModel> lists, ArrayList<Long> hosts) {
        mList = lists;
        mHostsList = hosts;
        OutputFragment outputFragment = new OutputFragment();
        outputFragment.setHostsList(hosts);
        outputFragment.setmVlsmList(mList);
        mFragmentManager.beginTransaction().replace(R.id.frame_container, outputFragment)
                .commit();
    }

    private void getInputFragment() {
        InputFragment inputFragment = new InputFragment();
        mFragmentManager.beginTransaction().replace(R.id.frame_container, inputFragment)
                .commit();
    }

    @Override
    public void getResult(List<VlsmModel> list, ArrayList<Long> hosts) {
        getOutputFragment(list, hosts);
        setTabSelected();
    }

    private void setTabSelected() {
        TabLayout.Tab tab = mTabLayout.getTabAt(1);
        tab.select();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();
        if (id == R.id.nav_home) {
            mTabLayout.setVisibility(View.VISIBLE);
            if (mTabLayout.getSelectedTabPosition() == 0)
                getInputFragment();
            else
                getOutputFragment(mList, mHostsList);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_rate) {

        } else if (id == R.id.nav_privacy) {

        } else if (id == R.id.nav_about) {
            mTabLayout.setVisibility(View.GONE);
            getAboutFragment();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void closeDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }

    private void getAboutFragment() {
        FragmentAbout fragmentAbout = new FragmentAbout();
        mFragmentManager.beginTransaction().replace(R.id.frame_container, fragmentAbout)
                .commit();
    }
}
