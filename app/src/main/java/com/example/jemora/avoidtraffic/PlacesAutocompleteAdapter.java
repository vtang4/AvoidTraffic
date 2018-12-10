package com.example.jemora.avoidtraffic;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seatgeek.placesautocomplete.PlacesApi;
import com.seatgeek.placesautocomplete.adapter.AbstractPlacesAutocompleteAdapter;
import com.seatgeek.placesautocomplete.history.AutocompleteHistoryManager;
import com.seatgeek.placesautocomplete.model.AutocompleteResultType;
import com.seatgeek.placesautocomplete.model.Place;

public class PlacesAutocompleteAdapter extends AbstractPlacesAutocompleteAdapter {


    public PlacesAutocompleteAdapter(final Context context,
                                     final PlacesApi api,
                                     final AutocompleteResultType autocompleteResultType,
                                     final AutocompleteHistoryManager history) {
        super(context, api, autocompleteResultType, history);
    }

    @Override
    protected View newView(ViewGroup parent) {
        Context context = parent.getContext();
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.autocomplete_items,parent,false);

        return view;
    }

    @Override
    protected void bindView(final View view, final Place item) {

        ( (TextView) view).setText(item.description);

    }
}
