package com.epitrack.guardioes.request;

import android.content.Context;

public class Base {

    private final Context context;

    public Base(final Context context) {
        this.context = context;
    }

    public final Context getContext() {
        return context;
    }
}
