package com.epitrack.guardioes.view;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.manager.PrefManager;
import com.epitrack.guardioes.utility.Constants;
import com.epitrack.guardioes.view.menu.HomeMenu;
import com.epitrack.guardioes.view.welcome.WelcomeActivity;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Igor Morais
 */
public class HomeActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {

    private static final Class<? extends Fragment> MAIN_FRAGMENT = HomeFragment.class;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.navigation_view)
    NavigationView navigationView;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private ActionBarDrawerToggle drawerToggle;

    private final Map<String, Fragment> fragmentMap = new HashMap<>();
    public static final String PREFS_NAME = "preferences_user_token";

    private Tracker tracker;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.home_activity);

        ButterKnife.bind(this);

        setupViews();
    }

    private void setupViews() {

        setSupportActionBar(toolbar);

        getSupportActionBar().setLogo(R.drawable.image_logo_small);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);

        drawerLayout.setDrawerListener(drawerToggle);

        navigationView.setNavigationItemSelectedListener(this);

        addFragment(MAIN_FRAGMENT,
                MAIN_FRAGMENT.getSimpleName());
    }

    @Override
    protected void onPostCreate(final Bundle bundle) {
        super.onPostCreate(bundle);

        drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        return drawerToggle.onOptionsItemSelected(menuItem) || super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else if (getCurrentFragment().getTag().equals(MAIN_FRAGMENT.getSimpleName())) {

            //super.onBackPressed();

            final Intent intent = new Intent(this, WelcomeActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

        } else {

            replaceFragment(MAIN_FRAGMENT,
                    MAIN_FRAGMENT.getSimpleName());

        }
    }

    //Miqueias - SAIR
    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {

        drawerLayout.closeDrawer(GravityCompat.START);

        final HomeMenu homeMenu = HomeMenu.getBy(menuItem);

        if (homeMenu.getId() == HomeMenu.EXIT.getId()) {

            if (new PrefManager(this).remove(Constants.Pref.USER)) {

                final Intent intent = new Intent(this, WelcomeActivity.class);

                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("preferences_user_token", "");
                editor.commit();

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            }

        } else if (homeMenu.isFragment()) {

            if (!homeMenu.getTag().equals(getCurrentFragment().getTag())) {

                replaceFragment(homeMenu.getMenuClass(), homeMenu.getTag());
            }

        } else if (homeMenu.isActivity()) {
            startActivity(new Intent(this, homeMenu.getMenuClass()));
        }

        menuItem.setChecked(true);

        return true;
    }

    private Fragment getCurrentFragment() {
        return getFragmentManager().findFragmentById(R.id.frame_layout);
    }

    private void addFragment(final Class<? extends Fragment> fragmentClass, final String tag) {

        final Fragment fragment = Fragment.instantiate(this, fragmentClass.getName());

        getFragmentManager().beginTransaction()
                            .add(R.id.frame_layout, fragment, tag)
                            .commit();

        fragmentMap.put(tag, fragment);
    }

    private void replaceFragment(final Class<?> fragmentClass, final String tag) {

        Fragment fragment = fragmentMap.get(tag);

        if (fragment == null) {

            fragment = Fragment.instantiate(this, fragmentClass.getName());

            fragmentMap.put(tag, fragment);
        }

        getFragmentManager().beginTransaction()
                            .replace(R.id.frame_layout, fragment, tag)
                            .commit();
    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Home Screen - " + this.getClass().getSimpleName());

        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    public Tracker getTracker() {

        if (tracker == null) {
            tracker = GoogleAnalytics.getInstance(this).newTracker(R.xml.analytics);
        }

        return tracker;
    }
}
