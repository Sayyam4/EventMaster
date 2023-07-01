package com.sayyam.eventmaster.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sayyam.eventmaster.R;
import com.sayyam.eventmaster.databinding.SearchItemBinding;
import com.sayyam.eventmaster.model.MainEventModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SearchItemViewAdapter extends RecyclerView.Adapter<SearchItemViewAdapter.ViewHolder> {

    private final List<MainEventModel.Event> mValues;

    private final ItemClicks itemClicks;

    public SearchItemViewAdapter(List<MainEventModel.Event> items, ItemClicks itemClicks) {
        this.mValues = items;
        this.itemClicks = itemClicks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(SearchItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        SearchItemBinding binding = holder.searchItemBinding;
        MainEventModel.Event model = mValues.get(position);
        binding.title.setText(model.name);
        binding.title.setSelected(true);
        binding.venue.setText(model._embedded.venues.get(0).name);
        binding.venue.setSelected(true);
        binding.date.setText(FormatDate(model.dates.start.localDate));
        binding.time.setText(FormatTime(model.dates.start.localTime));
        binding.genre.setText(model.classifications.get(0).segment.name);
        Glide.with(binding.logo.getContext()).load(model.images.get(0).url).into(binding.logo);
//        Picasso.get().load(model.images.get(0).url).into(binding.logo);
        if(model.isFavourite){
            binding.like.setImageDrawable(binding.like.getContext().getDrawable(R.drawable.heart_filled));
        }else{
            binding.like.setImageDrawable(binding.like.getContext().getDrawable(R.drawable.heart_outline));
        }
        binding.like.setOnClickListener(v -> {
            if(!model.isFavourite){
                itemClicks.likeClick(position);
            }else{
                itemClicks.dislikeClick(position);
            }
        });
        binding.getRoot().setOnClickListener(v -> {
            itemClicks.itemClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private SearchItemBinding searchItemBinding;

        public ViewHolder(SearchItemBinding binding) {
            super(binding.getRoot());
            searchItemBinding = binding;
        }
    }

    public String FormatDate(String date) {
        try {
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat output = new SimpleDateFormat("MM/dd/yyyy");
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

    public interface ItemClicks {
        void likeClick(int position);

        void dislikeClick(int position);

        void itemClick(int position);
    }
}
