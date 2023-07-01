package com.sayyam.eventmaster.response;

import com.sayyam.eventmaster.model.ArtistModel;

import java.util.List;

public class ArtistDetailsResponse {
    public List<ArtistModel> getData() {
        return data;
    }

    public void setData(List<ArtistModel> data) {
        this.data = data;
    }

    private List<ArtistModel> data;

}