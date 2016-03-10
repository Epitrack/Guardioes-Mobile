package com.epitrack.guardioes.request;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.utility.Logger;
import com.epitrack.guardioes.utility.MessageText;
import com.epitrack.guardioes.utility.SingleContext;
import com.epitrack.guardioes.view.dialog.LoadDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * @author Miquéias Lopes on 09/09/15.
 */
public class SimpleRequester extends AsyncTask<SimpleRequester, Object, String> {

    private static final String TAG = Requester.class.getSimpleName();
    private String url;
    private JSONObject jsonObject;
    private Method method;
    private boolean otherAPI = false;
    private Context context;
    private String strReturn;

    private RequestListener<String> listener;

    private LoadDialog loadDialog = new LoadDialog();

    public SimpleRequester() {

    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setOtherAPI(boolean otherAPI) {
        this.otherAPI = otherAPI;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setListener(final RequestListener<String> listener) {
        this.listener = listener;
    }

    public String getStrReturn() {
        return this.strReturn;
    }

    @Override
    protected void onPreExecute() {

        if (context != null) {
            loadDialog.show(((Activity) context).getFragmentManager(), LoadDialog.TAG);
        }

        if (listener != null) {
            listener.onStart();
        }
    }

    @Override
    protected void onPostExecute(String result) {

        if (loadDialog.isVisible()) {
            loadDialog.dismiss();
        }

        if (listener != null) {
            listener.onSuccess(result);
        }

        this.strReturn = result;
    }

    @Override
    protected String doInBackground(SimpleRequester... params) {

        try {

            SingleContext singleContext = SingleContext.getInstance();
            Context localContext;

            if (this.context != null) {
                singleContext.setContext(this.context);
            }

            localContext = singleContext.getContext();

            return SendPostRequest(url, method, jsonObject, otherAPI, localContext);

        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public void updateContext() {
        SingleContext singleContext = SingleContext.getInstance();

        if (context != null) {
            singleContext.setContext(context);
        }
    }

    private static String SendPostRequest(String apiUrl, Method method, JSONObject jsonObjSend, boolean otherAPI, Context context) throws JSONException, IOException, SimpleRequesterException {

        URL url;
        String returnStr = "";
        SingleUser singleUser = SingleUser.getInstance();

        try {
            url = new URL(apiUrl);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + apiUrl);
        }

        if (method == Method.POST) {

            HttpsURLConnection conn = null;
            byte[] bytes = null;
            conn = (HttpsURLConnection) url.openConnection();
            //HTTPS
            conn.setSSLSocketFactory(generateCertificate(context).getSocketFactory());

            String body = "";
            if (jsonObjSend != null) {
                body = jsonObjSend.toString();
            }

            Logger.logDebug(TAG, body + "' to " + url);
            bytes = body.getBytes();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("app_token", singleUser.getApp_token());
            conn.setRequestProperty("user_token", singleUser.getUser_token());

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(String.valueOf(method));
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();

            int status = conn.getResponseCode();
            if (status != 200) {
                return MessageText.ERROR_SERVER.toString();
            }

            String convertStreamToString = "";
            if (conn != null) {
                convertStreamToString = convertStreamToString(conn.getInputStream(), /*HTTP.UTF_8*/"UTF-8");
                conn.disconnect();
            }
            return convertStreamToString;
        } else if (method == Method.GET) {

            HttpsURLConnection urlConnection;
            BufferedReader br;

            if (url.toString().substring(0, 12).equals("https://maps")) {
                url = new URL(apiUrl);
                URLConnection urlConn  = url.openConnection();

                br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            } else {
                urlConnection = (HttpsURLConnection) url.openConnection();
                //HTTPS
                urlConnection.setSSLSocketFactory(generateCertificate(context).getSocketFactory());

                if (!otherAPI) {
                    urlConnection.setRequestProperty("app_token", singleUser.getApp_token());
                    urlConnection.setRequestProperty("user_token", singleUser.getUser_token());
                }
                br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            }

            String inputLine;
            String jsonStr = "";
            while ((inputLine = br.readLine()) != null) {
                //if (!otherAPI) {
                //    jsonStr = inputLine;
                //} else {
                    jsonStr += inputLine;
                //}
            }
            br.close();

            returnStr = jsonStr;
            return jsonStr;
        } else{
            return returnStr;
        }
    }

    private static String convertStreamToString(InputStream is, String enc) throws UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, enc));
        StringBuilder sb = new StringBuilder();
        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    private static SSLContext generateCertificate(Context context) {

        // Load CAs from an InputStream
        // (could be from a resource or ByteArrayInputStream or ...)
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }

        InputStream caInput = null;
        try {

            caInput = context.getAssets().open("47b83fbefd5092a.crt");
            //caInput = new BufferedInputStream(new FileInputStream(context.getAssets().open("47b83fbefd5092a.crt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Certificate ca = null;
        try {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        } catch (CertificateException e) {
            e.printStackTrace();
        } finally {
            try {
                caInput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = null;
        try {
            tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // Create an SSLContext that uses our TrustManager
        SSLContext SSLcontext = null;
        try {
            SSLcontext = SSLContext.getInstance("TLS");
            SSLcontext.init(null, tmf.getTrustManagers(), null);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return SSLcontext;
    }
}
