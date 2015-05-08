package edu.css.smueggenberg.discgolfdistancepro;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationListener;

/**
 * Created by smueggenberg
 * This activity allows the user to measure the distance between two points
 * by pressing start at the first point and stop at the second point
 */
public class DistanceMeasuringActivity extends FragmentActivity
        implements ConnectionCallbacks,
        LocationListener,
        OnConnectionFailedListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ImageButton btnStartStop, btnCancel;
    private LocationManager lcnmngr;
    private float distance;
    private Location throwLocation, landingLocation;

    // The settings for the fused location listener
    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(1000)
            .setFastestInterval(16)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    private GoogleApiClient mGoogleApiClient;

    // Whether the device is ready to start measuring the throw (Initially true)
    // If true, the user has not pressed the button. If false, the device is measuring the throw
    private boolean ready;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_measuring);

        // Set up the fused location listener
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        lcnmngr = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        setUpMapIfNeeded();
        mMap.clear();

        // Link the buttons to code and set the start button image
        btnCancel = (ImageButton) findViewById(R.id.btnCancel);
        btnStartStop = (ImageButton) findViewById(R.id.btnStartStop);
        btnStartStop.setImageDrawable(getDrawable(R.drawable.start_button));

        ready = true;

        // The button used to calculate the distance of the throw
        // Changes between start and stop with each click
        btnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ready){
                    if(mGoogleApiClient.isConnected()) {
                        // If not already measuring and the location listener is connected,
                        // get the first location, which is where the user made the throw
                        throwLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                        LatLng thrown = new LatLng(throwLocation.getLatitude(), throwLocation.getLongitude());

                        // Set the map focus to the throw location, clear existing markers, and add a marker
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(thrown).title("Throw location"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(thrown, 18));

                        Log.v("Steven", "Accuracy of location: " + throwLocation.getAccuracy());

                        ready = false;
                        btnStartStop.setImageDrawable(getDrawable(R.drawable.stop_button));
                    }else{
                        // Display a error message if unable to connect
                        Toast.makeText(getApplicationContext(), "Error connecting to map services", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    // If the first location has already been set,
                    // get the second location, which is where the throw landed
                    if(mGoogleApiClient.isConnected()) {
                        landingLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                        LatLng landing = new LatLng(landingLocation.getLatitude(), landingLocation.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(landing).title("Thrown"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(landing, 18));


                        Log.v("Steven", "Accuracy of location: " + landingLocation.getAccuracy());

                        ready = true;
                        btnStartStop.setImageDrawable(getDrawable(R.drawable.start_button));

                        // Get the distance between the start and stop points
                        // and convert the units from meters to feet
                        distance = throwLocation.distanceTo(landingLocation) * ((100f) / (2.54f * 12f));

                        // Move the information about the throw to the next activity for review
                        Intent i = new Intent(getApplicationContext(), ThrowEntryActivity.class);
                        i.putExtra("Distance", distance);
                        startActivity(i);
                    }else{
                        // Display a error message if unable to connect
                        Toast.makeText(getApplicationContext(), "Error connecting to map services", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Exit the activity
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        // Follow the user as he/she walks with the device
        if (mGoogleApiClient.isConnected()){
            LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        }
        Log.v("Steven", "Accuracy of location: " + location.getAccuracy());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Do nothing
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, REQUEST, this);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Do nothing
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
     * This is where we can add markers or lines, add listeners or move the camera.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        // Sets up the map near where the user is located
        Location lastLocation = lcnmngr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        LatLng start = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 15));
        Log.v("Steven", "Accuracy of location: " + lastLocation.getAccuracy());
    }
}
