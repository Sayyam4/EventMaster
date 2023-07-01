package com.sayyam.eventmaster.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sayyam.eventmaster.databinding.ArtistItemBinding;
import com.sayyam.eventmaster.model.ArtistModel;

import java.util.List;

public class ArtistViewAdapter extends RecyclerView.Adapter<ArtistViewAdapter.ViewHolder> {

    private final List<ArtistModel> mValues;

    private final ItemClicks itemClicks;

    public ArtistViewAdapter(List<ArtistModel> items, ItemClicks itemClicks) {
        this.mValues = items;
        this.itemClicks = itemClicks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ArtistItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ArtistItemBinding binding = holder.artistItemBinding;
        ArtistModel model = mValues.get(position);
        binding.title.setText(model.artist.name);
        binding.details.setText(formatFollowers(model.artist.followers.total));
        Glide.with(binding.logo.getContext()).load(model.artist.images.get(0).url).into(binding.logo);
//        Picasso.get().load(model.artist.images.get(0).url).into(binding.logo);
        binding.popularityTV.setText(model.artist.popularity + "");
        binding.popularity.setProgress(model.artist.popularity);
        Glide.with(binding.album1.getContext()).load(model.albums.get(0).images.get(0).url).into(binding.album1);
        Glide.with(binding.album2.getContext()).load(model.albums.get(1).images.get(0).url).into(binding.album2);
        Glide.with(binding.album3.getContext()).load(model.albums.get(2).images.get(0).url).into(binding.album3);
//        Picasso.get().load(model.albums.get(0).images.get(0).url).into(binding.album1);
//        Picasso.get().load(model.albums.get(1).images.get(0).url).into(binding.album2);
//        Picasso.get().load(model.albums.get(2).images.get(0).url).into(binding.album3);
        binding.spotify.setPaintFlags(binding.spotify.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.spotify.setOnClickListener(v -> itemClicks.spotifyClick(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ArtistItemBinding artistItemBinding;

        public ViewHolder(ArtistItemBinding binding) {
            super(binding.getRoot());
            artistItemBinding = binding;
        }
    }


    public static String formatFollowers(int number) {
        String suffix = "";
        float num = 0;

        if (number >= 1000000) {
            suffix = "M";
            num = number / 1000000.0f;
        } else if (number >= 1000) {
            suffix = "K";
            num = number / 1000.0f;
        } else {
            return String.valueOf(number);
        }

        String formatted = "" + Math.floor(num);
        if (formatted.endsWith(".0")) {
            formatted = formatted.substring(0, formatted.length() - 2);
        }
        return formatted + suffix + " Followers";
    }

    public interface ItemClicks {
        void spotifyClick(int position);
    }
}