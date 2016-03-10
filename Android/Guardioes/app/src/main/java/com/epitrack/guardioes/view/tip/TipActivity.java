package com.epitrack.guardioes.view.tip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.utility.Constants;
import com.epitrack.guardioes.view.IMenu;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.google.android.gms.analytics.HitBuilders;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * @author Igor Morais
 */
public class TipActivity extends BaseAppCompatActivity implements ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener {

    @Bind(R.id.expandable_list_view)
    ExpandableListView expandableListView;

    @Override
    protected void onCreate(final android.os.Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.tip);

        final Map<Integer, IMenu[]> menuMap = new HashMap<>();

        menuMap.put(Tip.TELEPHONE.getId(), Phone.values());

        expandableListView.setAdapter(new TipAdapter(Tip.values(), menuMap));

        expandableListView.setOnChildClickListener(this);
        expandableListView.setOnGroupClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Health Tips Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onChildClick(final ExpandableListView listView, final View view, final int groupPosition, final int childPosition, final long id) {

        final int phone = Phone.getBy(id).getId();
        final Bundle bundle = new Bundle();

        bundle.putInt("phone_id", phone);
        navigateTo(UsefulPhonesActivity.class, bundle);

        return false;
    }

    @Override
    public boolean onGroupClick(final ExpandableListView listView, final View view, final int groupPosition, final long id) {

        final Tip tip = Tip.getBy(id);

        if (tip.isActivity()) {

            final Intent intent = new Intent(this, tip.getMenuClass());

            if (tip == Tip.HOSPITAL || tip == Tip.PHARMACY) {

                getTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("Action")
                        .setAction(tip.getName() + " Button")
                        .build());

                intent.putExtra(Constants.Bundle.TIP, tip.getId());
            }

            startActivity(intent);

            return true;
        }

        return false;
    }
}
