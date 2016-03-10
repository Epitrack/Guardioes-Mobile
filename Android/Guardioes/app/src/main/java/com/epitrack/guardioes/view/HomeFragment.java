package com.epitrack.guardioes.view;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.Notice;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.Method;
import com.epitrack.guardioes.request.Requester;
import com.epitrack.guardioes.request.SimpleRequester;
import com.epitrack.guardioes.utility.DialogBuilder;
import com.epitrack.guardioes.utility.NetworkUtility;
import com.epitrack.guardioes.view.base.BaseFragment;
import com.epitrack.guardioes.view.diary.DiaryActivity;
import com.epitrack.guardioes.view.menu.profile.Avatar;
import com.epitrack.guardioes.view.menu.profile.ProfileActivity;
import com.epitrack.guardioes.view.survey.SelectParticipantActivity;
import com.epitrack.guardioes.view.tip.TipActivity;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.analytics.HitBuilders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Igor Morais
 */
public class HomeFragment extends BaseFragment {

    @Bind(R.id.text_view_name)
    TextView textViewName;

    @Bind(R.id.image_view_photo)
    CircularImageView imageViewPhoto;

    SingleUser singleUser = SingleUser.getInstance();

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setDisplayTitle(false);
        setDisplayLogo(true);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup viewGroup, Bundle bundle) {

        final View view = inflater.inflate(R.layout.home_fragment, viewGroup, false);

        ButterKnife.bind(this, view);

        String text = getString(R.string.message_hello);
        text = text.replace("{0}", singleUser.getNick());
        textViewName.setText(text);

        return view;
    }

    private void loadImageProfile() {

        String picture = singleUser.getPicture();

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
            imageViewPhoto.getLayoutParams().width = width;
            imageViewPhoto.getLayoutParams().height = height;

            File file = new File(singleUser.getPicture());

            if (!file.exists()) {
                imageViewPhoto.setImageURI(uri);
                Drawable drawable = imageViewPhoto.getDrawable();
                imageViewPhoto.setImageDrawable(drawable);

                if (drawable == null) {
                    setDefaultAvatar();
                }
            } else {
                imageViewPhoto.setImageURI(uri);

            }

        } else {

            if (singleUser.getPicture().equals("")) {
                singleUser.setPicture("0");
            }

            if (Integer.parseInt(singleUser.getPicture()) == 0) {
                setDefaultAvatar();
            } else {
                imageViewPhoto.setImageResource(Avatar.getBy(Integer.parseInt(singleUser.getPicture())).getLarge());
            }
        }
    }

    private void setDefaultAvatar() {
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

        imageViewPhoto.getLayoutParams().width = width;
        imageViewPhoto.getLayoutParams().height = height;

        if (singleUser.getGender().equals("M")) {

            if (singleUser.getRace().equals("branco") || singleUser.getRace().equals("amarelo")) {
                imageViewPhoto.setImageResource(R.drawable.image_avatar_6);
            } else {
                imageViewPhoto.setImageResource(R.drawable.image_avatar_4);
            }
        } else {

            if (singleUser.getRace().equals("branco") || singleUser.getRace().equals("amarelo")) {
                imageViewPhoto.setImageResource(R.drawable.image_avatar_8);
            } else {
                imageViewPhoto.setImageResource(R.drawable.image_avatar_7);
            }
        }
    }

    public void onResume() {
        super.onResume();
        //mTracker.setScreenName("Home Screen - " + this.getClass().getSimpleName());
        //mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        loadImageProfile();
    }

    @OnClick(R.id.image_view_photo)
    public void showProfile() {

        if (NetworkUtility.isOnline(getActivity().getApplication())) {

            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(getActivity(), R.style.Theme_MyProgressDialog);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.rgb(30, 136, 229)));
            progressDialog.setTitle(R.string.app_name);
            progressDialog.setMessage("Carregando...");
            progressDialog.show();

            new Thread() {

                @Override
                public void run() {
                    ProfileActivity.userArrayList = loadProfiles();
                    progressDialog.dismiss();
                    navigateTo(ProfileActivity.class);
                }

            }.start();

        } else {

            new DialogBuilder(getActivity()).load()
                    .title(R.string.attention)
                    .content(R.string.network_fail)
                    .positiveText(R.string.ok)
                    .show();
        }
    }

    @OnClick(R.id.text_view_notice)
    public void onNews() {
        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Notice Button")
                .build());

        if (NetworkUtility.isOnline(getActivity().getApplication())) {

            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(getActivity(), R.style.Theme_MyProgressDialog);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.rgb(30, 136, 229)));
            progressDialog.setTitle(R.string.app_name);
            progressDialog.setMessage("Carregando...");
            progressDialog.show();

            new Thread() {

                @Override
                public void run() {
                    NoticeActivity.noticeList = getNoticeList();
                    progressDialog.dismiss();
                    navigateTo(NoticeActivity.class);
                }

            }.start();

        } else {

            new DialogBuilder(getActivity()).load()
                    .title(R.string.attention)
                    .content(R.string.network_fail)
                    .positiveText(R.string.ok)
                    .show();
        }
    }

    @OnClick(R.id.text_view_map)
    public void onMap() {
        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Map Button")
                .build());

        navigateTo(MapSymptomActivity.class);
    }

    @OnClick(R.id.text_view_tip)
    public void onTip() {
        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Tip Button")
                .build());

        navigateTo(TipActivity.class);
    }

    @OnClick(R.id.text_view_diary)
    public void onDiary() {
        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Diary of Health Button")
                .build());

        if (NetworkUtility.isOnline(getActivity().getApplication())) {

            navigateTo(DiaryActivity.class);

        } else {

            new DialogBuilder(getActivity()).load()
                    .title(R.string.attention)
                    .content(R.string.network_fail)
                    .positiveText(R.string.ok)
                    .show();
        }

    }

    @OnClick(R.id.text_view_join)
    public void onJoin() {
        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Survey Button")
                .build());

        if (NetworkUtility.isOnline(getActivity().getApplication())) {

            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(getActivity(), R.style.Theme_MyProgressDialog);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.rgb(30, 136, 229)));
            progressDialog.setTitle(R.string.app_name);
            progressDialog.setMessage("Carregando...");
            progressDialog.show();

            new Thread() {

                @Override
                public void run() {
                    SelectParticipantActivity.parentList = loadHaousehold();
                    progressDialog.dismiss();
                    navigateTo(SelectParticipantActivity.class);
                }

            }.start();

        } else {

            new DialogBuilder(getActivity()).load()
                    .title(R.string.attention)
                    .content(R.string.network_fail)
                    .positiveText(R.string.ok)
                    .show();
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

    private List<Notice> getNoticeList() {

        List<Notice> noticeList = new ArrayList<>();

        SimpleRequester simpleRequester = new SimpleRequester();
        simpleRequester.setMethod(Method.GET);
        simpleRequester.setUrl(Requester.API_URL + "news/get");

        try {
            String jsonStr = simpleRequester.execute(simpleRequester).get();
            JSONArray jsonArray = new JSONObject(jsonStr).getJSONObject("data").getJSONArray("statuses");

            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    Notice notice = new Notice();

                    notice.setTitle(jsonObject.get("text").toString());
                    notice.setSource("via @minsaude");

                    notice.setDrawable(R.drawable.stub1);
                    notice.setLink("https://twitter.com/minsaude/status/" + jsonObject.get("id_str").toString());

                    noticeList.add(notice);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return noticeList;
    }

    private List<User> loadHaousehold() {

        List<User> parentList = new ArrayList<>();

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
                        parentList.add(new User(R.drawable.image_avatar_small_8, jsonObjectHousehold.get("nick").toString(),
                                /*jsonObjectHousehold.get("email").toString()*/"", jsonObjectHousehold.get("id").toString(),
                                jsonObjectHousehold.get("dob").toString(), jsonObjectHousehold.get("race").toString(),
                                jsonObjectHousehold.get("gender").toString(), jsonObjectHousehold.get("picture").toString()));
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
        parentList.add(new User(R.drawable.img_add_profile, "    Adicionar\nnovo membro", "", "-1", "", "", "", ""));
        return parentList;
    }

}