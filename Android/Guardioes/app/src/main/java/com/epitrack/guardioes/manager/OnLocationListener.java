package com.epitrack.guardioes.manager;

import android.location.Location;

public interface OnLocationListener {

    void onLastLocation(Location location);

    void onLocation(Location location);
}
