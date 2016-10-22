package com.caribou.yaweapp;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.caribou.yaweapp.fragment.AdminFragment;
import com.caribou.yaweapp.fragment.GalleryFragment;
import com.caribou.yaweapp.fragment.HomeFragment;
import com.caribou.yaweapp.fragment.MemberFragment;


public class MainActivity extends AppCompatActivity implements GalleryFragment.GalleryFragmentCallback, HomeFragment.HomeFragmentCallback, MemberFragment.MemberFragmentCallback {

    private Toolbar toolbar;

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    boolean isHeretic;
    boolean isAdmin;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        isHeretic = prefs.getBoolean("isHeretic", false);
        isAdmin = prefs.getBoolean("isAdmin", false);


        /**
         *Setup the DrawerLayout and NavigationView
         */
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff);

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
        /**
         * Setup click events on the Navigation View Items.
         */


        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

                if (menuItem.getItemId() == R.id.nav_item_home) {
                    Intent goHome = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(goHome);
                }
                if (menuItem.getItemId() == R.id.nav_item_listMember) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, new MemberFragment()).commit();
                }
                if (menuItem.getItemId() == R.id.nav_item_myAccount) {
                    Intent goMyAccount = new Intent(MainActivity.this, MyAccountActivity.class);
                    startActivity(goMyAccount);
                }
                if (menuItem.getItemId() == R.id.nav_item_listEvent) {
                    Intent goListEvents = new Intent(MainActivity.this, ListEventsActivity.class);
                    startActivity(goListEvents);
                }
                if (menuItem.getItemId() == R.id.nav_item_admin) {
                    if(isAdmin){
                        FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                        xfragmentTransaction.replace(R.id.containerView, new AdminFragment()).commit();
                    } else {
                        Toast.makeText(MainActivity.this, R.string.you_are_not_admin, Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

    }


    @Override
    public void intentCreatePicture() {
        Intent goCreatePicture = new Intent(MainActivity.this, CreatePictureActivity.class);
        startActivity(goCreatePicture);
    }

    @Override
    public void intentMyAccount() {
        Intent goMyAccount = new Intent(MainActivity.this, MyAccountActivity.class);
        startActivity(goMyAccount);
    }

    @Override
    public void intentListEvents() {
        Intent goListEvents = new Intent(MainActivity.this, ListEventsActivity.class);
        startActivity(goListEvents);
    }

}
