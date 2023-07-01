package com.sayyam.eventmaster.fragments;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.sayyam.eventmaster.R;
import com.sayyam.eventmaster.databinding.FragmentDetailsBinding;
import com.sayyam.eventmaster.model.MainEventModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailsFragment extends Fragment {


    private String mParam1;

    private FragmentDetailsBinding binding;

    public static String KEY = "event";

    Gson gson;

    MainEventModel.Event event = null;

    public DetailsFragment() {

    }

    public static DetailsFragment newInstance(String param1) {
        DetailsFragment fragment = new DetailsFragment();
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
        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        gson = new Gson();
        event = gson.fromJson(mParam1, MainEventModel.Event.class);
        setTitle();
        binding.title.setSelected(true);
        binding.venue.setText(event._embedded.venues.get(0).name);
        binding.date.setText(FormatDate(event.dates.start.localDate));
        binding.time.setText(FormatTime(event.dates.start.localTime));
        binding.genre.setText(getGenre(event.classifications));
        binding.genre.setSelected(true);
        if(event.priceRanges!=null && !event.priceRanges.isEmpty()) {
            binding.price.setText(gePriceRange(event.priceRanges.get(0)));
        }else{
            binding.priceLL.setVisibility(View.GONE);
        }
        binding.ticket.setText(getTicketStatus(event.dates.status.code));
        binding.ticket.setBackground(ContextCompat.getDrawable(requireContext(), getBackground(event.dates.status.code)));
        binding.buy.setText(event.url);
        binding.buy.setSelected(true);
        binding.buy.setPaintFlags(binding.buy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.buy.setOnClickListener(v -> openUrl(event.url));
        Glide.with(binding.image.getContext()).load(event.seatmap.staticUrl).into(binding.image);
//        Picasso.get().load(event.seatmap.staticUrl).into(binding.image);
        return binding.getRoot();
    }

    private String getTicketStatus(String code) {
        String status;
        switch (code) {
            case "onsale":
                status = "On Sale";
                break;
            case "offsale":
                status = "Off Sale";
                break;
            case "cancelled":
                status = "Cancelled";
                break;
            default:
                status = code;
                break;
        }
        return status;
    }

    private int getBackground(String code) {
        int background;
        switch (code) {
            case "onsale":
                background = R.drawable.green_circle_drawable;
                break;
            case "offsale":
                background = R.drawable.red_circle_drawable;
                break;
            case "cancelled":
                background = R.drawable.orange_circle_drawable;
                break;
            default:
                background = R.drawable.black_circle_drawable;
                break;
        }
        return background;
    }

    private void setTitle() {
        String title = "";
        List<MainEventModel.Attraction> atts = event._embedded.attractions;
        for (int i = 0; i < atts.size() - 1; i++) {
            title += atts.get(i).name + " | ";
        }
        title += atts.get(atts.size() - 1).name;
        binding.title.setText(title);
    }


    private void openUrl(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    private String gePriceRange(MainEventModel.PriceRange priceRange) {
        return "" + priceRange.min + " - " + priceRange.max + " (" + priceRange.currency + " )";
    }

    private String getGenre(ArrayList<MainEventModel.Classification> classifications) {
        String genre = "";
        MainEventModel.Classification classification = classifications.get(0);
        if (classification.segment != null && !classification.segment.name.equalsIgnoreCase("undefined")) {
            genre += classification.segment.name;
        }
        if (classification.genre != null && !classification.genre.name.equalsIgnoreCase("undefined")) {
            genre += " | " + classification.genre.name;
        }
        if (classification.subGenre != null && !classification.subGenre.name.equalsIgnoreCase("undefined")) {
            genre += " | " + classification.subGenre.name;
        }
        if (classification.type != null && !classification.type.name.equalsIgnoreCase("undefined")) {
            genre += " | " + classification.type.name;
        }
        if (classification.subType != null && !classification.subType.name.equalsIgnoreCase("undefined")) {
            genre += " | " + classification.subType.name;
        }
        return genre;
    }

    public String FormatDate(String date) {
        try {
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat output = new SimpleDateFormat("MMM dd, yyyy");
            Date fDate = input.parse(date);
            return output.format(fDate);
        } catch (Exception e) {
            e.printStackTrace();
            return date;
        }
    }

    public String FormatTime(String time) {
        try {
            SimpleDateFormat input = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("hh:mm a");
            Date fDate = input.parse(time);
            return output.format(fDate);
        } catch (Exception e) {
            e.printStackTrace();
            return time;
        }
    }
}