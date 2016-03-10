package com.epitrack.guardioes.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.Notice;
import com.epitrack.guardioes.utility.DialogBuilder;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Igor Morais
 */
public class NoticeActivity extends AppCompatActivity implements NoticeListener {

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout toolbarLayout;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.text_view_title)
    TextView textViewTitle;

    @Bind(R.id.text_view_source)
    TextView textViewSource;

    @Bind(R.id.text_view_date)
    TextView textViewDate;

    @Bind(R.id.image_view_image)
    ImageView imageView;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private Tracker tracker;

    public static List<Notice> noticeList;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.notice);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupHeader(new Notice());

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(new NoticeAdapter(this, noticeList));

        getSupportActionBar().setTitle(R.string.notice);

    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Notice Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.notice, menu);

        return true;
    }

    public void onPrivacy(final MenuItem item) {

    }

    private void setupHeader(final Notice notice) {

        // TODO: Stub only
        imageView.setImageResource(R.drawable.img_news);
    }

    @Override
    public void onNoticeSelect(final Notice notice) {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Show Notice")
                .build());

        new DialogBuilder(NoticeActivity.this).load()
                .title(R.string.attention)
                .content(R.string.open_link)
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .callback(new MaterialDialog.ButtonCallback() {

                    @Override
                    public void onNegative(final MaterialDialog dialog) {

                    }

                    @Override
                    public void onPositive(final MaterialDialog dialog) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(notice.getLink())));
                    }
                }).show();
    }

    public Tracker getTracker() {

        if (tracker == null) {
            tracker = GoogleAnalytics.getInstance(this).newTracker(R.xml.analytics);
        }

        return tracker;
    }
}
