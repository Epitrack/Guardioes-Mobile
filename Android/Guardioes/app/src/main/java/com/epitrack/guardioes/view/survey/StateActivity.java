package com.epitrack.guardioes.view.survey;

import android.os.Bundle;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.Method;
import com.epitrack.guardioes.request.Requester;
import com.epitrack.guardioes.request.SimpleRequester;
import com.epitrack.guardioes.utility.Constants;
import com.epitrack.guardioes.utility.DialogBuilder;
import com.epitrack.guardioes.utility.LocationUtility;
import com.epitrack.guardioes.utility.SocialShare;
import com.epitrack.guardioes.view.HomeActivity;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.google.android.gms.analytics.HitBuilders;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import butterknife.OnClick;

/**
 * @author Igor Morais
 */
public class StateActivity extends BaseAppCompatActivity {

    String id;
    SingleUser singleUser = SingleUser.getInstance();

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        final boolean mainMember = getIntent().getBooleanExtra(Constants.Bundle.MAIN_MEMBER, false);

        if (mainMember) {

            id = singleUser.getId();
        } else {
            id = getIntent().getStringExtra("id_user");
        }

        setContentView(R.layout.state);
    }

    @OnClick(R.id.text_view_state_good)
    public void onStateGood() {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Survey State Good Button")
                .build());

        JSONObject jsonObject = new JSONObject();

        User user = new User();
        LocationUtility locationUtility = new LocationUtility(getApplicationContext());

        user.setId(id);
        user.setLat(locationUtility.getLatitude());
        user.setLon(locationUtility.getLongitude());

        try {
            jsonObject.put("user_id", singleUser.getId());

            if (user.getId() != singleUser.getId()) {
                jsonObject.put("household_id", user.getId());
            }
            jsonObject.put("lat", user.getLat());
            jsonObject.put("lon", user.getLon());
            jsonObject.put("app_token", user.getApp_token());
            jsonObject.put("platform", user.getPlatform());
            jsonObject.put("client", user.getClient());
            jsonObject.put("no_symptom", "Y");
            jsonObject.put("token", singleUser.getUser_token());

            SimpleRequester sendPostRequest = new SimpleRequester();
            sendPostRequest.setUrl(Requester.API_URL + "survey/create");
            sendPostRequest.setJsonObject(jsonObject);
            sendPostRequest.setMethod(Method.POST);

            String jsonStr = sendPostRequest.execute(sendPostRequest).get();

            JSONObject jsonObjectSurvey = new JSONObject(jsonStr);

            if (jsonObjectSurvey.get("error").toString().equals("true")) {
                Toast.makeText(getApplicationContext(), jsonObjectSurvey.get("message").toString(), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        navigateTo(ShareActivity.class);
    }

    @OnClick(R.id.text_view_state_bad)
    public void onStateBad() {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Survey State Bad Button")
                .build());

        final Bundle bundle = new Bundle();

        bundle.putString("id_user", id);
        navigateTo(SymptomActivity.class, bundle);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (SocialShare.getInstance().isShared()) {
            new DialogBuilder(StateActivity.this).load()
                    .title(R.string.app_name)
                    .content(R.string.share_ok)
                    .positiveText(R.string.ok)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(final MaterialDialog dialog) {
                            SocialShare.getInstance().setIsShared(false);
                            navigateTo(HomeActivity.class);
                        }
                    }).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Select State Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }
}
