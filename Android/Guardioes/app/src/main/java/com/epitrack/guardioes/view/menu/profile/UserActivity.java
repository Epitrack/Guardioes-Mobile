package com.epitrack.guardioes.view.menu.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.Method;
import com.epitrack.guardioes.request.Requester;
import com.epitrack.guardioes.request.SimpleRequester;
import com.epitrack.guardioes.utility.BitmapUtility;
import com.epitrack.guardioes.utility.Constants;
import com.epitrack.guardioes.utility.DateFormat;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

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
    ImageView imageViewImage;

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

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.user);

        final boolean newMenber = getIntent().getBooleanExtra(Constants.Bundle.NEW_MEMBER, false);

        if (!newMenber) {
            final boolean mainMember = getIntent().getBooleanExtra(Constants.Bundle.MAIN_MEMBER, false);
            final String nick = getIntent().getStringExtra("nick");
            final String dob = getIntent().getStringExtra("dob");
            final String gender = getIntent().getStringExtra("gender");
            final String race = getIntent().getStringExtra("race");
            final String email = getIntent().getStringExtra("email");

            editTextNickname.setText(nick);
            editTextBirthDate.setText(DateFormat.getDate(dob, "dd/MM/yyyy"));
            editTextMail.setText(email);

            if (race.equals("branco")) {
                spinnerRace.setSelection(0);
            } else if (race.equals("preto")) {
                spinnerRace.setSelection(1);
            } else if (race.equals("pardo")) {
                spinnerRace.setSelection(2);
            } else if (race.equals("amarelo")) {
                spinnerRace.setSelection(3);
            } else if (race.equals("indígeno")) {
                spinnerRace.setSelection(4);
            }

            //spinnerGender.setSelection();

            if (gender.equals("M")) {
                spinnerGender.setSelection(0);
            } else {
                spinnerGender.setSelection(1);
            }

            if (mainMember) {

                textViewMessage.setText(R.string.message_fields);

                textLayoutMail.setVisibility(View.VISIBLE);
                editTextMail.setVisibility(View.VISIBLE);

                linearLayoutPassword.setVisibility(View.VISIBLE);
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
        SingleUser singleUser = SingleUser.getInstance();
        final boolean newMenber = getIntent().getBooleanExtra(Constants.Bundle.NEW_MEMBER, false);
        final boolean mainMember = getIntent().getBooleanExtra(Constants.Bundle.MAIN_MEMBER, false);

        user.setNick(editTextNickname.getText().toString().trim());
        user.setDob(editTextBirthDate.getText().toString().trim().toLowerCase());
        String gender = spinnerGender.getSelectedItem().toString();
        gender = gender.substring(0, 1);
        user.setGender(gender.toUpperCase());
        user.setRace(spinnerRace.getSelectedItem().toString().toLowerCase());


        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("nick", user.getNick());
            jsonObject.put("dob", DateFormat.getDate(user.getDob()));
            jsonObject.put("gender", user.getGender());
            jsonObject.put("race", user.getRace());
            jsonObject.put("client", user.getClient());
            jsonObject.put("race", user.getRace());

            if (mainMember) {
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();

                if (!password.equals("")) {
                    if (password.equals(confirmPassword)) {
                        user.setPassword(password);
                        jsonObject.put("password", user.getPassword());
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

            SimpleRequester simpleRequester = new SimpleRequester();

            if (newMenber) {
                simpleRequester.setUrl(Requester.API_URL + "household/create");
            } else if (mainMember) {
                simpleRequester.setUrl(Requester.API_URL + "user/update");
            } else {
                simpleRequester.setUrl(Requester.API_URL + "household/update");
            }

            simpleRequester.setJsonObject(jsonObject);
            simpleRequester.setMethod(Method.POST);

            String jsonStr = simpleRequester.execute(simpleRequester).get();

            jsonObject = new JSONObject(jsonStr);

            if (jsonObject.get("error").toString() == "true") {
                Toast.makeText(getApplicationContext(), jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
            } else {
                if (newMenber) {
                    Toast.makeText(getApplicationContext(), R.string.new_member_ok, Toast.LENGTH_SHORT).show();
                } else if (mainMember) {
                    Toast.makeText(getApplicationContext(), R.string.generic_update_data_ok, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.generic_update_data_ok, Toast.LENGTH_SHORT).show();
                }

                navigateTo(ProfileActivity.class);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {

        if (requestCode == Constants.RequestCode.IMAGE) {

            if (resultCode == RESULT_OK) {

                if (intent.hasExtra(Constants.Bundle.AVATAR)) {

                    final Avatar avatar = (Avatar) intent.getSerializableExtra(Constants.Bundle.AVATAR);

                    imageViewImage.setImageResource(avatar.getSmall());

                } else if (intent.hasExtra(Constants.Bundle.URI)) {

                    final Uri uri = intent.getParcelableExtra(Constants.Bundle.URI);

                    final int width = imageViewImage.getWidth();
                    final int height = imageViewImage.getHeight();

                    imageViewImage.setImageBitmap(BitmapUtility.scale(width, height, uri.getPath()));
                }
            }
        }
    }
}
