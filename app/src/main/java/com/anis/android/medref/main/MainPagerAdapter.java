package com.anis.android.medref.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.anis.android.medref.main.fragments.quickref.QuickRefFragment;
import com.anis.android.medref.main.fragments.subjects.SubjectsFragment;
public class MainPagerAdapter extends FragmentStateAdapter {

    private final Fragment[] fragments;

    public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        fragments = new Fragment[] {
                new QuickRefFragment(),
                new SubjectsFragment()
        };
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }

    public Fragment getFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return fragments.length;
    }
}
