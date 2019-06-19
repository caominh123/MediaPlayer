package com.lecaoliem.songlist.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> arrayFragment  = new ArrayList<Fragment>();
    ArrayList<String> arraytitle = new ArrayList<String>();
    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i)
    {
        return arrayFragment.get(i);
    }

    @Override
    public int getCount() {

        return arrayFragment.size();
    }
    public void addFragment(Fragment fragment, String title){
        arrayFragment.add(fragment);
        arraytitle.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return arraytitle.get(position);
    }
}
