package com.example.jemora.avoidtraffic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {

    private EditText destination;
    private EditText departTime;
    private TextView departTimeTextView;
    private TextView destinationTextView;
    private Button saveButton;
//   private GraphView graphView;

    PlacesAutocompleteTextView mAutocomplete;

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
        saveButton = findViewById(R.id.save);

        mAutocomplete.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(final Place place) {
                mAutocomplete.getDetailsFor(place, new DetailsCallback() {
                    @Override
                    public void onSuccess(final PlaceDetails details) {
                        Log.d("test", "details " + details);
//                        mStreet.setText(details.name);
//                        for (AddressComponent component : details.address_components) {
//                            for (AddressComponentType type : component.types) {
//                                switch (type) {
//                                    case STREET_NUMBER:
//                                        break;
//                                    case ROUTE:
//                                        break;
//                                    case NEIGHBORHOOD:
//                                        break;
//                                    case SUBLOCALITY_LEVEL_1:
//                                        break;
//                                    case SUBLOCALITY:
//                                        break;
//                                    case LOCALITY:
//                                        mCity.setText(component.long_name);
//                                        break;
//                                    case ADMINISTRATIVE_AREA_LEVEL_1:
//                                        mState.setText(component.short_name);
//                                        break;
//                                    case ADMINISTRATIVE_AREA_LEVEL_2:
//                                        break;
//                                    case COUNTRY:
//                                        break;
//                                    case POSTAL_CODE:
//                                        mZip.setText(component.long_name);
//                                        break;
//                                    case POLITICAL:
//                                        break;
                    }

                    @Override
                    public void onFailure(final Throwable failure) {
                        Log.d("test", "failure " + failure);
                    }
                });
            }
        });

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





}
