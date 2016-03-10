package com.epitrack.guardioes.view.account;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.request.Method;
import com.epitrack.guardioes.request.Requester;
import com.epitrack.guardioes.request.SimpleRequester;
import com.epitrack.guardioes.utility.DialogBuilder;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.google.android.gms.analytics.HitBuilders;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Miquéias Lopes
 */
public class ForgotPasswordActivity extends BaseAppCompatActivity {

    @Bind(R.id.button_forgot_password)
    Button btnForgotPassword;

    @Bind(R.id.edit_text_email_forgot)
    EditText editTextEmail;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.forgot_password);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar == null) {
            throw new IllegalArgumentException("The actionBar is null.");
        }

        actionBar.setDisplayShowTitleEnabled(false);
    }


    @OnClick(R.id.button_forgot_password)
    public void sendEmail() {
        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Forgot Password Button")
                .build());

        String message = forgotPassword(editTextEmail.getText().toString().trim().toLowerCase());

        if (message != "") {
            new DialogBuilder(ForgotPasswordActivity.this).load()
                    .title(R.string.attention)
                    .content(message)
                    .positiveText(R.string.ok)
                    .callback(new MaterialDialog.ButtonCallback() {

                        @Override
                        public void onPositive(final MaterialDialog dialog) {
                            onBackPressed();
                        }

                    }).show();
        }
    }

    private String forgotPassword(String email) {

        try {

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("email", email);

            SimpleRequester sendPostRequest = new SimpleRequester();
            sendPostRequest.setUrl(Requester.API_URL + "user/forgot-password");
            sendPostRequest.setJsonObject(jsonObject);
            sendPostRequest.setMethod(Method.POST);

            String jsonStr = sendPostRequest.execute(sendPostRequest).get();

            jsonObject = new JSONObject(jsonStr);

            return jsonObject.get("message").toString();
        } catch (Exception e) {
            return "Não foi possível enviar o e-mail. Tente novamente em alguns minutos";
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Forgot Password Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }
}
