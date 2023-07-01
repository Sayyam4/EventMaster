package com.sayyam.eventmaster.network;

import com.sayyam.eventmaster.response.ArtistDetailsResponse;
import com.sayyam.eventmaster.response.GMapResponse;
import com.sayyam.eventmaster.response.MainEventResponse;
import com.sayyam.eventmaster.response.SuggestionResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiEndpointInterface {

    @GET
    Call<GMapResponse> getLatLong(@Url String url);

    @GET("/suggest")
    Call<SuggestionResponse> getSuggestions(@Query("keyword") String keyword);

    @GET("/getArtistsDetails")
    Call<ArtistDetailsResponse> getArtistDetails(@Query("names") String artists);

    @GET("/fetchData")
    Call<MainEventResponse> getMainEvents(
            @Query("keyword") String keyword,
            @Query("distance") String distance,
            @Query("category") String category,
            @Query("location") String location);
}