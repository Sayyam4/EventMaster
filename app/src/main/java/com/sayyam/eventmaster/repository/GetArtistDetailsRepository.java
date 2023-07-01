package com.sayyam.eventmaster.repository;

import androidx.lifecycle.MutableLiveData;

import com.sayyam.eventmaster.network.ApiEndpointInterface;
import com.sayyam.eventmaster.network.ObjectModel;
import com.sayyam.eventmaster.network.RemoteDataSource;
import com.sayyam.eventmaster.response.ArtistDetailsResponse;

import org.json.JSONObject;

import kotlin.io.TextStreamsKt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetArtistDetailsRepository {
    final MutableLiveData<ObjectModel> getArtistDetailsResponseLiveData;
    final ApiEndpointInterface authService;

    public GetArtistDetailsRepository() {
        getArtistDetailsResponseLiveData = new MutableLiveData<>();
        authService = RemoteDataSource.createService(ApiEndpointInterface.class);
    }

    public MutableLiveData<ObjectModel> getArtistDetails(String artists) {
        authService.getArtistDetails(artists).enqueue(new Callback<ArtistDetailsResponse>() {
            @Override
            public void onResponse(Call<ArtistDetailsResponse> call, Response<ArtistDetailsResponse> response) {
                if (response.isSuccessful()) {
                    getArtistDetailsResponseLiveData.postValue(new ObjectModel(true, response.body(), response.message()));
                } else {
                    try {
                        JSONObject errObj = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                        getArtistDetailsResponseLiveData.postValue(new ObjectModel(false, null, errObj.optString("message")));
                    } catch (Exception e) {
                        e.printStackTrace();
                        getArtistDetailsResponseLiveData.postValue(new ObjectModel(false, null, response.message()));
                    }
                }
            }

            @Override
            public void onFailure(Call<ArtistDetailsResponse> call, Throwable t) {
                getArtistDetailsResponseLiveData.postValue(new ObjectModel(false, null, t.getMessage()));
            }
        });
        return getArtistDetailsResponseLiveData;
    }
}