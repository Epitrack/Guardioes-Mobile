package com.epitrack.guardioes.view.menu.help;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.epitrack.guardioes.view.welcome.Welcome;
import com.epitrack.guardioes.view.welcome.WelcomePagerAdapter;
import com.google.android.gms.analytics.HitBuilders;
import com.viewpagerindicator.CirclePageIndicator;

import butterknife.Bind;

/**
 * @author Miquéias Lopes
 */
public class TutorialActivity extends BaseAppCompatActivity {

    @Bind(R.id.page_indicator)
    CirclePageIndicator pageIndicator;

    @Bind(R.id.view_pager)
    ViewPager viewPager;

    @Bind(R.id.button_login)
    Button buttonLogin;

    @Bind(R.id.button_create_account)
    Button buttonCreateAccount;

    @Override
    protected void onCreate(final Bundle bundle){
        super.onCreate(bundle);

        setContentView(R.layout.welcome);

        viewPager.setAdapter(new WelcomePagerAdapter(getSupportFragmentManager(), this, Welcome.values()));

        pageIndicator.setViewPager(viewPager);

        buttonLogin.setVisibility(View.INVISIBLE);
        buttonCreateAccount.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Tutorial Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        onBackPressed();

        return true;
    }

}
