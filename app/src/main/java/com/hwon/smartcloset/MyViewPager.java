package com.hwon.smartcloset;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by hwkim_000 on 2016-02-02.
 */
public class MyViewPager extends FragmentPagerAdapter {
    Fragment[] fragments = new Fragment[4];

    public MyViewPager(FragmentManager fm){
        super(fm);
        fragments[0]  = new HomeFragment();
        fragments[1] = new SearchFragment();
        fragments[2] = new ShopFragment();
        fragments[3] = new ClosetFragment();

    }

    public Fragment getItem(int arg){
        return fragments[arg];
    }

    public int getCount(){
        return fragments.length;
    }
}
