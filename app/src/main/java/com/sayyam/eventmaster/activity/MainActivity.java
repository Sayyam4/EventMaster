package com.sayyam.eventmaster.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayoutMediator;
import com.sayyam.eventmaster.R;
import com.sayyam.eventmaster.databinding.ActivityMainBinding;
import com.sayyam.eventmaster.fragments.FavouritesFragment;
import com.sayyam.eventmaster.fragments.SearchFragment;
import com.sayyam.eventmaster.helper.MyPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initActionBar();
        initViewPager();
    }

    private void initViewPager() {
        ViewPager2 viewPager = binding.viewpager;
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), getLifecycle());
        adapter.addFragment(new SearchFragment());
        adapter.addFragment(new FavouritesFragment());
        viewPager.setAdapter(adapter);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(binding.tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText(R.string.searchC);
            } else {
                tab.setText(R.string.favoritesC);
            }
        });
        tabLayoutMediator.attach();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initActionBar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("EventFinder");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dgrey, getTheme())));
    }
}