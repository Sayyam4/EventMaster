package com.sayyam.eventmaster.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sayyam.eventmaster.R;
import com.sayyam.eventmaster.activity.EventDetailsActivity;
import com.sayyam.eventmaster.adapter.SearchItemViewAdapter;
import com.sayyam.eventmaster.databinding.FragmentSearchResultsBinding;
import com.sayyam.eventmaster.model.MainEventModel;
import com.sayyam.eventmaster.network.ObjectModel;
import com.sayyam.eventmaster.response.MainEventResponse;
import com.sayyam.eventmaster.viewmodel.SearchResultFragmentViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchResultsFragment extends Fragment implements SearchItemViewAdapter.ItemClicks, SearchFragment.ParentFragmentCallback {
    private FragmentSearchResultsBinding binding;

    private SearchResultFragmentViewModel searchResultFragmentViewModel;

    public static String KEYWORD2 = "keyword2";
    public static String DISTANCE = "distance";
    public static String CATEGORY = "category";
    public static String LATLNG = "latlng";

    String keyword = "", distance = "", category = "", latlon = "";

    private Observer<ObjectModel> observerEvents;

    SearchItemViewAdapter adapter;
    List<MainEventModel.Event> items;

    Gson gson;
    SharedPreferences mPrefs;

    public SearchResultsFragment() {
    }

    public static SearchResultsFragment newInstance() {
        SearchResultsFragment fragment = new SearchResultsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            keyword = getArguments().getString(KEYWORD2);
            distance = getArguments().getString(DISTANCE);
            category = getArguments().getString(CATEGORY);
            latlon = getArguments().getString(LATLNG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchResultsBinding.inflate(inflater, container, false);
        searchResultFragmentViewModel = new ViewModelProvider(this).get(SearchResultFragmentViewModel.class);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(requireActivity().getApplicationContext());
        gson = new Gson();
        items = new ArrayList<>();
        adapter = new SearchItemViewAdapter(items, this);
        binding.list.setAdapter(adapter);
        observerEvents = objectModel -> {
            if (objectModel.isStatus()) {
                items.clear();
                try {
                    items.addAll(((MainEventResponse) objectModel.getObj()).getData()._embedded.events);
                    Set<String> favIds = getFavoriteIds();
                    for (int i = 0; i < items.size(); i++) {
                        items.get(i).isFavourite = favIds.contains(items.get(i).id);
                    }
                } catch (Exception e) {
                    binding.error.setVisibility(View.VISIBLE);
                }
                binding.loader.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            } else {
                binding.error.setVisibility(View.VISIBLE);
                showToast(objectModel.getMessage());
            }
        };
        binding.back.setOnClickListener(v -> popFragment());
        searchResultFragmentViewModel.getMainEvents(keyword, distance, category, latlon).observe(getViewLifecycleOwner(), observerEvents);
        return binding.getRoot();
    }

    private void popFragment() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void showToast(String msg) {
        if (msg == null) {
            msg = getString(R.string.something_went_wrong);
        }
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void likeClick(int position) {
        items.get(position).isFavourite = true;
        addToFavorites(items.get(position));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void dislikeClick(int position) {
        items.get(position).isFavourite = false;
        removeFromFavorites(items.get(position));
        adapter.notifyDataSetChanged();
    }

    void addToFavorites(MainEventModel.Event event) {
        List<MainEventModel.Event> events = getFavorites();
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        events.add(event);
        String json = gson.toJson(events);
        prefsEditor.putString("Favorites", json);
        prefsEditor.commit();
        showToast(event.name + " added to favorites");
    }

    void removeFromFavorites(MainEventModel.Event event) {
        List<MainEventModel.Event> events = getFavorites();
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).id.equals(event.id)) {
                events.remove(i);
                break;
            }
        }
        String json = gson.toJson(events);
        prefsEditor.putString("Favorites", json);
        prefsEditor.commit();
        showToast(event.name + " removed from favorites");
    }

    List<MainEventModel.Event> getFavorites() {
        List<MainEventModel.Event> empty = new ArrayList<>();
        String json = mPrefs.getString("Favorites", gson.toJson(empty));
        return gson.fromJson(json, new TypeToken<List<MainEventModel.Event>>() {}.getType());
    }

    Set<String> getFavoriteIds() {
        List<MainEventModel.Event> favs = getFavorites();
        List<String> ids = new ArrayList<>();
        for (MainEventModel.Event e : favs) {
            ids.add(e.id);
        }
        return new HashSet<>(ids);
    }

    @Override
    public void itemClick(int position) {
        Intent i = new Intent(getActivity(), EventDetailsActivity.class);
        i.putExtra(EventDetailsActivity.KEY, gson.toJson(items.get(position)));
        startActivity(i);
    }

    @Override
    public void onResume() {
        super.onResume();
        Set<String> favIds = getFavoriteIds();
        for (int i = 0; i < items.size(); i++) {
            items.get(i).isFavourite = favIds.contains(items.get(i).id);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResumeFromParent() {
        onResume();
    }
}