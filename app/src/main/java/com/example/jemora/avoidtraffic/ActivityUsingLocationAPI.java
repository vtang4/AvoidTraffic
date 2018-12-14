package com.example.jemora.avoidtraffic;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ActivityUsingLocationAPI extends AppCompatActivity implements ConnectionCallbacks,
        OnConnectionFailedListener,OnRequestPermissionsResultCallback,
        PermissionUtils.PermissionResultCallback {

    private EditText destination;
    private EditText departTime;
    private TextView departTimeTextView;
    private TextView destinationTextView;
    private TextView tvTime;
    private TextView tvDestination;


    private Button saveButton;
    private TextView tvAddress;
    private Button resetButton;


//   private GraphView graphView;

    PlacesAutocompleteTextView mAutocomplete;

    // LogCat tag
    private static final String TAG = MainActivity.class.getSimpleName();

    private final static int PLAY_SERVICES_REQUEST = 1000;
    private final static int REQUEST_CHECK_SETTINGS = 2000;

    Location mLastLocation;

    // Google client to interact with Google API

    private GoogleApiClient mGoogleApiClient;


    double latitude;
    double longitude;

   ArrayList<String> permissions = new ArrayList<>();
   PermissionUtils permissionUtils;

   boolean isGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);



        mAutocomplete = findViewById(R.id.autocomplete);
        departTimeTextView = findViewById(R.id.depart_time);
        departTime = findViewById(R.id.depart_time_edit_text);
//              destination = findViewById(R.id.destination_edit_text);
        destinationTextView = findViewById(R.id.destination);
        //       graphView = findViewById(R.id.graph);

        tvAddress = findViewById(R.id.tvAddress);
        tvDestination =findViewById(R.id.tvDestination);
        tvTime = findViewById(R.id.tvTime);


        saveButton = findViewById(R.id.save);
        resetButton = findViewById(R.id.reset);

        mAutocomplete.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(final Place place) {
                mAutocomplete.getDetailsFor(place, new DetailsCallback() {
                    @Override
                    public void onSuccess(final PlaceDetails details) {
                        Log.d("test", "details " + details);

                    }

                    @Override
                    public void onFailure(final Throwable failure) {
                        Log.d("test", "failure " + failure);
                    }
                });
            }
        });



        permissionUtils = new PermissionUtils(this);

        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);

        permissionUtils.check_permission(permissions, "Need GPS permission for getting your location",1);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();

                if(mLastLocation != null){
                    latitude = mLastLocation.getLatitude();
                    longitude = mLastLocation.getLongitude();
                    getAddress();

                    tvDestination.setText(mAutocomplete.getText().toString());
                    tvTime.setText(departTime.getText().toString());


                    mAutocomplete.setVisibility(View.GONE);
                    departTime.setVisibility(View.GONE);

                    tvDestination.setVisibility(View.VISIBLE);
                    tvTime.setVisibility(View.VISIBLE);


                }
                else
                {
                    showToast("Couldn't get the location. Make sure location is enabled on the device");
                }

            }
        });


            resetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    departTime.setText(" ");
                    mAutocomplete.setText(" ");

                    tvTime.setText("");
                    tvDestination.setText("");
                    tvAddress.setText("");

                    tvTime.setVisibility(View.GONE);
                    tvDestination.setVisibility(View.GONE);
                    tvAddress.setVisibility(View.GONE);

                    mAutocomplete.setVisibility(View.VISIBLE);
                    departTime.setVisibility(View.VISIBLE);


                }
            });



        // check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        }

    }


    private void getLocation(){

        if(isGranted){

            try{
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            }
            catch (SecurityException e){
                e.printStackTrace();
            }


        }

    }

    public Address getAddress(double latitude, double longitude){

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try{
            addresses = geocoder.getFromLocation(latitude,longitude,1);
            return addresses.get(0);

        }catch (IOException e){
            e.printStackTrace();
        }
        return null;

    }

    public void getAddress(){

        Address locationAddress = getAddress(latitude,longitude);

        if(locationAddress != null){

            String address = locationAddress.getAddressLine(0);
//            String address1 = locationAddress.getAddressLine(1);
//            String city = locationAddress.getLocality();
//            String state = locationAddress.getAdminArea();
//            String country = locationAddress.getCountryName();
//            String postalCode = locationAddress.getPostalCode();

            String currentLocation;

            if(!TextUtils.isEmpty(address)) {
                currentLocation = address;

//                if (!TextUtils.isEmpty(address1))
//                    currentLocation += "\n" + address1;
//
//                if (!TextUtils.isEmpty(city)) {
//                    currentLocation += "\n" + city;
//
//                    if (!TextUtils.isEmpty(postalCode))
//                        currentLocation += " - " + postalCode;
//                } else {
//                    if (!TextUtils.isEmpty(postalCode))
//                        currentLocation += "\n" + postalCode;
//                }
//
//                if (!TextUtils.isEmpty(state))
//                    currentLocation += "\n" + state;
//
//                if (!TextUtils.isEmpty(country))
//                    currentLocation += "\n" + country;




                tvAddress.setText(currentLocation);
                tvAddress.setVisibility(View.VISIBLE);


            }

            }


    }

    protected  synchronized  void buildGoogleApiClient(){

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {

                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location requests here
                        getLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(ActivityUsingLocationAPI.this, REQUEST_CHECK_SETTINGS);

                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });


    }


    /**
     * Method to verify google play services on the device
     * */

    private boolean checkPlayServices() {

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this,resultCode,
                        PLAY_SERVICES_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        getLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        break;
                    default:
                        break;
                }
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        getLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }


    // Permission check functions


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // redirects to utils
        permissionUtils.onRequestPermissionsResult(requestCode,permissions,grantResults);

    }




    @Override
    public void PermissionGranted(int request_code) {
        Log.i("PERMISSION","GRANTED");
        isGranted=true;
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
        Log.i("PERMISSION PARTIALLY","GRANTED");
    }

    @Override
    public void PermissionDenied(int request_code) {
        Log.i("PERMISSION","DENIED");
    }

    @Override
    public void NeverAskAgain(int request_code) {
        Log.i("PERMISSION","NEVER ASK AGAIN");
    }

    public void showToast(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }



}
