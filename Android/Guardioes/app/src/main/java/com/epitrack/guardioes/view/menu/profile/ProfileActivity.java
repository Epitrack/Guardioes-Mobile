package com.epitrack.guardioes.view.menu.profile;

import android.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
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
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.google.android.gms.analytics.HitBuilders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Igor Morais
 */
public class ProfileActivity extends BaseAppCompatActivity implements UserListener {

    @Bind(R.id.list_view)
    ListView listView;

    SingleUser singleUser = SingleUser.getInstance();
    private final Map<String, Fragment> fragmentMap = new HashMap<>();

    public static ArrayList<User> userArrayList;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.profile_activity);

        if (userArrayList == null) {
            userArrayList = loadProfiles();
        }

        listView.setAdapter(new UserAdapter(this, userArrayList, this));

    }

    private void back() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getTracker().setScreenName("List Profile Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());

        if (userArrayList == null) {
            userArrayList = loadProfiles();
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            //navigateTo(ProfileFragment.class);
        } else {
            super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    @OnClick(R.id.button_add)
    public void onAdd() {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Add New Member Button")
                .build());

        final Bundle bundle = new Bundle();

        bundle.putBoolean(Constants.Bundle.NEW_MEMBER, true);
        bundle.putBoolean(Constants.Bundle.SOCIAL_NEW, false);
        navigateTo(UserActivity.class, bundle);
    }

    @Override
    public void onEdit(final User user) {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Edit Profile Button")
                .build());

        final Bundle bundle = new Bundle();

        bundle.putBoolean(Constants.Bundle.SOCIAL_NEW, false);
        bundle.putBoolean(Constants.Bundle.NEW_MEMBER, false);
        bundle.putString("nick", user.getNick());
        bundle.putString("dob", user.getDob());
        bundle.putString("gender", user.getGender());
        bundle.putString("race", user.getRace());
        bundle.putString("email", user.getEmail());
        bundle.putString("password", user.getPassword());
        bundle.putString("id", user.getId());
        bundle.putString("picture", user.getPicture());
        bundle.putString("relationship", user.getRelationship());

        if (user.getPicture().length() > 2) {
            bundle.putString("picture", user.getPicture());
        } else if (Integer.parseInt(user.getPicture()) == 0) {
            if (user.getGender().equals("M")) {
                if (user.getRace().equals("branco") || user.getRace().equals("amarelo")) {
                    bundle.putString("picture", "4");
                } else {
                    bundle.putString("picture", "3");
                }
            } else {
                if (user.getRace().equals("branco") || singleUser.getRace().equals("amarelo")) {
                    bundle.putString("picture", "8");
                } else {
                    bundle.putString("picture", "7");
                }
            }
        } else {
            bundle.putString("picture", user.getPicture());
        }

        // TODO: Check if is main member..
        if (singleUser.getId() == user.getId()) {
            bundle.putBoolean(Constants.Bundle.MAIN_MEMBER, true);
        }
        navigateTo(UserActivity.class, bundle);
    }

    @Override
    public void onDelete(final User user) {
        //Miquéias Lopes

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Delete Member Button")
                .build());

        if (singleUser.getId() == user.getId()) {

            new DialogBuilder(ProfileActivity.this).load()
                    .title(R.string.attention)
                    .content(R.string.not_remove_member)
                    .positiveText(R.string.ok)
                    .show();
        } else {

            new DialogBuilder(ProfileActivity.this).load()
                    .title(R.string.attention)
                    .content(R.string.delete_profile)
                    .positiveText(R.string.yes)
                    .negativeText(R.string.no)
                    .callback(new MaterialDialog.ButtonCallback() {

                        @Override
                        public void onNegative(final MaterialDialog dialog) {

                        }

                        @Override
                        public void onPositive(final MaterialDialog dialog) {

                            SimpleRequester simpleRequester = new SimpleRequester();
                            simpleRequester.setMethod(Method.GET);
                            simpleRequester.setUrl(Requester.API_URL + "household/delete/" + user.getId() + "?client=api");
                            simpleRequester.setJsonObject(null);

                            try {
                                String jsonStr = simpleRequester.execute(simpleRequester).get();

                                JSONObject jsonObject = new JSONObject(jsonStr);

                                if (jsonObject.get("error").toString() == "true") {
                                    refresh(true);
                                } else {
                                    refresh(false);
                                }

                            } catch (InterruptedException e) {
                                Toast.makeText(getApplicationContext(), R.string.generic_error + " - " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (ExecutionException e) {
                                Toast.makeText(getApplicationContext(), R.string.generic_error + " - " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), R.string.generic_error + " - " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }).show();
        }
    }

    private void refresh(boolean error) {
        if (error) {
            Toast.makeText(getApplicationContext(), R.string.generic_error, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), R.string.delete_user, Toast.LENGTH_SHORT).show();
            listView.setAdapter(new UserAdapter(this, new ArrayList<User>(), this));
        }
    }

    private ArrayList<User> loadProfiles() {
        ArrayList<User> userList = new ArrayList<User>();

        SingleUser singleUser = SingleUser.getInstance();

        userList.add(new User(R.drawable.image_avatar_small_2, singleUser.getNick(), singleUser.getEmail(), singleUser.getId(),
                singleUser.getDob(), singleUser.getRace(), singleUser.getGender(), singleUser.getPicture()));

        SimpleRequester simpleRequester = new SimpleRequester();
        simpleRequester.setUrl(Requester.API_URL + "user/household/" + singleUser.getId());
        simpleRequester.setJsonObject(null);
        simpleRequester.setMethod(Method.GET);

        try {
            String jsonStr = simpleRequester.execute(simpleRequester).get();

            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.get("error").toString() == "false") {

                JSONArray jsonArray = jsonObject.getJSONArray("data");

                if (jsonArray.length() > 0) {

                    JSONObject jsonObjectHousehold;

                    for (int i = 0; i < jsonArray.length(); i++) {

                        jsonObjectHousehold = jsonArray.getJSONObject(i);

                        User user = new User(R.drawable.image_avatar_small_8, jsonObjectHousehold.get("nick").toString(),
                                "", jsonObjectHousehold.get("id").toString(),
                                jsonObjectHousehold.get("dob").toString(), jsonObjectHousehold.get("race").toString(),
                                jsonObjectHousehold.get("gender").toString(), jsonObjectHousehold.get("picture").toString());
                        try {
                            user.setRelationship(jsonObjectHousehold.get("relationship").toString());
                        } catch (Exception e) {
                            user.setRelationship("");
                        }

                        try {
                            user.setEmail(jsonObjectHousehold.get("email").toString());
                        } catch (Exception e) {
                            user.setEmail("");
                        }

                        userList.add(user);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userList;
    }
}
