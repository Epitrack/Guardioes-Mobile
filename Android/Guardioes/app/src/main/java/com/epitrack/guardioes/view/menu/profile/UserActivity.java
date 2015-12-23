package com.epitrack.guardioes.view.menu.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.Method;
import com.epitrack.guardioes.request.Requester;
import com.epitrack.guardioes.request.SimpleRequester;
import com.epitrack.guardioes.utility.BitmapUtility;
import com.epitrack.guardioes.utility.Constants;
import com.epitrack.guardioes.utility.DateFormat;
import com.epitrack.guardioes.utility.DialogBuilder;
import com.epitrack.guardioes.utility.Mask;
import com.epitrack.guardioes.view.HomeActivity;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.epitrack.guardioes.view.welcome.WelcomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
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

    boolean socialNew;
    boolean newMenber;
    boolean mainMember;
    SingleUser singleUser = SingleUser.getInstance();
    private int userAvatar = 0;
    String photoPath = "";

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        socialNew = getIntent().getBooleanExtra(Constants.Bundle.SOCIAL_NEW, false);
        newMenber = getIntent().getBooleanExtra(Constants.Bundle.NEW_MEMBER, false);
        mainMember = getIntent().getBooleanExtra(Constants.Bundle.MAIN_MEMBER, false);

        setContentView(R.layout.user);

        editTextBirthDate.addTextChangedListener(Mask.insert("##/##/####", editTextBirthDate));

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

    private void loadUser() {
        String nick;
        String dob;
        String gender;
        String race;
        String email;
        String picture;

        if (socialNew) {
            nick = singleUser.getNick();
            dob = singleUser.getDob();
            gender = singleUser.getGender();
            race = singleUser.getRace();
            email = singleUser.getEmail();
            picture = singleUser.getPicture();

        } else {
            nick = getIntent().getStringExtra("nick");
            dob = getIntent().getStringExtra("dob");
            gender = getIntent().getStringExtra("gender");
            race = getIntent().getStringExtra("race");
            email = getIntent().getStringExtra("email");
            picture = getIntent().getStringExtra("picture");
        }

        if (!newMenber || socialNew) {

            try {
                    editTextNickname.setText(nick);

                    if (dob != null) {
                        editTextBirthDate.setText(DateFormat.getDate(dob, "dd/MM/yyyy"));
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

                    if (gender != null) {
                        if (gender.equals("M")) {
                            spinnerGender.setSelection(0);
                        } else {
                            spinnerGender.setSelection(1);
                        }

                        if (!picture.equals("")) {
                            imageViewImage.setImageResource(Avatar.getBy(Integer.parseInt(picture)).getSmall());
                        } else if (singleUser.getImageResource() == null) {
                            if (gender.equals("M")) {
                                if (race.equals("branco") || race.equals("amarelo")) {
                                    imageViewImage.setImageResource(R.drawable.image_avatar_6);
                                } else {
                                    imageViewImage.setImageResource(R.drawable.image_avatar_4);
                                }
                            } else {

                                if (race.equals("branco") || race.equals("amarelo")) {
                                    imageViewImage.setImageResource(R.drawable.image_avatar_8);
                                } else {
                                    imageViewImage.setImageResource(R.drawable.image_avatar_7);
                                }
                            }
                        } else {
                            if (singleUser.getEmail().equals(email) && !singleUser.getImageResource().equals("")) {
                                imageViewImage.setImageBitmap(BitmapUtility.scale((singleUser.getWidthImageProfile() / 2), (singleUser.getHeightImageProfile() / 2), singleUser.getImageResource()));
                            }
                        }
                    }

                    if (mainMember) {
                        textViewMessage.setText(R.string.message_fields);

                        textLayoutMail.setVisibility(View.VISIBLE);
                        editTextMail.setVisibility(View.VISIBLE);

                        linearLayoutPassword.setVisibility(View.VISIBLE);

                        if (Integer.parseInt(singleUser.getPicture()) > 0) {
                            imageViewImage.setImageResource(Avatar.getBy(Integer.parseInt(singleUser.getPicture())).getSmall());
                        }
                    } else if (socialNew) {
                        if (singleUser.getEmail() == null) {
                            textLayoutMail.setVisibility(View.VISIBLE);
                            editTextMail.setVisibility(View.VISIBLE);
                            editTextMail.setEnabled(true);
                        }
                    }

                } catch (Exception e) {

                e.printStackTrace();
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
        User user = new User();

        //final boolean newMenber = getIntent().getBooleanExtra(Constants.Bundle.NEW_MEMBER, false);
        //final boolean mainMember = getIntent().getBooleanExtra(Constants.Bundle.MAIN_MEMBER, false);

        user.setNick(editTextNickname.getText().toString().trim());
        user.setDob(editTextBirthDate.getText().toString().trim().toLowerCase());
        String gender = spinnerGender.getSelectedItem().toString();
        gender = gender.substring(0, 1);
        user.setGender(gender.toUpperCase());
        user.setRace(spinnerRace.getSelectedItem().toString().toLowerCase());

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

                if (userAvatar > 0) {
                    jsonObject.put("picture", userAvatar);
                } else if (photoPath.length() > 0) {
                    try {
                        JSONObject uploadImageField = new JSONObject();
                        photoPath = photoPath.replace("\"", "");
                        uploadImageField.put("uploadFile", photoPath);
                        photoPath = "";

                        SimpleRequester uploadImage = new SimpleRequester();
                        uploadImage.setMethod(Method.POST);
                        uploadImage.setJsonObject(uploadImageField);
                        uploadImage.setUrl(Requester.API_URL + "user/upload-photo");

                        String uploadImageStr = uploadImage.execute(uploadImage).get();
                        JSONObject uploadImageJsonObject = new JSONObject(uploadImageStr);

                        if (uploadImageJsonObject.get("error").toString().equals("true")) {
                            jsonObject.put("picture", "0");
                        }
                    } catch (JSONException e) {
                        jsonObject.put("picture", "0");
                    } catch (InterruptedException e) {
                        jsonObject.put("picture", "0");
                    } catch (ExecutionException e) {
                        jsonObject.put("picture", "0");
                    }
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

                    if (singleUser.getEmail() == null) {
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
                            new DialogBuilder(UserActivity.this).load()
                                    .title(R.string.attention)
                                    .content(R.string.new_member_ok)
                                    .positiveText(R.string.ok)
                                    .show();
                        } else if (mainMember) {
                            new DialogBuilder(UserActivity.this).load()
                                    .title(R.string.attention)
                                    .content(R.string.generic_update_data_ok)
                                    .positiveText(R.string.ok)
                                    .show();
                            lookup();
                        } else {
                            new DialogBuilder(UserActivity.this).load()
                                    .title(R.string.attention)
                                    .content(R.string.generic_update_data_ok)
                                    .positiveText(R.string.ok)
                                    .show();
                            lookup();
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

                    imageViewImage.setImageResource(avatar.getSmall());
                    userAvatar = avatar.getId();

                } else if (intent.hasExtra(Constants.Bundle.URI)) {

                    final Uri uri = intent.getParcelableExtra(Constants.Bundle.URI);

                    final int width = imageViewImage.getWidth();
                    final int height = imageViewImage.getHeight();

                    imageViewImage.setImageBitmap(BitmapUtility.scale(width, height, uri.getPath().replace("\"", "")));

                    photoPath = uri.getPath();

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
