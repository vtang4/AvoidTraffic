package com.example.jemora.avoidtraffic;

import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import butterknife.BindView;
//import butterknife.ButterKnife;

import com.jjoe64.graphview.GraphView;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

public class MainActivity extends AppCompatActivity implements ConnectionCallbacks,  OnConnectionFailedListener,ActivityCompat.OnRequestPermissionsResultCallback {

    private EditText destination;
    private EditText departTime;
    private TextView departTimeTextView;
    private TextView destinationTextView;
    private Button saveButton;
    private TextView tvAddress;
    private TextView tvTime;
    private TextView tvDestination;
    private Button resetButton;
//   private GraphView graphView;

    PlacesAutocompleteTextView mAutocomplete;

    Location mLastLocation;

    double latitude;
    double longitude;

    LocationHelper locationHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        locationHelper = new LocationHelper(this);
        locationHelper.checkpermission();


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

//        departTime.getText().toString();

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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mLastLocation = locationHelper.getLocation();

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
                else{
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

       // }


        // check availability of play services
        if (locationHelper.checkPlayServices()) {

            // Building the GoogleApi client
            locationHelper.buildGoogleApiClient();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.hide_x) {
            mAutocomplete.showClearButton(false);
        }
        if (id == R.id.show_x) {
            mAutocomplete.showClearButton(true);
        }
        return super.onOptionsItemSelected(item);
    }

    public void getAddress(){

        Address locationAddress;

        locationAddress = locationHelper.getAddress(latitude,longitude);

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
//
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

        }else
            showToast("Something went wrong");


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        locationHelper.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationHelper.checkPlayServices();
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("Connection failed:", " ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        mLastLocation=locationHelper.getLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        locationHelper.connectApiClient();
    }


    // Permission check functions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // redirects to utils
        locationHelper.onRequestPermissionsResult(requestCode,permissions,grantResults);

    }

    public void showToast(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }



}
