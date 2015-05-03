package edu.css.smueggenberg.discgolfdistancepro;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class DistanceMeasuringActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, LocationListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ImageButton btnStartStop, btnCancel;
    LocationManager lcnmngr;
    float distance;
    Location throwLocation, landingLocation;

    boolean ready;      // Whether the device is ready to start measuring the throw (Initially true)
                        // If true, the user has not pressed the button. If false, the device is measuring the throw

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_measuring);

        lcnmngr = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        setUpMapIfNeeded();

        btnCancel = (ImageButton) findViewById(R.id.btnCancel);
        btnStartStop = (ImageButton) findViewById(R.id.btnStartStop);
        btnStartStop.setImageDrawable(getDrawable(R.drawable.start_button));

        ready = true;

        btnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ready){

                    if(throwLocation != null)
                        throwLocation.reset();

                    throwLocation = lcnmngr.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    LatLng thrown = new LatLng(throwLocation.getLatitude(), throwLocation.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(thrown).title("Thrown"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(thrown, 18));

                    Log.v("Steven", "Accuracy of location: " + throwLocation.getAccuracy());

                    ready = false;
                    btnStartStop.setImageDrawable(getDrawable(R.drawable.stop_button));
                }else{

                    if(landingLocation != null)
                        landingLocation.reset();

                    landingLocation = lcnmngr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    LatLng landing = new LatLng(landingLocation.getLatitude(), landingLocation.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(landing).title("Thrown"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(landing, 18));


                    Log.v("Steven", "Accuracy of location: " + landingLocation.getAccuracy());

                    ready = true;
                    btnStartStop.setImageDrawable(getDrawable(R.drawable.start_button));
                    mMap.clear();

                    distance = throwLocation.distanceTo(landingLocation);

                    Intent i = new Intent(getApplicationContext(), ThrowEntryActivity.class);
                    i.putExtra("Distance", distance);
                    startActivity(i);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));

        Log.v("Steven", "Accuracy of location: " + location.getAccuracy());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    //TODO: create an on location changed event listener so the camera follows the user during the round of disc golf

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        Location lastLocation = lcnmngr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        LatLng start = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 15));
        Log.v("Steven", "Accuracy of location: " + lastLocation.getAccuracy());
    }
}
