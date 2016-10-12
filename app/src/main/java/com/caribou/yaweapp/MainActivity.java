package com.caribou.yaweapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.caribou.yaweapp.fragment.AdminFragment;
import com.caribou.yaweapp.fragment.ChatFragment;
import com.caribou.yaweapp.fragment.GalleryFragment;
import com.caribou.yaweapp.fragment.HomeFragment;
import com.caribou.yaweapp.fragment.MemberFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdminFragment.AdminFragmentCallback, GalleryFragment.GalleryFragmentCallback, HomeFragment.HomeFragmentCallback, MemberFragment.MemberFragmentCallback {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        try {
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {

        }


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        boolean isHeretic = prefs.getBoolean("isHeretic", false);
        if (isHeretic) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccentHeretic));
            tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryHeretic));
        } else {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_createPicture:
                Intent goCreatePicture = new Intent(MainActivity.this, CreatePictureActivity.class);
                startActivity(goCreatePicture);
                break;
            case R.id.action_myAccount:
                Intent goMyAccount = new Intent(MainActivity.this, MyAccountActivity.class);
                startActivity(goMyAccount);
                break;
            case R.id.action_listEvent:
                Intent goListEvents = new Intent(MainActivity.this, ListEventsActivity.class);
                startActivity(goListEvents);
                break;
        }

        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), getString(R.string.home));
        adapter.addFragment(new GalleryFragment(), getString(R.string.gallery));
        adapter.addFragment(new ChatFragment(), "Chat room");
        adapter.addFragment(new MemberFragment(), "Member");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        boolean isAdmin = prefs.getBoolean("isAdmin", false);
        if (isAdmin) {
            Toast.makeText(MainActivity.this, R.string.you_are_admin, Toast.LENGTH_SHORT).show();
            adapter.addFragment(new AdminFragment(), "ADMIN");
        }
        boolean isHeretic = prefs.getBoolean("isHeretic", false);
        if (isHeretic) {
            Toast.makeText(MainActivity.this, R.string.you_are_heretic, Toast.LENGTH_SHORT).show();
        }

        viewPager.setAdapter(adapter);
    }

    @Override
    public void intentGoCreateUser() {
        Intent goCreateUser = new Intent(MainActivity.this, CreateUserActivity.class);
        startActivity(goCreateUser);
    }

    @Override
    public void intentGoCreateEvent() {
        Intent goCreateEvent = new Intent(MainActivity.this, CreateEventActivity.class);
        startActivity(goCreateEvent);
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

    public void intentGoListUsers() {
        Intent goListUsers = new Intent(MainActivity.this, ListUsersActivity.class);
        startActivity(goListUsers);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {

            if (fragment instanceof AdminFragment) {
                ((AdminFragment) fragment).setCallback(MainActivity.this);
            } else if (fragment instanceof GalleryFragment) {
                ((GalleryFragment) fragment).setCallback(MainActivity.this);
            } else if (fragment instanceof HomeFragment) {
                ((HomeFragment) fragment).setCallback(MainActivity.this);
            } else if (fragment instanceof ChatFragment) {
                ((ChatFragment) fragment).setCallback(MainActivity.this);
            } else if (fragment instanceof MemberFragment) {
                ((MemberFragment) fragment).setCallback(MainActivity.this);
            }
//            else  if(fragment instanceof ChatFragment){
//
//            }

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
