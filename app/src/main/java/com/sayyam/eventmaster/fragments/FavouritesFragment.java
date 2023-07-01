package com.sayyam.eventmaster.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sayyam.eventmaster.R;
import com.sayyam.eventmaster.activity.EventDetailsActivity;
import com.sayyam.eventmaster.adapter.SearchItemViewAdapter;
import com.sayyam.eventmaster.databinding.FragmentFavouritesBinding;
import com.sayyam.eventmaster.model.MainEventModel;

import java.util.ArrayList;
import java.util.List;

public class FavouritesFragment extends Fragment implements SearchItemViewAdapter.ItemClicks {

    private FragmentFavouritesBinding binding;

    SearchItemViewAdapter adapter;
    List<MainEventModel.Event> items;

    Gson gson;
    SharedPreferences mPrefs;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFavouritesBinding.inflate(inflater, container, false);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(requireActivity().getApplicationContext());
        gson = new Gson();
        items = new ArrayList<>();
        items.addAll(getFavorites());
        if(items.isEmpty()){
            binding.error.setVisibility(View.VISIBLE);
        }else{
            binding.error.setVisibility(View.GONE);
        }
        adapter = new SearchItemViewAdapter(items, this);
        binding.list.setAdapter(adapter);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void showToast(String msg) {
        if (msg == null) {
            msg = getString(R.string.something_went_wrong);
        }
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void likeClick(int position) {

    }

    @Override
    public void dislikeClick(int position) {
        removeFromFavorites(items.get(position));
        items.remove(position);
        if(items.isEmpty()){
            binding.error.setVisibility(View.VISIBLE);
        }else{
            binding.error.setVisibility(View.GONE);
        }
        adapter.notifyItemRemoved(position);
        adapter.notifyDataSetChanged();
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

    @Override
    public void itemClick(int position) {
        Intent i = new Intent(getActivity(), EventDetailsActivity.class);
        i.putExtra(EventDetailsActivity.KEY, gson.toJson(items.get(position)));
        startActivity(i);
    }

    @Override
    public void onResume() {
        super.onResume();
        items.clear();
        items.addAll(getFavorites());
        if(items.isEmpty()){
            binding.error.setVisibility(View.VISIBLE);
        }else{
            binding.error.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }
}