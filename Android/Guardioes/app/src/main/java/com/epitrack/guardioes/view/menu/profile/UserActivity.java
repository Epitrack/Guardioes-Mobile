package com.epitrack.guardioes.view.menu.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.ProfileImage;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.Method;
import com.epitrack.guardioes.request.Requester;
import com.epitrack.guardioes.request.SimpleRequester;
import com.epitrack.guardioes.utility.BitmapUtility;
import com.epitrack.guardioes.utility.Constants;
import com.epitrack.guardioes.utility.DateFormat;
import com.epitrack.guardioes.utility.DialogBuilder;
import com.epitrack.guardioes.utility.LocationUtility;
import com.epitrack.guardioes.utility.Mask;
import com.epitrack.guardioes.view.HomeActivity;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.epitrack.guardioes.view.welcome.WelcomeActivity;
import com.google.android.gms.analytics.HitBuilders;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Igor Morais
 */
public class UserActivity extends BaseAppCompatActivity {

    @Bind(R.id.text_view_message)
    TextView textViewMessage;

    @Bind(R.id.edit_text_nickname)
    EditText editTextNickname;

    @Bind(R.id.image_view_image)
    de.hdodenhof.circleimageview.CircleImageView imageViewImage;
    //ImageView imageViewImage;

    @Bind(R.id.spinner_gender)
    Spinner spinnerGender;

    @Bind(R.id.spinner_race)
    Spinner spinnerRace;

    @Bind(R.id.edit_text_birth_date)
    EditText editTextBirthDate;

    @Bind(R.id.text_layout_mail)
    TextInputLayout textLayoutMail;

    @Bind(R.id.edit_text_mail)
    EditText editTextMail;

    @Bind(R.id.linear_layout_password)
    LinearLayout linearLayoutPassword;

    @Bind(R.id.edit_text_password)
    EditText editTextPassword;

    @Bind(R.id.edit_text_confirm_password)
    EditText editTextConfirmPassword;

    @Bind(R.id.spinner_relationship)
    Spinner spinnerRelationship;

    @Bind(R.id.label_relationship)
    TextView textViewRelationship;

    boolean socialNew;
    boolean newMenber;
    boolean mainMember;
    SingleUser singleUser = SingleUser.getInstance();
    private int userAvatar = 0;
    String photoPath = "";
    private ProfileImage profileImage = ProfileImage.getInstance();

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        profileImage = ProfileImage.getInstance();

        socialNew = getIntent().getBooleanExtra(Constants.Bundle.SOCIAL_NEW, false);
        newMenber = getIntent().getBooleanExtra(Constants.Bundle.NEW_MEMBER, false);
        mainMember = getIntent().getBooleanExtra(Constants.Bundle.MAIN_MEMBER, false);

        setContentView(R.layout.user);

        editTextBirthDate.addTextChangedListener(Mask.insert("##/##/####", editTextBirthDate));

        if (mainMember || socialNew) {
            textViewRelationship.setVisibility(View.INVISIBLE);
            spinnerRelationship.setVisibility(View.INVISIBLE);
        }

        if (newMenber || socialNew) {
            textLayoutMail.setVisibility(View.VISIBLE);
            editTextMail.setEnabled(true);
            editTextMail.setVisibility(View.VISIBLE);
        }

        if (socialNew) {
            imageViewImage.setVisibility(View.INVISIBLE);
            new DialogBuilder(UserActivity.this).load()
                    .title(R.string.attention)
                    .content(R.string.new_user_social_media)
                    .positiveText(R.string.ok)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(final MaterialDialog dialog) {
                            loadUser();
                        }
                    }).show();
        } else {
            imageViewImage.setVisibility(View.VISIBLE);
            loadUser();
        }
    }

    public void onResume() {
        super.onResume();

        getTracker().setScreenName("User Form Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());

        loadUser();
    }

    private void loadUser() {
        String nick;
        String dob;
        String gender;
        String race;
        String email;
        String picture;
        String relationship;

        if (socialNew) {
            nick = singleUser.getNick();
            dob = singleUser.getDob();
            gender = singleUser.getGender();
            race = singleUser.getRace();
            email = singleUser.getEmail();
            picture = singleUser.getPicture();
            relationship = null;
        } else {
            nick = getIntent().getStringExtra("nick");
            dob = getIntent().getStringExtra("dob");
            gender = getIntent().getStringExtra("gender");
            race = getIntent().getStringExtra("race");
            email = getIntent().getStringExtra("email");
            picture = getIntent().getStringExtra("picture");
            relationship = getIntent().getStringExtra("relationship");
        }

        if (!newMenber || socialNew) {
            try {
                    editTextNickname.setText(nick);

                    if (dob != null) {
                        String dateFormat = DateFormat.getDate(dob, "dd/MM/yyyy");
                        editTextBirthDate.setText(dateFormat);
                        //Mask.insert("##/##/####", editTextBirthDate);

                    }
                    editTextMail.setText(email);

                    if (race != null) {
                        if (race.equals("branco")) {
                            spinnerRace.setSelection(0);
                        } else if (race.equals("preto")) {
                            spinnerRace.setSelection(1);
                        } else if (race.equals("pardo")) {
                            spinnerRace.setSelection(2);
                        } else if (race.equals("amarelo")) {
                            spinnerRace.setSelection(3);
                        } else if (race.equals("indígena")) {
                            spinnerRace.setSelection(4);
                        }
                    }

                    if (race == null) {
                        race = "branco";
                    }

                    if (relationship != null) {
                        switch (relationship) {
                            case "pai":
                                spinnerRelationship.setSelection(0);
                                break;
                            case "mae":
                                spinnerRelationship.setSelection(1);
                                break;
                            case "filho":
                                spinnerRelationship.setSelection(2);
                                break;
                            case "irmao":
                                spinnerRelationship.setSelection(3);
                                break;
                            case "avo":
                                spinnerRelationship.setSelection(4);
                                break;
                            case "neto":
                                spinnerRelationship.setSelection(5);
                                break;
                            case "tio":
                                spinnerRelationship.setSelection(6);
                                break;
                            case "sobrinho":
                                spinnerRelationship.setSelection(7);
                                break;
                            case "bisavo":
                                spinnerRelationship.setSelection(8);
                                break;
                            case "bisneto":
                                spinnerRelationship.setSelection(9);
                                break;
                            case "primo":
                                spinnerRelationship.setSelection(10);
                                break;
                            case "sogro":
                                spinnerRelationship.setSelection(11);
                                break;
                            case "genro":
                                spinnerRelationship.setSelection(12);
                                break;
                            case "nora":
                                spinnerRelationship.setSelection(13);
                                break;
                            case "padrasto":
                                spinnerRelationship.setSelection(14);
                                break;
                            case "madrasta":
                                spinnerRelationship.setSelection(15);
                                break;
                            case "enteado":
                                spinnerRelationship.setSelection(16);
                                break;
                            case "conjuge":
                                spinnerRelationship.setSelection(17);
                                break;
                            case "outros":
                                spinnerRelationship.setSelection(18);
                                break;
                            default:
                                spinnerRelationship.setSelection(18);
                                break;
                        }
                    }

                    if (gender != null) {
                        if (gender.equals("M")) {
                            spinnerGender.setSelection(0);
                        } else {
                            spinnerGender.setSelection(1);
                        }

                        if (profileImage.getAvatar() != null) {
                            if (!profileImage.getAvatar().equals("")) {
                                imageViewImage.setBackgroundResource(Avatar.getBy(Integer.parseInt(profileImage.getAvatar())).getSmall());
                            }
                        } else if (profileImage.getUri() != null) {
                            File file = new File(profileImage.getUri().getPath());

                            if (!file.exists()) {
                                if (gender.equals("M")) {
                                    if (race.equals("branco") || race.equals("amarelo")) {
                                        imageViewImage.setBackgroundResource(R.drawable.image_avatar_6);
                                    } else {
                                        imageViewImage.setBackgroundResource(R.drawable.image_avatar_4);
                                    }
                                } else {

                                    if (race.equals("branco") || race.equals("amarelo")) {
                                        imageViewImage.setBackgroundResource(R.drawable.image_avatar_8);
                                    } else {
                                        imageViewImage.setBackgroundResource(R.drawable.image_avatar_7);
                                    }
                                }
                            } else {
                                imageViewImage.setImageURI(profileImage.getUri());
                            }
                        } else {

                            if (singleUser.getUri() != null && (email.equals(singleUser.getEmail()))) {
                                imageViewImage.setImageURI(singleUser.getUri());
                            } else {

                                if (!picture.equals("")) {
                                    if (picture.length() > 2) {
                                        if (picture.substring(0, 4).toLowerCase().equals("http")) {
                                            if (gender.equals("M")) {
                                                if (race.equals("branco") || race.equals("amarelo")) {
                                                    imageViewImage.setBackgroundResource(R.drawable.image_avatar_6);
                                                } else {
                                                    imageViewImage.setBackgroundResource(R.drawable.image_avatar_4);
                                                }
                                            } else {

                                                if (race.equals("branco") || race.equals("amarelo")) {
                                                    imageViewImage.setBackgroundResource(R.drawable.image_avatar_8);
                                                } else {
                                                    imageViewImage.setBackgroundResource(R.drawable.image_avatar_7);
                                                }
                                            }
                                        } else {
                                            Uri uri = Uri.parse(picture);
                                            imageViewImage.setImageURI(uri);

                                            Drawable drawable = imageViewImage.getDrawable();
                                            imageViewImage.setBackground(drawable);
                                            imageViewImage.setImageResource(R.drawable.mask);

                                            if (drawable == null) {
                                                imageViewImage.setImageResource(R.drawable.mask);
                                                if (gender.equals("M")) {
                                                    if (race.equals("branco") || race.equals("amarelo")) {
                                                        imageViewImage.setBackgroundResource(R.drawable.image_avatar_6);
                                                    } else {
                                                        imageViewImage.setBackgroundResource(R.drawable.image_avatar_4);
                                                    }
                                                } else {

                                                    if (race.equals("branco") || race.equals("amarelo")) {
                                                        imageViewImage.setBackgroundResource(R.drawable.image_avatar_8);
                                                    } else {
                                                        imageViewImage.setBackgroundResource(R.drawable.image_avatar_7);
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        imageViewImage.setBackgroundResource(Avatar.getBy(Integer.parseInt(picture)).getSmall());
                                    }
                                } else if (singleUser.getImageResource() == null) {
                                    if (gender.equals("M")) {
                                        if (race.equals("branco") || race.equals("amarelo")) {
                                            imageViewImage.setBackgroundResource(R.drawable.image_avatar_6);
                                        } else {
                                            imageViewImage.setBackgroundResource(R.drawable.image_avatar_4);
                                        }
                                    } else {

                                        if (race.equals("branco") || race.equals("amarelo")) {
                                            imageViewImage.setBackgroundResource(R.drawable.image_avatar_8);
                                        } else {
                                            imageViewImage.setBackgroundResource(R.drawable.image_avatar_7);
                                        }
                                    }
                                } else {
                                    if (singleUser.getEmail().equals(email) && !singleUser.getImageResource().equals("")) {
                                        imageViewImage.setImageBitmap(BitmapUtility.scale((singleUser.getWidthImageProfile() / 2), (singleUser.getHeightImageProfile() / 2), singleUser.getImageResource()));
                                    }
                                }
                            }
                        }
                    }

                    if (mainMember) {
                        textViewMessage.setText(R.string.message_fields);

                        textLayoutMail.setVisibility(View.VISIBLE);
                        editTextMail.setVisibility(View.VISIBLE);

                        linearLayoutPassword.setVisibility(View.VISIBLE);

                        if (profileImage.getAvatar() != null) {
                            if (!profileImage.getAvatar().equals("")) {
                                imageViewImage.setBackgroundResource(Avatar.getBy(Integer.parseInt(profileImage.getAvatar())).getSmall());
                            }
                        } else if (profileImage.getUri() != null) {
                            imageViewImage.setImageURI(profileImage.getUri());
                        } else {
                            if (Integer.parseInt(singleUser.getPicture()) > 0) {
                                imageViewImage.setBackgroundResource(Avatar.getBy(Integer.parseInt(singleUser.getPicture())).getSmall());
                            }
                        }
                    } else if (socialNew) {
                        if (singleUser.getEmail() == null) {
                            textLayoutMail.setVisibility(View.VISIBLE);
                            editTextMail.setEnabled(true);
                            editTextMail.setVisibility(View.VISIBLE);
                        }
                    } else if (newMenber) {
                        textLayoutMail.setVisibility(View.VISIBLE);
                        editTextMail.setEnabled(true);
                        editTextMail.setVisibility(View.VISIBLE);
                    } else {
                        textLayoutMail.setVisibility(View.VISIBLE);
                        editTextMail.setEnabled(true);
                        editTextMail.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {

                e.printStackTrace();
            } finally {
                if (profileImage.getAvatar() != null) {
                    profileImage.setAvatar(null);
                }

                if (profileImage.getUri() != null) {
                    profileImage.setUri(null);
                }
            }
        }
    }

    @OnClick(R.id.image_view_image)
    public void onImage() {
        navigateForResult(AvatarActivity.class, Constants.RequestCode.IMAGE);
    }

    @OnClick(R.id.button_add)
    public void onAdd() {
        //Miquéias Lopes
        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Create New Member/User Button")
                .build());
            User user = new User();

        user.setNick(editTextNickname.getText().toString().trim());
        user.setDob(editTextBirthDate.getText().toString().trim().toLowerCase());
        String gender = spinnerGender.getSelectedItem().toString();
        gender = gender.substring(0, 1);
        user.setGender(gender.toUpperCase());
        user.setRace(spinnerRace.getSelectedItem().toString().toLowerCase());
        user.setEmail(editTextMail.getText().toString().toLowerCase().trim());

        String relationship = spinnerRelationship.getSelectedItem().toString().toLowerCase();
        relationship = relationship.replace("ô", "o");
        relationship = relationship.replace("ã", "a");
        relationship = relationship.replace("ã", "a");

        user.setRelationship(relationship.toLowerCase());

        if (!DateFormat.isDate(user.getDob())) {

            new DialogBuilder(UserActivity.this).load()
                    .title(R.string.attention)
                    .content(R.string.dob_invalid)
                    .positiveText(R.string.ok)
                    .show();

        } else if (DateFormat.getDateDiff(DateFormat.getDate(editTextBirthDate.getText().toString().trim())) > 120) {

            new DialogBuilder(UserActivity.this).load()
                    .title(R.string.attention)
                    .content(R.string.dob_invalid)
                    .positiveText(R.string.ok)
                    .show();

        } else if (DateFormat.getDateDiff(DateFormat.getDate(editTextBirthDate.getText().toString().trim())) < 0) {

            new DialogBuilder(UserActivity.this).load()
                    .title(R.string.attention)
                    .content(R.string.dob_invalid)
                    .positiveText(R.string.ok)
                    .show();
        } else {

            JSONObject jsonObject = new JSONObject();
            SimpleRequester simpleRequester = new SimpleRequester();

            try {
                jsonObject.put("nick", user.getNick());
                jsonObject.put("dob", DateFormat.getDate(user.getDob()));
                jsonObject.put("gender", user.getGender());
                jsonObject.put("race", user.getRace());
                jsonObject.put("client", user.getClient());
                jsonObject.put("race", user.getRace());
                jsonObject.put("relationship", user.getRelationship());
                jsonObject.put("email", user.getEmail());

                LocationUtility locationUtility;
                try {
                    locationUtility = new LocationUtility(getApplicationContext());

                    if (locationUtility.getLocation() != null) {
                        jsonObject.put("lat", locationUtility.getLatitude());
                        jsonObject.put("lon", locationUtility.getLongitude());
                    }
                } catch (Exception e) {

                }

                if (singleUser.getUri() != null && (user.getNick().equals(singleUser.getNick()))) {
                    jsonObject.put("picture", singleUser.getUri().toString());
                } else if (photoPath != "") {
                    //jsonObject.put("picture", photoPath);
                    SharedPreferences settingsImage = getSharedPreferences(Constants.Pref.PREFS_IMAGE, 0);
                    SharedPreferences.Editor editorImage = settingsImage.edit();
                    editorImage.putString(Constants.Pref.PREFS_IMAGE, photoPath);
                    editorImage.apply();
                    SharedPreferences settingsImageUserToken = getSharedPreferences(Constants.Pref.PREFS_IMAGE_USER_TOKEN, 0);
                    SharedPreferences.Editor editorImageUserToken = settingsImageUserToken.edit();
                    editorImageUserToken.putString(Constants.Pref.PREFS_IMAGE_USER_TOKEN, photoPath);
                    editorImageUserToken.apply();
                } else if (userAvatar > 0) {
                    jsonObject.put("picture", userAvatar);
                }

                if (!socialNew) {
                    if (mainMember) {
                        String password = editTextPassword.getText().toString().trim();
                        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

                        if (!password.trim().equals("") || !confirmPassword.trim().equals("")) {

                            if ((password.trim().length() < 6) || (confirmPassword.trim().length() < 6)) {

                                new DialogBuilder(UserActivity.this).load()
                                        .title(R.string.attention)
                                        .content(R.string.password_fail)
                                        .positiveText(R.string.ok)
                                        .show();
                                return;
                            }

                            if (!password.equals(confirmPassword)) {

                                new DialogBuilder(UserActivity.this).load()
                                        .title(R.string.attention)
                                        .content(R.string.password_not_equal)
                                        .positiveText(R.string.ok)
                                        .show();
                                return;
                            }

                            if (!password.equals("")) {
                                if (password.equals(confirmPassword)) {
                                    user.setPassword(password);
                                    jsonObject.put("password", user.getPassword());
                                }
                            }
                        }
                    }

                    if (newMenber) {
                        jsonObject.put("user", singleUser.getId());
                    } else if (mainMember) {
                        jsonObject.put("id", singleUser.getId());
                    } else {
                        jsonObject.put("id", getIntent().getStringExtra("id"));
                    }

                    if (newMenber) {
                        simpleRequester.setUrl(Requester.API_URL + "household/create");
                    } else if (mainMember) {
                        simpleRequester.setUrl(Requester.API_URL + "user/update");
                    } else {
                        simpleRequester.setUrl(Requester.API_URL + "household/update");
                    }
                } else {
                    jsonObject.put("password", singleUser.getPassword());
                    jsonObject.put("app_token", user.getApp_token());
                    jsonObject.put("platform", user.getPlatform());
                    jsonObject.put("gl", singleUser.getGl());
                    jsonObject.put("tw", singleUser.getTw());
                    jsonObject.put("fb", singleUser.getFb());
                    jsonObject.put("picture", "0");

                    try {
                        locationUtility = new LocationUtility(getApplicationContext());

                        if (locationUtility.getLocation() != null) {
                            jsonObject.put("lat", locationUtility.getLatitude());
                            jsonObject.put("lon", locationUtility.getLongitude());
                        }
                    } catch (Exception e) {

                    }

                    if (singleUser.getEmail() == null || singleUser.getEmail() == "") {
                        jsonObject.put("email", editTextMail.getText().toString().toLowerCase());
                        jsonObject.put("password", editTextMail.getText().toString().toLowerCase());

                    } else {
                        jsonObject.put("email", singleUser.getEmail());
                    }

                    simpleRequester.setUrl(Requester.API_URL + "user/create");
                }

                simpleRequester.setJsonObject(jsonObject);
                simpleRequester.setMethod(Method.POST);

                String jsonStr = simpleRequester.execute(simpleRequester).get();

                jsonObject = new JSONObject(jsonStr);

                if (jsonObject.get("error").toString() == "true") {
                    //Toast.makeText(getApplicationContext(), jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();

                    new DialogBuilder(UserActivity.this).load()
                            .title(R.string.attention)
                            .content(R.string.error_add_new_member)
                            .positiveText(R.string.ok)
                            .show();

                } else {
                    if (socialNew) {

                        JSONObject jsonObjectUser = jsonObject.getJSONObject("user");

                        singleUser.setNick(jsonObjectUser.getString("nick").toString());
                        singleUser.setEmail(jsonObjectUser.getString("email").toString());
                        singleUser.setGender(jsonObjectUser.getString("gender").toString());
                        singleUser.setPicture("0");
                        singleUser.setId(jsonObjectUser.getString("id").toString());
                        singleUser.setPassword(jsonObjectUser.getString("email").toString());
                        singleUser.setRace(jsonObjectUser.getString("race").toString());
                        singleUser.setDob(jsonObjectUser.getString("dob").toString());
                        try {
                            singleUser.setHashtags(jsonObjectUser.getJSONArray("hashtags"));
                        } catch (Exception e) {

                        }

                        SharedPreferences sharedPreferences = null;

                        jsonObject = new JSONObject();

                        jsonObject.put("email", singleUser.getEmail());
                        jsonObject.put("password", singleUser.getPassword());

                        simpleRequester = new SimpleRequester();
                        simpleRequester.setUrl(Requester.API_URL + "user/login");
                        simpleRequester.setJsonObject(jsonObject);
                        simpleRequester.setMethod(Method.POST);

                        jsonStr = simpleRequester.execute(simpleRequester).get();

                        jsonObject = new JSONObject(jsonStr);

                        if (jsonObject.get("error").toString() == "true") {
                            Toast.makeText(getApplicationContext(), "Erro - " + jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            singleUser.setUser_token(jsonObject.get("token").toString());

                            sharedPreferences = getSharedPreferences(Constants.Pref.PREFS_NAME, 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString(Constants.Pref.PREFS_NAME, singleUser.getUser_token());
                            editor.commit();

                            sharedPreferences = getSharedPreferences(Constants.Pref.PREFS_SOCIAL, 0);
                            SharedPreferences.Editor editorSocial = sharedPreferences.edit();

                            editorSocial.putString(Constants.Pref.PREFS_NAME, "true");
                            editor.commit();
                        }

                        new DialogBuilder(UserActivity.this).load()
                                .title(R.string.attention)
                                .content(R.string.cadastro_sucesso)
                                .positiveText(R.string.ok)
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(final MaterialDialog dialog) {
                                        navigateTo(HomeActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                Intent.FLAG_ACTIVITY_NEW_TASK);
                                    }
                                }).show();
                    } else {
                        if (newMenber) {
                            lookup();

                            new DialogBuilder(UserActivity.this).load()
                                    .title(R.string.attention)
                                    .content(R.string.new_member_ok)
                                    .positiveText(R.string.ok)
                                    .callback(new MaterialDialog.ButtonCallback() {
                                        @Override
                                        public void onPositive(MaterialDialog dialog) {
                                            onBackPressed();
                                        }
                                    })
                                    .show();
                        } else if (mainMember) {
                            lookup();
                            new DialogBuilder(UserActivity.this).load()
                                    .title(R.string.attention)
                                    .content(R.string.generic_update_data_ok)
                                    .positiveText(R.string.ok)
                                    .callback(new MaterialDialog.ButtonCallback() {
                                        @Override
                                        public void onPositive(MaterialDialog dialog) {
                                            onBackPressed();
                                        }
                                    })
                                    .show();
                        } else {
                            lookup();
                            new DialogBuilder(UserActivity.this).load()
                                    .title(R.string.attention)
                                    .content(R.string.generic_update_data_ok)
                                    .positiveText(R.string.ok)
                                    .callback(new MaterialDialog.ButtonCallback() {
                                        @Override
                                        public void onPositive(MaterialDialog dialog) {
                                            onBackPressed();
                                        }
                                    })
                                    .show();
                        }

                        //editTextNickname.setText("");
                        //editTextBirthDate.setText("");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {

        if (requestCode == Constants.RequestCode.IMAGE) {

            if (resultCode == RESULT_OK) {

                if (intent.hasExtra(Constants.Bundle.AVATAR)) {

                    final Avatar avatar = (Avatar) intent.getSerializableExtra(Constants.Bundle.AVATAR);

                    //imageViewImage.setImageResource(avatar.getSmall());
                    userAvatar = avatar.getId();

                    if(userAvatar > 0) {
                        imageViewImage.setBackgroundResource(Avatar.getBy(Integer.parseInt(profileImage.getAvatar())).getSmall());
                        userAvatar = Integer.parseInt(profileImage.getAvatar());
                    }
                } else if (intent.hasExtra(Constants.Bundle.URI)) {

                    photoPath = "";

                    final Uri uri = intent.getParcelableExtra(Constants.Bundle.URI);

                    final int width = imageViewImage.getWidth();
                    final int height = imageViewImage.getHeight();

                    //imageViewImage.setImageBitmap(BitmapUtility.scale(width, height, profileImage.getUri().toString()));
                    if (profileImage.getUri() != null) {
                        imageViewImage.getLayoutParams().width = width;
                        imageViewImage.getLayoutParams().height = height;
                        imageViewImage.setImageURI(profileImage.getUri());

                        photoPath = profileImage.getUri().toString();
                    }
                }
            }
        }
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

    private void lookup() {
        SingleUser singleUser = SingleUser.getInstance();
        JSONObject jsonObject = new JSONObject();

        SimpleRequester sendPostRequest = new SimpleRequester();
        sendPostRequest.setUrl(Requester.API_URL + "user/lookup/");
        sendPostRequest.setJsonObject(jsonObject);
        sendPostRequest.setMethod(Method.GET);

        String jsonStr;
        try {
            jsonStr = sendPostRequest.execute(sendPostRequest).get();

            jsonObject = new JSONObject(jsonStr);

            if (jsonObject.get("error").toString() == "true") {
                navigateTo(WelcomeActivity.class);
            } else {

                JSONObject jsonObjectUser = jsonObject.getJSONObject("data");

                singleUser.setNick(jsonObjectUser.getString("nick").toString());
                singleUser.setEmail(jsonObjectUser.getString("email").toString());
                singleUser.setGender(jsonObjectUser.getString("gender").toString());
                singleUser.setId(jsonObjectUser.getString("id").toString());
                singleUser.setRace(jsonObjectUser.getString("race").toString());
                singleUser.setDob(jsonObjectUser.getString("dob").toString());
                singleUser.setUser_token(jsonObjectUser.get("token").toString());

                try {
                    singleUser.setPicture(jsonObjectUser.get("picture").toString());
                } catch (Exception e) {
                    singleUser.setPicture("0");
                }
                //navigateTo(ProfileActivity.class);
            }
        } catch (InterruptedException e) {
            navigateTo(WelcomeActivity.class);
        } catch (ExecutionException e) {
            navigateTo(WelcomeActivity.class);
        } catch (JSONException e) {
            navigateTo(WelcomeActivity.class);
        }
    }
}
