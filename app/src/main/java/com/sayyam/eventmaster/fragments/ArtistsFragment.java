package com.sayyam.eventmaster.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.sayyam.eventmaster.R;
import com.sayyam.eventmaster.adapter.ArtistViewAdapter;
import com.sayyam.eventmaster.databinding.FragmentArtistsBinding;
import com.sayyam.eventmaster.model.ArtistModel;
import com.sayyam.eventmaster.model.MainEventModel;
import com.sayyam.eventmaster.network.ObjectModel;
import com.sayyam.eventmaster.response.ArtistDetailsResponse;
import com.sayyam.eventmaster.viewmodel.ArtistFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

public class ArtistsFragment extends Fragment implements ArtistViewAdapter.ItemClicks {

    private String mParam1;

    private FragmentArtistsBinding binding;

    private ArtistFragmentViewModel artistFragmentViewModel;

    public static String KEY = "event";

    private Observer<ObjectModel> observerArtistDetails;

    Gson gson;

    MainEventModel.Event event = null;

    ArtistViewAdapter adapter;
    List<ArtistModel> items;

    public ArtistsFragment() {
        // Required empty public constructor
    }

    public static ArtistsFragment newInstance(String param1) {
        ArtistsFragment fragment = new ArtistsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentArtistsBinding.inflate(inflater, container, false);
        artistFragmentViewModel = new ViewModelProvider(this).get(ArtistFragmentViewModel.class);
        gson = new Gson();
        event = gson.fromJson(mParam1, MainEventModel.Event.class);
        items = new ArrayList<>();
        adapter = new ArtistViewAdapter(items, this);
        binding.list.setAdapter(adapter);
        initObservers();
        fetchData();
        return binding.getRoot();
    }

    private void initObservers() {
        observerArtistDetails = objectModel -> {
            if (objectModel.isStatus()) {
                List<ArtistModel> data = ((ArtistDetailsResponse) objectModel.getObj()).getData();
                items.clear();
                items.addAll(data);
                if (items.isEmpty()) {
                    binding.error.setVisibility(View.VISIBLE);
                } else {
                    binding.error.setVisibility(View.GONE);
                }
                binding.loader.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            } else {
                binding.error.setVisibility(View.VISIBLE);
                showToast(objectModel.getMessage());
            }
        };
    }

    void fetchData() {
        List<String> artists = new ArrayList<>();
        for (MainEventModel.Attraction att : event._embedded.attractions) {
            artists.add(att.name);
        }
        artistFragmentViewModel.getArtistDetails(artists).observe(getViewLifecycleOwner(), observerArtistDetails);
    }

    private void showToast(String msg) {
        if (msg == null) {
            msg = getString(R.string.something_went_wrong);
        }
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void spotifyClick(int position) {
        openUrl(items.get(position).artist.external_urls.spotify);
    }


    private void openUrl(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}