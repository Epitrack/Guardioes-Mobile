package com.epitrack.guardioes.view.survey;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.utility.Constants;
import com.epitrack.guardioes.utility.DateFormat;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.epitrack.guardioes.view.menu.profile.Avatar;
import com.epitrack.guardioes.view.menu.profile.UserActivity;
import com.google.android.gms.analytics.HitBuilders;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Igor Morais
 */
public class SelectParticipantActivity extends BaseAppCompatActivity implements ParentListener {

    @Bind(R.id.text_view_name)
    TextView textViewName;

    @Bind(R.id.text_view_age)
    TextView textViewAge;

    @Bind(R.id.image_view_photo)
    de.hdodenhof.circleimageview.CircleImageView imageViewAvatar;
    //ImageView imageViewAvatar;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.text_view_id)
    TextView textViewId;

    public static List<User> parentList = new ArrayList<>();
    SingleUser singleUser = SingleUser.getInstance();

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.select_participant);

        //Miquéias Lopes
        int j = DateFormat.getDateDiff(singleUser.getDob());

        textViewName.setText(singleUser.getNick());
        textViewAge.setText(j + " Anos");
        textViewId.setText(singleUser.getId());

        if (singleUser.getPicture().length() > 2) {
            Uri uri = Uri.parse(singleUser.getPicture());

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int densityDpi = (int) (metrics.density * 160f);
            int width = 0;
            int height = 0;

            if (densityDpi == DisplayMetrics.DENSITY_LOW) {
                width = 90;
                height = 90;
            } else if (densityDpi == DisplayMetrics.DENSITY_MEDIUM) {
                width = 120;
                height = 120;
            } else if (densityDpi == DisplayMetrics.DENSITY_HIGH) {
                width = 180;
                height = 180;
            } else if (densityDpi >= DisplayMetrics.DENSITY_XHIGH) {
                width = 240;
                height = 240;
            }
            imageViewAvatar.getLayoutParams().width = width;
            imageViewAvatar.getLayoutParams().height = height;
            imageViewAvatar.setImageURI(uri);

            File file = new File(singleUser.getPicture());

            if (!file.exists()) {
                imageViewAvatar.setImageURI(uri);
                Drawable drawable = imageViewAvatar.getDrawable();
                imageViewAvatar.setImageDrawable(drawable);

                if (drawable == null) {
                    setDefaultAvatar();
                }
            } else {
                imageViewAvatar.setImageURI(uri);
            }
        } else {
            if (Integer.parseInt(singleUser.getPicture()) == 0) {
                setDefaultAvatar();
            } else {
                imageViewAvatar.setImageResource(Avatar.getBy(Integer.parseInt(singleUser.getPicture())).getLarge());
            }
        }

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        loadHousehold();
    }

    private void setDefaultAvatar() {
        if (singleUser.getGender().equals("M")) {

            if (singleUser.getRace().equals("branco") || singleUser.getRace().equals("amarelo")) {
                imageViewAvatar.setImageResource(R.drawable.image_avatar_6);
            } else {
                imageViewAvatar.setImageResource(R.drawable.image_avatar_4);
            }
        } else {

            if (singleUser.getRace().equals("branco") || singleUser.getRace().equals("amarelo")) {
                imageViewAvatar.setImageResource(R.drawable.image_avatar_8);
            } else {
                imageViewAvatar.setImageResource(R.drawable.image_avatar_7);
            }
        }
    }

    private void loadHousehold() {
        recyclerView.setAdapter(new ParentAdapter(getApplicationContext(), this, parentList));
    }

    @Override
    protected void onResume() {
        super.onResume();

        getTracker().setScreenName("Select Participant Survey Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());

        loadHousehold();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadHousehold();
    }

    //@OnClick(R.id.button_add)
    public void onAdd() {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Survey Add New Member Button")
                .build());

        final Bundle bundle = new Bundle();

        bundle.putBoolean(Constants.Bundle.NEW_MEMBER, true);
        bundle.putBoolean(Constants.Bundle.SOCIAL_NEW, false);
        navigateTo(UserActivity.class, bundle);
    }

    @OnClick(R.id.image_view_photo)
    public void onUserSelect() {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Survey Select Main User Button")
                .build());

        final Bundle bundle = new Bundle();

        bundle.putBoolean(Constants.Bundle.MAIN_MEMBER, true);
        navigateTo(StateActivity.class, bundle);
    }

    @Override
    public void onParentSelect(String id) {
        if (id.equals("-1")) {
            final Bundle bundle = new Bundle();

            bundle.putBoolean(Constants.Bundle.NEW_MEMBER, true);
            bundle.putBoolean(Constants.Bundle.SOCIAL_NEW, false);
            navigateTo(UserActivity.class, bundle);
        } else {

            getTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("Survey Select Household Button")
                    .build());

            final Bundle bundle = new Bundle();

            bundle.putString("id_user", id);
            bundle.putBoolean(Constants.Bundle.NEW_MEMBER, false);
            navigateTo(StateActivity.class, bundle);
        }
    }
}
