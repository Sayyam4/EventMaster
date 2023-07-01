package com.sayyam.eventmaster.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sayyam.eventmaster.R;
import com.sayyam.eventmaster.databinding.ActivityEventDetailsBinding;
import com.sayyam.eventmaster.fragments.ArtistsFragment;
import com.sayyam.eventmaster.fragments.DetailsFragment;
import com.sayyam.eventmaster.fragments.VenueFragment;
import com.sayyam.eventmaster.helper.MyPagerAdapter;
import com.sayyam.eventmaster.model.MainEventModel;

import java.util.ArrayList;
import java.util.List;

public class EventDetailsActivity extends AppCompatActivity {

    private ActivityEventDetailsBinding binding;

    public static String KEY = "event";

    Gson gson;
    SharedPreferences mPrefs;

    MainEventModel.Event event = null;
    MenuItem fav, favSel;

    List<MainEventModel.Event> favorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        gson = new Gson();
        event = gson.fromJson(getIntent().getStringExtra(KEY), MainEventModel.Event.class);
        favorites = getFavorites();
        initActionBar();
        initViewPager();
    }

    private void initViewPager() {
        ViewPager2 viewPager = binding.viewpager;
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), getLifecycle());
        adapter.addFragment(DetailsFragment.newInstance(gson.toJson(event)));
        adapter.addFragment(ArtistsFragment.newInstance(gson.toJson(event)));
        adapter.addFragment(VenueFragment.newInstance(gson.toJson(event)));
        viewPager.setAdapter(adapter);
        int[] tabIcons = {
                R.drawable.info_icon,
                R.drawable.artist_icon,
                R.drawable.venue_icon
        };
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(binding.tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText(R.string.detailsC);
            } else if (position == 1) {
                tab.setText(R.string.artistC);
            } else {
                tab.setText(R.string.venueC);
            }
        });
        tabLayoutMediator.attach();
        binding.tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        binding.tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        binding.tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void initActionBar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.green_back_btn);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle(null);
        binding.toolbarTitle.setText(event.name);
        binding.toolbarTitle.setSelected(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dgrey, getTheme())));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.facebook) {
            openUrl("https://www.facebook.com/sharer/sharer.php?u=" + event.url);
            return true;
        } else if (id == R.id.twitter) {
            openUrl("https://twitter.com/intent/tweet?text=" + event.url);
            return true;
        } else if (id == R.id.favorites) {
            fav.setVisible(false);
            favSel.setVisible(true);
            likeClick();
            return true;
        } else if (id == R.id.favoritesSelected) {
            fav.setVisible(true);
            favSel.setVisible(false);
            dislikeClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        fav = menu.findItem(R.id.favorites);
        favSel = menu.findItem(R.id.favoritesSelected);
        if (event.isFavourite) {
            fav.setVisible(false);
            favSel.setVisible(true);
        } else {
            fav.setVisible(true);
            favSel.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void openUrl(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    public void likeClick() {
        event.isFavourite = true;
        addToFavorites(event);
    }

    public void dislikeClick() {
        event.isFavourite = false;
        removeFromFavorites(event);
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
        return gson.fromJson(json, new TypeToken<List<MainEventModel.Event>>() {
        }.getType());
    }

    private void showToast(String msg) {
        if (msg == null) {
            msg = getString(R.string.something_went_wrong);
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}