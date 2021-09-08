package com.example.assignment_1_study_app.ui.timetable;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TimetablePagerAdapter extends FragmentPagerAdapter {

    CharSequence[] days = { "M", "T", "W", "T", "F", "S", "S" };

    public TimetablePagerAdapter (FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new TimetableTabFragment();
        Bundle args = new Bundle();
        args.putInt("day", i);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 7;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return days[position];
    }
}
