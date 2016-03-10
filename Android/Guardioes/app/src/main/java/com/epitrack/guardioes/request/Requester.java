package com.epitrack.guardioes.request;

import android.content.Context;

/**
 * @author Igor Morais
 */
public class Requester extends BaseRequester {

    private static final String TAG = Requester.class.getSimpleName();
    public static final String API_URL = "https://api.guardioesdasaude.org/";
    public static final String API_URL_PHOTO = "https://api.guardioesdasaude.org";

    public Requester(final Context context) {
        super(context);
    }
}
