package com.epitrack.guardioes.view.account;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.DTO;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.request.Method;
import com.epitrack.guardioes.request.Requester;
import com.epitrack.guardioes.request.SimpleRequester;
import com.epitrack.guardioes.utility.Constants;
import com.epitrack.guardioes.utility.DialogBuilder;
import com.epitrack.guardioes.view.HomeActivity;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.epitrack.guardioes.view.menu.profile.UserActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import io.fabric.sdk.android.Fabric;

/**
 * @author Miqueias Lopes
 */
public class SocialLoginActivity extends BaseAppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @Bind(R.id.fragment_button_facebook)
    LoginButton buttonFaceBook;

    @Bind(R.id.button_google)
    SignInButton buttonGoogle;

    @Bind(R.id.button_twitter)
    TwitterLoginButton buttonTwitter;

    private String modeSociaLogin;

    //Google
    private static final int RC_SIGN_IN = 0;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions mGoogleSignInOptions;
    private ConnectionResult mConnectionResult;

    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;
    private boolean mSignInClicked;

    //Facebook
    private CallbackManager callbackManager;

    //Twitter
    private static final String TWITTER_KEY = "2lnE0tRTpj0VPihSOpvrT13rv";
    private static final String TWITTER_SECRET = "lbcEUcgSSZrzpRDkwPoBlj0BbcWADPymMLvvFewbwTO2j426hx";
    private ProfileTracker profileTracker;

    SingleUser singleUser = SingleUser.getInstance();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.social_login);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar == null) {
            throw new IllegalArgumentException("The actionBar is null.");
        }

        actionBar.setDisplayShowTitleEnabled(false);

        modeSociaLogin = (String)DTO.object;

        if (modeSociaLogin == Constants.Bundle.TWITTER) {

            getTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("Twitter Button")
                    .build());

            TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
            Fabric.with(this, new Twitter(authConfig));

            buttonTwitter.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {

                    TwitterSession session = result.data;
                    TwitterAuthToken authToken = session.getAuthToken();
                    String token = authToken.token;
                    String secret = authToken.secret;
                    String user = session.getUserName();

                    singleUser.setTw(token);
                    singleUser.setNick(user);
                    userExistSocial(token, Constants.Bundle.TWITTER);
                    //executeSocialLogin(false);
                }

                @Override
                public void failure(TwitterException e) {
                    new DialogBuilder(SocialLoginActivity.this).load()
                            .title(R.string.attention)
                            .content(R.string.twitter_fail)
                            .positiveText(R.string.ok)
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(final MaterialDialog dialog) {
                                    onBackPressed();
                                }
                            }).show();

                }
            });

             buttonTwitter.callOnClick();
        } else if (modeSociaLogin == Constants.Bundle.GOOGLE) {

            getTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("Google Button")
                    .build());

            validateServerClientID();

            // Configure sign-in to request the user's ID, email address, and basic
            // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.google_client_id))
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
                    .build();

            buttonGoogle.setScopes(mGoogleSignInOptions.getScopeArray());
            buttonGoogle.setOnClickListener(this);
            //buttonGoogle.callOnClick();
            signIn();
        } else if (modeSociaLogin == Constants.Bundle.FACEBOOK) {

            getTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("Facebook Button")
                    .build());

            FacebookSdk.sdkInitialize(getApplicationContext());
            callbackManager = CallbackManager.Factory.create();

            LoginManager.getInstance().logInWithReadPermissions(SocialLoginActivity.this, Arrays.asList("public_profile", "email"));
            loginFacebook();
        }
    }

    private void loginFacebook() {
        buttonFaceBook.setReadPermissions(Arrays.asList("public_profile", "email", "token_for_business"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        final JSONObject jsonObject = response.getJSONObject();

                        try {

                            String user = jsonObject.getString("name");
                            try {
                                singleUser.setEmail(object.getString("email"));
                            } catch (Exception ex) {

                            }
                            singleUser.setNick(user);

                            Bundle bundle = new Bundle();
                            bundle.putString("fields", "token_for_business");

                            /* make the API call */
                            new GraphRequest(
                                    AccessToken.getCurrentAccessToken(),
                                    "/"+ AccessToken.getCurrentAccessToken().getUserId() +"/ids_for_business",
                                    null,
                                    HttpMethod.GET,
                                    new GraphRequest.Callback() {
                                        public void onCompleted(GraphResponse response) {
                                        /* handle the result */
                                            JSONObject jsonObjectBusiness = response.getJSONObject();
                                            try {
                                                JSONObject jsonObject1 = jsonObjectBusiness.getJSONArray("data").getJSONObject(0);
                                                singleUser.setFb(jsonObject1.getString("id"));
                                                userExistSocial(jsonObject1.getString("id"), Constants.Bundle.FACEBOOK);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                            ).executeAsync();
                            if (singleUser.getFb() != "") {
                                userExistSocial(singleUser.getFb(), Constants.Bundle.FACEBOOK);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                //executeSocialLogin(true);
                new DialogBuilder(SocialLoginActivity.this).load()
                        .title(R.string.attention)
                        .content(R.string.facebook_cancel)
                        .positiveText(R.string.ok)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(final MaterialDialog dialog) {
                                onBackPressed();
                            }
                        }).show();
            }

            @Override
            public void onError(FacebookException exception) {
                //executeSocialLogin(true);
                new DialogBuilder(SocialLoginActivity.this).load()
                        .title(R.string.attention)
                        .content(exception.getMessage())
                        .positiveText(R.string.ok)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(final MaterialDialog dialog) {
                                onBackPressed();
                            }
                        }).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Social Access Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (modeSociaLogin == Constants.Bundle.GOOGLE) {
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
        } else if (modeSociaLogin == Constants.Bundle.FACEBOOK) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } else if (modeSociaLogin == Constants.Bundle.TWITTER) {
            buttonTwitter.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void signIn() {
        try {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } catch (Exception e) {
            new DialogBuilder(SocialLoginActivity.this).load()
                    .title(R.string.attention)
                    .content(e.getMessage())
                    .positiveText(R.string.ok)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(final MaterialDialog dialog) {
                            onBackPressed();
                        }
                    }).show();
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            //GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount acct = result.getSignInAccount();
            assert acct != null;
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            //Uri personPhoto = acct.getPhotoUrl();
            int genderInt = 0;//0 for male, and 1 for female

            updateUI(true);
            singleUser.setGl(personId);
            singleUser.setEmail(personEmail);
            singleUser.setPassword(personEmail);
            singleUser.setNick(personName);

            if (genderInt == 0) {
                singleUser.setGender("M");
            } else {
                singleUser.setGender("F");
            }

            userExistSocial(personId, Constants.Bundle.GOOGLE);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
            new DialogBuilder(SocialLoginActivity.this).load()
                    .title(R.string.attention)
                    .content("Status: " + result.getStatus() + "Status Code: " + result.getStatus().getStatusCode() + "Status Message: " + result.getStatus().getStatusMessage())
                    .positiveText(R.string.ok)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(final MaterialDialog dialog) {
                            onBackPressed();
                        }
                    }).show();
        }
    }

    private void validateServerClientID() {
        String serverClientId = getString(R.string.google_client_id);
        String suffix = ".apps.googleusercontent.com";
        if (!serverClientId.trim().endsWith(suffix)) {
            String message = "Invalid server client ID in strings.xml, must end with " + suffix;

            //Log.w(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_google:
                //signInWithGplus();
                signIn();
                break;
        }
    }

    private void userExistSocial(String token, String type) {

        String url = "";

        if (type == Constants.Bundle.GOOGLE) {
            url = "user/get?gl=";
        } else if (type == Constants.Bundle.FACEBOOK) {
            url = "user/get?fb=";
        } else if (type == Constants.Bundle.TWITTER) {
            url = "user/get?tw=";
        }

        SimpleRequester simpleRequester = new SimpleRequester();
        simpleRequester.setMethod(Method.GET);
        simpleRequester.setUrl(Requester.API_URL + url + token);
        simpleRequester.setContext(this);

        try {
            String jsonStr = simpleRequester.execute(simpleRequester).get();

            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.get("error").toString() == "false") {

                JSONArray jsonArray = jsonObject.getJSONArray("data");

                if (jsonArray.length() > 0) {

                    JSONObject jsonObjectUser = jsonArray.getJSONObject(0);

                    String email = jsonObjectUser.getString("email").toString();
                    String password = jsonObjectUser.getString("email").toString();

                    jsonObject = new JSONObject();
                    jsonObject.put("email", email);
                    jsonObject.put("password", password);

                    simpleRequester = new SimpleRequester();
                    simpleRequester.setUrl(Requester.API_URL + "user/login");
                    simpleRequester.setJsonObject(jsonObject);
                    simpleRequester.setMethod(Method.POST);
                    simpleRequester.setContext(this);

                    jsonStr = simpleRequester.execute(simpleRequester).get();

                    jsonObject = new JSONObject(jsonStr);

                    if (jsonObject.get("error").toString() == "true") {

                        new DialogBuilder(SocialLoginActivity.this).load()
                                .title(R.string.attention)
                                .content(jsonObject.get("message").toString())
                                .positiveText(R.string.ok)
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(final MaterialDialog dialog) {
                                        onBackPressed();
                                    }
                                }).show();

                    } else {
                        singleUser.setNick(jsonObjectUser.getString("nick").toString());
                        singleUser.setEmail(jsonObjectUser.getString("email").toString());
                        singleUser.setGender(jsonObjectUser.getString("gender").toString());

                        try {
                            singleUser.setPicture(jsonObjectUser.getString("picture").toString());
                        } catch (Exception e) {
                            singleUser.setPicture("0");
                        }

                        singleUser.setId(jsonObjectUser.getString("id").toString());
                        singleUser.setRace(jsonObjectUser.getString("race").toString());
                        singleUser.setDob(jsonObjectUser.getString("dob").toString());
                        singleUser.setUser_token(jsonObject.get("token").toString());

                        SharedPreferences sharedPreferences = getSharedPreferences(Constants.Pref.PREFS_NAME, 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString(Constants.Pref.PREFS_NAME, singleUser.getUser_token());
                        editor.apply();

                        navigateTo(HomeActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                } else {
                    //if (type != Constants.Bundle.FACEBOOK) {
                        Bundle dtoBundle = new Bundle();

                        dtoBundle.putBoolean(Constants.Bundle.SOCIAL_NEW, true);
                        dtoBundle.putBoolean(Constants.Bundle.NEW_MEMBER, false);
                        dtoBundle.putBoolean(Constants.Bundle.MAIN_MEMBER, false);

                        DTO.object = null;
                        navigateTo(UserActivity.class, dtoBundle);
                    //}
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (profileTracker != null) {
            profileTracker.stopTracking();
        }
    }

    protected void onStart() {
        super.onStart();
        /*if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }*/
    }

    protected void onStop() {
        super.onStop();
        /*if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }
        }*/
    }

    @Override
    public void onConnected(Bundle bundle) {
        //mSignInClicked = false;
        //getProfileInformation();
        //updateUI(true);
    }

    @Override
    public void onConnectionSuspended(int i) {
        //mGoogleApiClient.connect();
        //updateUI(false);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        /*if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (result.getErrorCode() > 0) {
                resolveSignInError();
            }

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }*/
    }

    private void signInWithGplus() {
        /*if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }

        getProfileInformation();*/
    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    private void updateUI(boolean isSignedIn) {

    }

    private String getMessageConnectionFailedGoogle(int errorCode) {

        String strReturn = "";

        if (errorCode == Constants.Google.API_UNAVAILABLE) {

            strReturn = "Um dos componentes da API que você tentou se conectar não está disponível.";

        } else if (errorCode == Constants.Google.CANCELED) {

            strReturn = "A conexão foi cancelada.";

        } else if (errorCode == Constants.Google.DEVELOPER_ERROR) {

            strReturn = "O aplicativo está configurado incorretamente.";

        } else if (errorCode == Constants.Google.INTERNAL_ERROR) {

            strReturn = "Ocorreu um erro interno.";

        } else if (errorCode == Constants.Google.INTERRUPTED) {

            strReturn = "Uma interrupção ocorreu enquanto aguardava a conexão completa.";

        } else if (errorCode == Constants.Google.INVALID_ACCOUNT) {

            strReturn = "O cliente tentou se conectar ao serviço com um nome de conta inválido especificado.";

        } else if (errorCode == Constants.Google.LICENSE_CHECK_FAILED) {

            strReturn = "O aplicativo não está licenciado para o usuário.";

        } else if (errorCode == Constants.Google.NETWORK_ERROR) {

            strReturn = "Ocorreu um erro de rede.";

        } else if (errorCode == Constants.Google.RESOLUTION_REQUIRED) {

            strReturn = "Conclusão da conexão requer alguma forma de resolução.";

        } else if (errorCode == Constants.Google.SERVICE_DISABLED) {

            strReturn = "A versão instalada dos serviços do Google Play foi desativada neste dispositivo.";

        } else if (errorCode == Constants.Google.SERVICE_INVALID) {

            strReturn = "A versão dos serviços Google Play instalados neste dispositivo não é original.";

        } else if (errorCode == Constants.Google.SERVICE_MISSING) {

            strReturn = "Serviços do Google Play está faltando neste dispositivo.";

        } else if (errorCode == Constants.Google.SERVICE_MISSING_PERMISSION) {

            strReturn = "Serviço Google Play não tem um ou mais permissões necessárias.";

        } else if (errorCode == Constants.Google.SERVICE_UPDATING) {

            strReturn = "Serviço Google Play está sendo atualizado neste dispositivo.";

        } else if (errorCode == Constants.Google.SERVICE_VERSION_UPDATE_REQUIRED) {

            strReturn = "A versão instalada dos serviços do Google Play está desatualizada.";

        } else if (errorCode == Constants.Google.SIGN_IN_FAILED) {

            strReturn = "O cliente tentou conectar-se ao serviço, mas o usuário não está conectado.";

        } else if (errorCode == Constants.Google.SIGN_IN_REQUIRED) {

            strReturn = "O cliente tentou conectar-se ao serviço, mas o usuário não está conectado.";

        } else if (errorCode == Constants.Google.TIMEOUT) {

            strReturn = "O tempo limite foi ultrapassado, enquanto espera para a conexão para concluir.";
        }

        return strReturn;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        onBackPressed();
        return true;
    }
}
