package com.sayyam.eventmaster.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.sayyam.eventmaster.R;
import com.sayyam.eventmaster.adapter.KeywordSearchAdapter;
import com.sayyam.eventmaster.databinding.FragmentSearchBinding;
import com.sayyam.eventmaster.network.ObjectModel;
import com.sayyam.eventmaster.response.SuggestionResponse;
import com.sayyam.eventmaster.viewmodel.SearchFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private SearchFragmentViewModel searchFragmentViewModel;
    private AutoCompleteTextView keyword2;
    private String selectedValue;

    private Observer<ObjectModel> observerLatLon, observerSuggestions;

    List<String> suggestions = new ArrayList<>();
    String keyword = "", distance = "", category = "", latlon = "";
    FusedLocationProviderClient mFusedLocationClient;
    private static final int REQUEST_LOCATION = 1;

    ArrayAdapter adapter;

    TextWatcher textWatcher;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        searchFragmentViewModel = new ViewModelProvider(this).get(SearchFragmentViewModel.class);
        final View currentView = inflater.inflate(R.layout.fragment_search, container, false);
        keyword2 = currentView.findViewById(R.id.keyword2);
        keyword2.setDropDownBackgroundResource(R.color.black);
        binding.keyword2.setAdapter(new KeywordSearchAdapter(requireContext(), R.layout.my_list_item));

        keyword2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedValue = (String) parent.getItemAtPosition(position);
            }
        });

        init();
        initObservers();
        initSpinner();
        initListeners();
        return binding.getRoot();
    }

    private void init() {
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchFragmentViewModel.getSuggestions(s.toString()).observe(getViewLifecycleOwner(), observerSuggestions);
            }
        };
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        adapter = new ArrayAdapter<>(requireContext(), R.layout.suggestion_item, suggestions);
        binding.suggestionList.setAdapter(adapter);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initObservers() {
        observerLatLon = objectModel -> {
            if (objectModel.isStatus()) {
                latlon = objectModel.getObj().toString();
                submitForm(latlon);
            } else {
                showToast(objectModel.getMessage());
            }
        };
        observerSuggestions = objectModel -> {
            if (objectModel.isStatus()) {
                List<SuggestionResponse.Data> sugg = ((SuggestionResponse) objectModel.getObj()).getData();
                suggestions.clear();
                for (SuggestionResponse.Data s : sugg) {
                    suggestions.add(s.getName());
                }
                adapter.notifyDataSetChanged();
            }
        };
    }

    private void initListeners() {
        binding.suggestionList.setOnItemClickListener((parent, view, position, id) -> {
            //binding.keyword.removeTextChangedListener(textWatcher);
            //binding.keyword.setText(suggestions.get(position));
            //binding.keyword.setSelection(binding.keyword.getText().length());
            //binding.keyword.addTextChangedListener(textWatcher);
            suggestions.clear();
            adapter.notifyDataSetChanged();
        });
        // binding.keyword.addTextChangedListener(textWatcher);
        binding.locationToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.location.setVisibility(View.GONE);
                binding.location.setText("");
                getLocation();
            } else {
                latlon = "";
                binding.location.setVisibility(View.VISIBLE);
            }
        });
        binding.submit.setOnClickListener(v -> validateForm());
        binding.clear.setOnClickListener(v -> clearForm());
    }

    private void validateForm() {
        keyword = binding.keyword2.getText().toString();
        distance = binding.distance.getText().toString();
        if (keyword.trim().isEmpty() || distance.trim().isEmpty()) {
            showToast(getText(R.string.error_message).toString());
            return;
        }
        category = binding.categorySpinner.getSelectedItem().toString();
        if (binding.locationToggle.isChecked()) {
            if (latlon.trim().isEmpty()) {
                showToast(getText(R.string.fetching_locatino).toString());
            } else {
                submitForm(latlon);
            }
        } else {
            String location = binding.location.getText().toString();
            if (location.trim().isEmpty()) {
                showToast(getText(R.string.error_message).toString());
            } else {
                searchFragmentViewModel.getLatLong(location).observe(getViewLifecycleOwner(), observerLatLon);
            }
        }
    }

    private void submitForm(String latlng) {
        Bundle bundle = new Bundle();
        bundle.putString(SearchResultsFragment.KEYWORD2, keyword);
        bundle.putString(SearchResultsFragment.DISTANCE, distance);
        bundle.putString(SearchResultsFragment.CATEGORY, category);
        bundle.putString(SearchResultsFragment.LATLNG, latlng);

        SearchResultsFragment searchResultsFragment = new SearchResultsFragment();
        searchResultsFragment.setArguments(bundle);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .add(R.id.container, searchResultsFragment)
                .addToBackStack(null)
                .commit();
    }

    private void clearForm() {
        suggestions.clear();
        adapter.notifyDataSetChanged();
        binding.keyword2.setText("");
        binding.distance.setText("10");
        binding.categorySpinner.setSelection(0);
        binding.locationToggle.setChecked(false);
        binding.location.setText("");
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.category_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        binding.categorySpinner.setAdapter(adapter);
    }

    private void showToast(String msg) {
        if (msg == null) {
            msg = getString(R.string.something_went_wrong);
        }
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                binding.locationToggle.setChecked(true);
                mFusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
                    @NonNull
                    @Override
                    public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                        return new CancellationTokenSource().getToken();
                    }

                    @Override
                    public boolean isCancellationRequested() {
                        return false;
                    }
                }).addOnSuccessListener(location -> {
                    if (location == null)
                        showToast(null);
                    else {
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();
                        latlon = lat + "," + lon;
                    }
                });
            } else {
                binding.locationToggle.setChecked(false);
                showToast(getString(R.string.location_error));
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            binding.locationToggle.setChecked(false);
            requestPermissions();
        }
    }

    private boolean checkPermissions() {
        return requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        requestPermissions(new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onResume() {
        super.onResume();
        for (Fragment fragment : requireActivity().getSupportFragmentManager().getFragments()) {
            if (fragment instanceof ParentFragmentCallback) {
                ((ParentFragmentCallback) fragment).onResumeFromParent();
            }
        }
    }


    public interface ParentFragmentCallback {
        void onResumeFromParent();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}