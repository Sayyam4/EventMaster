package com.sayyam.eventmaster.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.sayyam.eventmaster.network.ObjectModel;
import com.sayyam.eventmaster.repository.GetArtistDetailsRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ArtistFragmentViewModel extends ViewModel {
    private final GetArtistDetailsRepository getArtistDetailsRepository;

    public ArtistFragmentViewModel() {
        super();
        getArtistDetailsRepository = new GetArtistDetailsRepository();
    }

    public LiveData<ObjectModel> getArtistDetails(List<String> artists) {
        return getArtistDetailsRepository.getArtistDetails("[" + artists.stream().map(name -> ("\"" + name + "\"")).collect(Collectors.joining(",")) + "]");

    }
}