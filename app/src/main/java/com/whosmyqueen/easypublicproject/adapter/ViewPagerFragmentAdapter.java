package com.whosmyqueen.easypublicproject.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by 郑志辉 on 2016/6/19.
 */
public class ViewPagerFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mList;

    public void setList(List<Fragment> list) {
        mList = list;
    }

    public ViewPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "问题广场";
                break;
            case 1:
                title = "灌水区";
                break;
        }
        return title;
    }
}
