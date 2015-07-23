package com.epitrack.guardioes.view;

import android.location.Location;
import android.os.Handler;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.manager.LocationManager;
import com.epitrack.guardioes.manager.OnLocationListener;
import com.epitrack.guardioes.utility.LocationUtility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public abstract class AbstractBaseMapActivity extends BaseAppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, OnLocationListener {

    private static final long DEFAULT_ZOOM = 15;

    private final Handler handler = new Handler();

    private MarkerOptions markerOption;
    private Marker userMarker;

    private GoogleMap map;

    private LocationManager locationManager;

    @Override
    protected void onResume() {
        super.onResume();

//        if (locationManager.getLocationManager().isConnected()) {
//
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Stop
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        setMap(map);

        locationManager = new LocationManager(this, this);
    }

    @Override
    public void onLastLocation(final Location location) {

        final LatLng latLng = LocationUtility.toLatLng(location);

        getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM), new GoogleMap.CancelableCallback() {

            @Override
            public void onFinish() {
                userMarker = getMap().addMarker(loadMarkerOption().position(latLng));
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public void onLocation(final Location location) {

        if (getUserMarker() != null) {
            getUserMarker().setPosition(LocationUtility.toLatLng(location));
        }
    }

    private MarkerOptions loadMarkerOption() {

        if (markerOption == null) {
            markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon));
        }

        return markerOption;
    }

    private void setMap(final GoogleMap map) {

        map.setOnMarkerClickListener(this);

        this.map = map;
    }

    public final GoogleMap getMap() {
        return map;
    }

    public final Marker getUserMarker() {
        return userMarker;
    }
}
