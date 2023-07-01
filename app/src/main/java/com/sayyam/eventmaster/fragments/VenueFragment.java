package com.sayyam.eventmaster.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.sayyam.eventmaster.R;
import com.sayyam.eventmaster.databinding.FragmentVenueBinding;
import com.sayyam.eventmaster.helper.WorkAroundMapFragment;
import com.sayyam.eventmaster.model.MainEventModel;

public class VenueFragment extends Fragment implements OnMapReadyCallback {
    private String mParam1;

    private FragmentVenueBinding binding;

    public static String KEY = "event";

    Gson gson;

    MainEventModel.Event event = null;

    GoogleMap mMap;
    Marker marker;

    public VenueFragment() {
        // Required empty public constructor
    }

    public static VenueFragment newInstance(String param1) {
        VenueFragment fragment = new VenueFragment();
        Bundle args = new Bundle();
        args.putString(KEY, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVenueBinding.inflate(inflater, container, false);
        gson = new Gson();
        event = gson.fromJson(mParam1, MainEventModel.Event.class);
        binding.title.setText(event._embedded.venues.get(0).name);
        binding.title.setSelected(true);
        if (event._embedded.venues.get(0).address != null && event._embedded.venues.get(0).address.line1 != null && !event._embedded.venues.get(0).address.line1.isEmpty()) {
            binding.address.setText(event._embedded.venues.get(0).address.line1);
            binding.address.setSelected(true);
        } else {
            binding.addressLL.setVisibility(View.GONE);
        }
        if (event._embedded.venues.get(0).city != null && event._embedded.venues.get(0).city.name != null && !event._embedded.venues.get(0).city.name.isEmpty()
                && event._embedded.venues.get(0).state != null && event._embedded.venues.get(0).state.name != null && !event._embedded.venues.get(0).state.name.isEmpty()) {
            binding.city.setText(event._embedded.venues.get(0).city.name + ", " + event._embedded.venues.get(0).state.name);
            binding.city.setSelected(true);
        } else if (event._embedded.venues.get(0).city != null && event._embedded.venues.get(0).city.name != null && !event._embedded.venues.get(0).city.name.isEmpty()) {
            binding.city.setText(event._embedded.venues.get(0).city.name);
            binding.city.setSelected(true);
        } else if (event._embedded.venues.get(0).state != null && event._embedded.venues.get(0).state.name != null && !event._embedded.venues.get(0).state.name.isEmpty()) {
            binding.city.setText(event._embedded.venues.get(0).state.name);
            binding.city.setSelected(true);
        } else {
            binding.cityLL.setVisibility(View.GONE);
        }
        if (event._embedded.venues.get(0).boxOfficeInfo != null && event._embedded.venues.get(0).boxOfficeInfo.phoneNumberDetail != null && !event._embedded.venues.get(0).boxOfficeInfo.phoneNumberDetail.isEmpty()) {
            binding.contact.setText(event._embedded.venues.get(0).boxOfficeInfo.phoneNumberDetail);
            binding.contact.setSelected(true);
        } else {
            binding.contactLL.setVisibility(View.GONE);
        }
        WorkAroundMapFragment mapFragment = (WorkAroundMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        boolean showYellowCard = false;
        if (event._embedded.venues.get(0).boxOfficeInfo != null && event._embedded.venues.get(0).boxOfficeInfo.openHoursDetail != null && !event._embedded.venues.get(0).boxOfficeInfo.openHoursDetail.isEmpty()) {
            binding.open.setText(event._embedded.venues.get(0).boxOfficeInfo.openHoursDetail);
            showYellowCard = true;
        } else {
            binding.openHoursLL.setVisibility(View.GONE);
            binding.open.setVisibility(View.GONE);
        }
        binding.open.setOnClickListener(v -> {
            if (binding.open.getMaxLines() != Integer.MAX_VALUE)
                binding.open.setMaxLines(Integer.MAX_VALUE);
            else
                binding.open.setMaxLines(3);
        });
        if (event._embedded.venues.get(0).generalInfo != null && event._embedded.venues.get(0).generalInfo.generalRule != null && !event._embedded.venues.get(0).generalInfo.generalRule.isEmpty()) {
            binding.rules.setText(event._embedded.venues.get(0).generalInfo.generalRule);
            showYellowCard = true;
        } else {
            binding.ruleLL.setVisibility(View.GONE);
            binding.rules.setVisibility(View.GONE);
        }
        binding.rules.setOnClickListener(v -> {
            if (binding.rules.getMaxLines() != Integer.MAX_VALUE)
                binding.rules.setMaxLines(Integer.MAX_VALUE);
            else
                binding.rules.setMaxLines(3);
        });
        if (event._embedded.venues.get(0).generalInfo != null && event._embedded.venues.get(0).generalInfo.childRule != null && !event._embedded.venues.get(0).generalInfo.childRule.isEmpty()) {
            binding.childRules.setText(event._embedded.venues.get(0).generalInfo.childRule);
            showYellowCard = true;
        } else {
            binding.childRulesLL.setVisibility(View.GONE);
            binding.childRules.setVisibility(View.GONE);
        }
        binding.childRules.setOnClickListener(v -> {
            if (binding.childRules.getMaxLines() != Integer.MAX_VALUE)
                binding.childRules.setMaxLines(Integer.MAX_VALUE);
            else
                binding.childRules.setMaxLines(3);
        });
        if (showYellowCard) {
            binding.yellowCard.setVisibility(View.VISIBLE);
        } else {
            binding.yellowCard.setVisibility(View.GONE);
        }
        return binding.getRoot();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        NestedScrollView parentSV = binding.parentSV;
        ((WorkAroundMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).setListener(() -> parentSV.requestDisallowInterceptTouchEvent(true));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (marker != null)
            marker.remove();
        LatLng loc = new LatLng(Double.parseDouble(event._embedded.venues.get(0).location.latitude), Double.parseDouble(event._embedded.venues.get(0).location.longitude));
        marker = mMap.addMarker(new MarkerOptions().position(loc).title("Event Location"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 12.0f));
        mMap.setOnMapClickListener(latLng -> {
            String geoUri = "http://maps.google.com/maps?q=loc:" + event._embedded.venues.get(0).location.latitude + "," + event._embedded.venues.get(0).location.longitude;
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
            startActivity(mapIntent);
        });
        mMap.setOnMarkerClickListener(marker -> {
            String geoUri = "http://maps.google.com/maps?q=loc:" + event._embedded.venues.get(0).location.latitude + "," + event._embedded.venues.get(0).location.longitude;
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
            startActivity(mapIntent);
            return false;
        });
    }
}