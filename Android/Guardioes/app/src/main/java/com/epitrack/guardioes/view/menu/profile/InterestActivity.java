package com.epitrack.guardioes.view.menu.profile;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.IMenu;
import com.epitrack.guardioes.view.MenuListener;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.google.android.gms.analytics.HitBuilders;

import butterknife.Bind;

/**
 * @author Igor Morais
 */
public class InterestActivity extends BaseAppCompatActivity implements MenuListener {

    @Bind(R.id.list_view_category)
    ListView listViewCategory;

    @Bind(R.id.list_view_tag)
    ListView listViewTag;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.interest);
        InterestTag.getBy(1).setIdApi("5629090b9ad47c0d2fb0196d");
        InterestTag.getBy(2).setIdApi("5629091d9ad47c0d2fb0196e");
        InterestTag.getBy(3).setIdApi("5629092f9ad47c0d2fb0196f");
        listViewTag.setAdapter(new InterestAdapter(this, this, InterestTag.values()));
    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Interest Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();

        } else {
            super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onMenuSelect(final IMenu menu) {
        Toast.makeText(getApplicationContext(), menu.getId(), Toast.LENGTH_SHORT);
    }
}
