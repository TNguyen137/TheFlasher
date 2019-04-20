package com.android.theflasherapp.MapsService;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.List;

public class MapsGPS extends Service implements LocationListener {
    private static final String TAG = MapsGPS.class.getSimpleName();

    private Activity mActivity;
    private Context mContext;
    private GoogleMap googleMap;
    private Marker marker;
    private boolean mLocationPermissionGranted;

    // Create location necessities
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    boolean canGetGpsLocation = false;

    Location location;
    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; //meters
    private static final long MIN_TIME_BETWEEN_UPDATES = 1000 * 5 * 1; // 5second
    private static final long TIME_BETWEEN_UPDATES = 1000 * 10 * 1; // 10seconds

    // Constructors
    public MapsGPS() {}

    public MapsGPS(Context context, GoogleMap googleMap) {
        this.mContext = context;
        this.googleMap = googleMap;
    }

    public MapsGPS(Activity activity, GoogleMap googleMap) {
        this.mActivity = activity;
        this.googleMap = googleMap;
    }

    public MapsGPS(Activity activity, Context context, GoogleMap googleMap, FusedLocationProviderClient fusedLocationProviderClient, boolean mLocationPermissionGranted) {
        this.mActivity = activity;
        this.mContext = context;
        this.googleMap = googleMap;
        this.mFusedLocationProviderClient = fusedLocationProviderClient;
        this.mLocationPermissionGranted = mLocationPermissionGranted;
        createLocationCallback();
        createLocationRequest();
        getCurrentLocation();
    }

    @Override
    public void onCreate() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationCallback();
        createLocationRequest();
        getCurrentLocation();
    }

    public boolean canGetGpsLocation() {
        return this.canGetGpsLocation;
    }

    public Location getLocation() {
        return this.location;
    }
    /**
     * Method to get the current GPS location
     * @return current Location
     */
    @SuppressLint("MissingPermission")
    public Location getCurrentLocation() {
        try {
            if (mLocationPermissionGranted) {
                this.canGetGpsLocation = true;
                mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                Toast.makeText(mContext, "Current Location Found!", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("Location Permissions", "Permissions not granted, please enable location permissions");
                requestLocationPermission();
            }
        } catch (Exception e) {
            e.printStackTrace();;
        }
        return location;
    }

    /**
     * Method to get the Latitude coordinate
     * @return Latitude coordinate
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    /**
     * Method to get the Longitude coordinate
     * @return Longitude coordinate
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    /**
     * Method to request Location Permissions using the Dexter library
     */
    public void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            Dexter.withActivity(this.mActivity).withPermissions(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                mLocationPermissionGranted = true;
                                Toast.makeText(mContext, "All permissions are granted!", Toast.LENGTH_SHORT).show();
                                getCurrentLocation();
                            }
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                mLocationPermissionGranted = false;
                                showSettingsDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).withErrorListener(new PermissionRequestErrorListener() {
                @Override
                public void onError(DexterError error) {
                    mLocationPermissionGranted = false;
                    Toast.makeText(getApplicationContext(), "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            })
                    .onSameThread().check();
        } else {
            mLocationPermissionGranted = true;
        }
    }

    /**
     * Method to show settings alert dialog
     * When pressing the Go To Settings button it will launch application settings
     */
    private void showSettingsDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
        alertDialog.setTitle("Need Permissions");
        alertDialog.setMessage("This app needs location permissions to use!  You can grant them in app settings.");
        alertDialog.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
        intent.setData(uri);
        mActivity.startActivity(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        //get location name
        Geocoder geocoder = new Geocoder(mContext);
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String result = addresses.get(0).getLocality() + " : ";
            result += addresses.get(0).getCountryName();
            LatLng latlng = new LatLng(latitude, longitude);
            if (marker != null) {
                marker.remove();
                marker = googleMap.addMarker(new MarkerOptions().position(latlng).title(result));
                googleMap.setMaxZoomPreference(20.0f);
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
            } else {
                marker = googleMap.addMarker(new MarkerOptions().position(latlng).title(result));
                googleMap.setMaxZoomPreference(20.0f);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15.0f));
            }
            Log.d("APP", "Lat: " + latitude + "Lng: " + longitude + "Loc: " + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void moveToCurrentLocationWithMarker () {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Geocoder geocoder = new Geocoder(mContext);
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String result = addresses.get(0).getLocality() + " : ";
            result += addresses.get(0).getCountryName();
            LatLng latlng = new LatLng(latitude, longitude);
            if (marker != null) {
                marker.remove();
                marker = googleMap.addMarker(new MarkerOptions().position(latlng).title(result));
                googleMap.setMaxZoomPreference(25.0f);
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
            } else {
                marker = googleMap.addMarker(new MarkerOptions().position(latlng).title(result));
                googleMap.setMaxZoomPreference(25.0f);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15.0f));
            }
            Log.d("APP", "Lat: " + latitude + "Lng: " + longitude + "Loc: " + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                location = locationResult.getLastLocation();
                moveToCurrentLocationWithMarker();
            }
        };
    }

    public void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(TIME_BETWEEN_UPDATES);
        mLocationRequest.setFastestInterval(MIN_TIME_BETWEEN_UPDATES);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    @Override
    public void onProviderEnabled(String provider) {

    }
    @Override
    public void onProviderDisabled(String provider) {

    }
}
