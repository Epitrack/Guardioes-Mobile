package com.epitrack.guardioes.view.welcome;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.epitrack.guardioes.view.welcome.WelcomePageFragment;

public class WelcomePagerAdapter extends FragmentPagerAdapter {

    private final Context context;

    public WelcomePagerAdapter(final FragmentManager fragmentManager, final Context context) {
        super(fragmentManager);

        this.context = context;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Fragment getItem(final int position) {
        return Fragment.instantiate(context, WelcomePageFragment.class.getName());
    }
}