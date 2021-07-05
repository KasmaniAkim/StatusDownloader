package com.HashTagApps.WATool.adapter;



import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class StatusPageAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments = new ArrayList<>();

    public StatusPageAdapter(FragmentManager fm) {
        super(fm);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0){
            return "Images";
        }else if (position == 1){
            return "Videos";
        } else {
            return "Saved";
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public void addFragment(Fragment fragment) {
        fragments.add(fragment);
    }

}
