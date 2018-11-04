package com.example.shikooo.wallpaper.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.shikooo.wallpaper.Fragment.CategoryFragment;
import com.example.shikooo.wallpaper.Fragment.TrendingFragment;
import com.example.shikooo.wallpaper.Fragment.RecentsFragment;

/**
 * Created by shikooo on 8/8/2018.
 */

public class MyFragmentAdapter extends FragmentPagerAdapter {

    private Context context;

    public MyFragmentAdapter(FragmentManager fm , Context context) {
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
        {
            return CategoryFragment.getINSTANCE();
        }
        else if (position == 1)
        {
            return TrendingFragment.getINSTANCE();
        }
        else if (position == 2)
        {
            return RecentsFragment.getINSTANCE(context);
        }
        else return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position)
        {
            case 0:
                return "Category";

            case 1:
                return "Trending";

            case 2:
                return "Recents";
        }
        return "";
    }
}
