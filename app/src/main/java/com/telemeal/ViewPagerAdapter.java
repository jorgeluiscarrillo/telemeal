package com.telemeal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bryan on 4/11/2018.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter{
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> FragmentListTitles = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    public int getCount() {
        return FragmentListTitles.size();
    }

    public CharSequence getPageTitle(int position) {
        return FragmentListTitles.get(position);
    }

    public void AddFragment(Fragment fragment, String title)
    {
        fragmentList.add(fragment);
        FragmentListTitles.add(title);
    }
}
