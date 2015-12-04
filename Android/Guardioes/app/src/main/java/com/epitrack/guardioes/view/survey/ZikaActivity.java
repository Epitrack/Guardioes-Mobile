package com.epitrack.guardioes.view.survey;

import android.content.Intent;
import android.os.Bundle;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.utility.Constants;
import com.epitrack.guardioes.view.HomeActivity;
import com.epitrack.guardioes.view.base.BaseActivity;
import com.epitrack.guardioes.view.tip.Tip;

import butterknife.OnClick;

/**
 * @author Miquéias Lopes
 */
public class ZikaActivity extends BaseActivity {

    private static final float MARGIN_TOP = 25;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.zika);
    }

    @Override
    public void onBackPressed() {
        navigateTo(HomeActivity.class);
    }

    @OnClick(R.id.button_confirm)
    public void onConfirm() {
        navigateTo();
    }

    @OnClick(R.id.upa_zika)
    public void goToUpasScreen() {

        final Tip tip = Tip.getBy(1);
        final Intent intent = new Intent(this, tip.getMenuClass());
        intent.putExtra(Constants.Bundle.TIP, tip.getId());
        startActivity(intent);
    }

    private void navigateTo() {

        navigateTo(HomeActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
    }
}
