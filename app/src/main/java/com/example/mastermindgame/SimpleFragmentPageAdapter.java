package com.example.mastermindgame;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SimpleFragmentPageAdapter extends FragmentPagerAdapter {

    public SimpleFragmentPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0)
        {
            return new EasyFragment();
        }
        else if(position == 1)
        {
            return new MediumFragment();
        }
        else
        {
            return new HardFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
