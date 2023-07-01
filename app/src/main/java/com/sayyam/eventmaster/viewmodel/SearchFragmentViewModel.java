package com.sayyam.eventmaster.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.sayyam.eventmaster.network.ObjectModel;
import com.sayyam.eventmaster.repository.GetMainEventsRepository;

public class SearchFragmentViewModel extends ViewModel {
    private final GetMainEventsRepository getMainEventsRepository;

    public SearchFragmentViewModel() {
        super();
        getMainEventsRepository = new GetMainEventsRepository();
    }

    public LiveData<ObjectModel> getSuggestions(String keyword) {
        return getMainEventsRepository.getSuggestion(keyword);
    }

    public LiveData<ObjectModel> getLatLong(String location) {
        return getMainEventsRepository.getLatLong(location);
    }
}
