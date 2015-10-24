package com.epitrack.guardioes.view.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.DTO;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.request.Method;
import com.epitrack.guardioes.request.Requester;
import com.epitrack.guardioes.request.SimpleRequester;
import com.epitrack.guardioes.utility.Constants;
import com.epitrack.guardioes.utility.DateFormat;
import com.epitrack.guardioes.utility.DialogBuilder;
import com.epitrack.guardioes.utility.Utility;
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
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

/**
 * @author Miqueias Lopes
 */
public class SocialLoginActivity extends BaseAppCompatActivity {

    @Bind(R.id.fragment_button_facebook)
    LoginButton buttonFaceBook;

    @Bind(R.id.button_google)
    Button buttonGoogle;

    @Bind(R.id.button_twitter)
    TwitterLoginButton buttonTwitter;

    private String modeSociaLogin;
    private GoogleApiClient authGoogle;
    private ProfileTracker profileTracker;
    private CallbackManager callbackManager;

    private static final String TWITTER_KEY = "2lnE0tRTpj0VPihSOpvrT13rv";
    private static final String TWITTER_SECRET = "lbcEUcgSSZrzpRDkwPoBlj0BbcWADPymMLvvFewbwTO2j426hx";

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
                    executeSocialLogin(false);
                }

                @Override
                public void failure(TwitterException e) {
                    executeSocialLogin(true);

                }
            });

             buttonTwitter.callOnClick();
        } else if (modeSociaLogin == Constants.Bundle.GOOGLE) {
            buttonGoogle.callOnClick();
        } else if (modeSociaLogin == Constants.Bundle.FACEBOOK) {

            FacebookSdk.sdkInitialize(getApplicationContext());
            callbackManager = CallbackManager.Factory.create();

            if (AccessToken.getCurrentAccessToken() != null) {

                userExistSocial(AccessToken.getCurrentAccessToken().getUserId(), Constants.Bundle.FACEBOOK);

                //AccessToken.setCurrentAccessToken(null);

                new DialogBuilder(SocialLoginActivity.this).load()
                        .title(R.string.attention)
                        .content(R.string.facebook_fail)
                        .positiveText(R.string.ok)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(final MaterialDialog dialog) {
                                navigateTo(CreateAccountActivity.class);
                            }
                        }).show();
            }

            buttonFaceBook.setReadPermissions(Arrays.asList("public_profile", "email"));
            buttonFaceBook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
               @Override
               public void onSuccess(final LoginResult loginResult) {

                   GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                       @Override
                       public void onCompleted(JSONObject object, GraphResponse response) {

                           JSONObject jsonObject = response.getJSONObject();

                           try {

                               String user = jsonObject.getString("name");
                               //String email = jsonObject.getString("email");
                               //String gender = jsonObject.getString("gender");
                               //gender = gender.substring(0, 1).toUpperCase();
                                singleUser.setFb(loginResult.getAccessToken().getUserId());
                                singleUser.setNick(user);
                               //singleUser.setEmail(email);
                               //singleUser.setPassword(email);
                               //singleUser.setGender(gender);
                                userExistSocial(loginResult.getAccessToken().getToken(), Constants.Bundle.FACEBOOK);
                                executeSocialLogin(false);

                           } catch (JSONException e) {
                               e.printStackTrace();
                           }
                       }
                   });
                   request.executeAsync();
               }

               @Override
               public void onCancel() {
                   executeSocialLogin(true);
               }

               @Override
               public void onError(FacebookException exception) {
                   executeSocialLogin(true);
               }
           });

            buttonFaceBook.callOnClick();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (modeSociaLogin == Constants.Bundle.GOOGLE) {
            authGoogle.connect();
        } else if (modeSociaLogin == Constants.Bundle.FACEBOOK) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } else if (modeSociaLogin == Constants.Bundle.TWITTER) {
            buttonTwitter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @OnClick(R.id.button_google)
    public void onGoogle() {
        loadGoogle(getApplicationContext());
        authGoogle.connect();
    }

    private void loadGoogle(final Context context) {

        if (authGoogle == null) {

            final GoogleHandler handler = new GoogleHandler();

            authGoogle = new GoogleApiClient.Builder(context).addConnectionCallbacks(handler)
                    .addOnConnectionFailedListener(handler)
                    .addApi(Plus.API)
                    .addScope(Plus.SCOPE_PLUS_LOGIN)
                    .addScope(Plus.SCOPE_PLUS_PROFILE)
                    .build();
        }
    }

    private void getDatabyGoogleApi() {
        try {

            if (authGoogle.isConnected()) {
                Person person = Plus.PeopleApi.getCurrentPerson(authGoogle);

                if (person != null) {

                    String personName = person.getDisplayName();
                    int genderInt = person.getGender();//0 for male, and 1 for female
                    String email = Plus.AccountApi.getAccountName(authGoogle);

                    singleUser.setGl(person.getId());
                    singleUser.setEmail(email);
                    singleUser.setPassword(email);
                    singleUser.setNick(personName);
                    if (genderInt == 0) {
                        singleUser.setGender("M");
                    } else {
                        singleUser.setGender("F");
                    }
                    userExistSocial(person.getId(), Constants.Bundle.GOOGLE);
                    executeSocialLogin(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class GoogleHandler implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

        @Override
        public void onConnected(final Bundle bundle) {
            getDatabyGoogleApi();
            executeSocialLogin(false);
        }

        @Override
        public void onConnectionSuspended(final int i) {
            authGoogle.connect();
        }

        @Override
        public void onConnectionFailed(final ConnectionResult connectionResult) {
            executeSocialLogin(true);
        }
    }

    private void executeSocialLogin(boolean loginFail) {

        if (loginFail) {

            new DialogBuilder(SocialLoginActivity.this).load()
                    .title(R.string.attention)
                    .content(R.string.google_fail)
                    .positiveText(R.string.ok)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(final MaterialDialog dialog) {
                            navigateTo(CreateAccountActivity.class);
                        }
                    }).show();
        } else  {

            if (userExist(singleUser.getEmail())) {

                new DialogBuilder(SocialLoginActivity.this).load()
                        .title(R.string.attention)
                        .content(R.string.user_exist)
                        .positiveText(R.string.ok)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(final MaterialDialog dialog) {
                                navigateTo(CreateAccountActivity.class);
                            }
                        }).show();
            } else {

                Bundle dtoBundle = new Bundle();

                dtoBundle.putBoolean(Constants.Bundle.SOCIAL_NEW, true);
                dtoBundle.putBoolean(Constants.Bundle.NEW_MEMBER, false);
                dtoBundle.putBoolean(Constants.Bundle.MAIN_MEMBER, false);

                DTO.object = null;
                navigateTo(UserActivity.class, dtoBundle);
            }
        }
    }

    private boolean userExist(String email) {

        boolean bReturn = false;

        SimpleRequester simpleRequester = new SimpleRequester();
        JSONObject jsonObject = new JSONObject();

        simpleRequester.setUrl(Requester.API_URL + "user/get?email=" + email);
        simpleRequester.setMethod(Method.GET);
        simpleRequester.setJsonObject(jsonObject);

        try {
            String jsonStr = simpleRequester.execute(simpleRequester).get();

            jsonObject = new JSONObject(jsonStr);

            if (jsonObject.get("error").toString() == "true") {
                bReturn = true;
            } else {

                JSONArray jsonArray = jsonObject.getJSONArray("data");

                if (jsonArray.length() > 0) {
                    bReturn = true;
                } else {
                    bReturn = false;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bReturn;
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

                    jsonStr = simpleRequester.execute(simpleRequester).get();

                    jsonObject = new JSONObject(jsonStr);

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
}
