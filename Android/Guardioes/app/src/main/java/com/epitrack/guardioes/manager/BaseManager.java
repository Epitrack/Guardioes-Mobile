package com.epitrack.guardioes.manager;

import android.content.Context;

/**
 * @author Igor Morais
 */
class BaseManager {

    private final Context context;

    public BaseManager(final Context context) {

        if (context == null) {
            throw new IllegalArgumentException("The context cannot be null.");
        }

        this.context = context.getApplicationContext();
    }

    public final Context getContext() {
        return context;
    }
}
