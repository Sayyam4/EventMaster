package com.sayyam.eventmaster.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.sayyam.eventmaster.network.ObjectModel;
import com.sayyam.eventmaster.repository.GetMainEventsRepository;

public class SearchResultFragmentViewModel extends ViewModel {
    private final GetMainEventsRepository getMainEventsRepository;

    public SearchResultFragmentViewModel() {
        super();
        getMainEventsRepository = new GetMainEventsRepository();
    }

    public LiveData<ObjectModel> getMainEvents(String keyword, String distance, String category, String location) {
        return getMainEventsRepository.getGetMainEvents(keyword, distance, category, location);
    }
}
