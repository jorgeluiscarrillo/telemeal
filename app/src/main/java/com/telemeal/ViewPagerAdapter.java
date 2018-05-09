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

    /**
     * Represent each page as a View
     * @param fm Application fragment for adding and removing fragments
     */
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Getter for the fragment at a position
     * @param position Position of fragment in the list
     * @return Position of the Fragment
     */
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    /**
     * Get the amount of fragments in a list
     * @return Amount of fragments
     */
    public int getCount() {
        return FragmentListTitles.size();
    }

    /**
     * Getter for a fragment page title
     * @param position Position of fragment page title in a list
     * @return Fragment title at the input position
     */
    public CharSequence getPageTitle(int position) {
        return FragmentListTitles.get(position);
    }

    /**
     * Add a Fragment to the Page
     * @param fragment Fragment object
     * @param title Title of the fragment
     */
    public void AddFragment(Fragment fragment, String title)
    {
        fragmentList.add(fragment);
        FragmentListTitles.add(title);
    }
}
