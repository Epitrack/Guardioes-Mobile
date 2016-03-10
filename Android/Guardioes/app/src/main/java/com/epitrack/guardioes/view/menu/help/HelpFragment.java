package com.epitrack.guardioes.view.menu.help;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.utility.DialogBuilder;
import com.epitrack.guardioes.utility.NetworkUtility;
import com.epitrack.guardioes.view.IMenu;
import com.epitrack.guardioes.view.MenuListener;
import com.epitrack.guardioes.view.base.BaseFragment;
import com.google.android.gms.analytics.HitBuilders;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Igor Morais
 */
public class HelpFragment extends BaseFragment implements MenuListener {

    @Bind(R.id.list_view_option)
    ListView listViewOption;

    @Bind(R.id.list_view_contact)
    ListView listViewContact;

    @Bind(R.id.list_view_report_error)
    ListView listViewReport;

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        getSupportActionBar().setTitle(R.string.help);

    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Help Screen - " + this.getClass().getSimpleName());

        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup viewGroup, final Bundle bundle) {

        final View view = inflater.inflate(R.layout.help, viewGroup, false);

        ButterKnife.bind(this, view);

        listViewOption.setAdapter(new HelpAdapter(getActivity(), this, HelpOption.values()));

        listViewOption.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (HelpOption.getBy(position + 1).getId() == HelpOption.TERM.getId()) {

                    getTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("Action")
                            .setAction("Terms Button")
                            .build());

                    navigateTo(Term.class);

                } else if (HelpOption.getBy(position + 1).getId() == HelpOption.TUTORIAL.getId()) {

                    getTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("Action")
                            .setAction("Tutorial Button")
                            .build());

                    navigateTo(TutorialActivity.class);
                }
            }
        });

        listViewContact.setAdapter(new HelpAdapter(getActivity(), this, HelpContact.values()));

        listViewContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (HelpContact.getBy(position + 2).getId() == HelpContact.TWITTER.getId()) {

                    getTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("Action")
                            .setAction("Help Contact Twitter Button")
                            .build());

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://twitter.com/minsaude"));
                    startActivity(i);

                } else if (HelpContact.getBy(position + 2).getId() == HelpContact.FACEBOOK.getId()) {

                    getTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("Action")
                            .setAction("Help Contact Facebook Button")
                            .build());

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://www.facebook.com/minsaude"));
                    startActivity(i);
                }
            }
        });

        listViewReport.setAdapter(new HelpAdapter(getActivity(), this, HelpReport.values()));

        listViewReport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (HelpReport.getBy(position + 1).getId() == HelpReport.REPORT.getId()) {

                    getTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("Action")
                            .setAction("Help Contact Report Button")
                            .build());

                    if (NetworkUtility.isOnline(getActivity().getApplication())) {

                        navigateTo(Report.class);

                    } else {

                        new DialogBuilder(getActivity()).load()
                                .title(R.string.attention)
                                .content(R.string.network_fail)
                                .positiveText(R.string.ok)
                                .show();
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onMenuSelect(IMenu menu) {

    }
}
