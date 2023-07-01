package com.sayyam.eventmaster.repository;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.sayyam.eventmaster.network.ApiEndpointInterface;
import com.sayyam.eventmaster.network.ObjectModel;
import com.sayyam.eventmaster.network.RemoteDataSource;
import com.sayyam.eventmaster.response.GMapResponse;
import com.sayyam.eventmaster.response.MainEventResponse;
import com.sayyam.eventmaster.response.SuggestionResponse;

import org.json.JSONObject;

import java.util.List;

import kotlin.io.TextStreamsKt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetMainEventsRepository {
    final MutableLiveData<ObjectModel> getMainEventsResponseLiveData;
    final MutableLiveData<ObjectModel> getLatLongResponseLiveData;
    final MutableLiveData<ObjectModel> getSuggestionResponseLiveData;

    final ApiEndpointInterface authService;

    public GetMainEventsRepository() {
        getMainEventsResponseLiveData = new MutableLiveData<>();
        getLatLongResponseLiveData = new MutableLiveData<>();
        getSuggestionResponseLiveData = new MutableLiveData<>();
        authService = RemoteDataSource.createService(ApiEndpointInterface.class);
    }

    public MutableLiveData<ObjectModel> getSuggestion(String keyword) {
        authService.getSuggestions(keyword).enqueue(new Callback<SuggestionResponse>() {
            @Override
            public void onResponse(Call<SuggestionResponse> call, Response<SuggestionResponse> response) {
                if (response.isSuccessful()) {
                    getSuggestionResponseLiveData.postValue(new ObjectModel(true, response.body(), response.message()));
                } else {
                    try {
                        JSONObject errObj = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                        getSuggestionResponseLiveData.postValue(new ObjectModel(false, null, errObj.optString("message")));
                    } catch (Exception e) {
                        e.printStackTrace();
                        getSuggestionResponseLiveData.postValue(new ObjectModel(false, null, response.message()));
                    }
                }
            }
            @Override
            public void onFailure(Call<SuggestionResponse> call, Throwable t) {
                getSuggestionResponseLiveData.postValue(new ObjectModel(false, null, t.getMessage()));
            }
        });
        return getSuggestionResponseLiveData;
    }

    public MutableLiveData<ObjectModel> getLatLong(String location) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + location + "&key=AIzaSyDVHeZZ5_YxftpcoPiPhHxYQ6vpTxuelCw";
        authService.getLatLong(url).enqueue(new Callback<GMapResponse>() {
            @Override
            public void onResponse(Call<GMapResponse> call, Response<GMapResponse> response) {
                if (response.isSuccessful()) {
                    List<GMapResponse.Results> results = response.body().getResults();
                    if (results.isEmpty()) {
                        getLatLongResponseLiveData.postValue(new ObjectModel(false, null, "Please enter correct location"));
                    } else {
                        String lat = results.get(0).getGeometry().getLocation().getLat();
                        String lon = results.get(0).getGeometry().getLocation().getLng();
                        getLatLongResponseLiveData.postValue(new ObjectModel(true, lat + "," + lon, response.message()));
                    }
                } else {
                    try {
                        JSONObject errObj = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                        getLatLongResponseLiveData.postValue(new ObjectModel(false, null, errObj.optString("message")));
                    } catch (Exception e) {
                        e.printStackTrace();
                        getLatLongResponseLiveData.postValue(new ObjectModel(false, null, response.message()));
                    }
                }
            }

            @Override
            public void onFailure(Call<GMapResponse> call, Throwable t) {
                getLatLongResponseLiveData.postValue(new ObjectModel(false, null, t.getMessage()));
            }
        });
        return getLatLongResponseLiveData;
    }

    public MutableLiveData<ObjectModel> getGetMainEvents(String keyword, String distance, String category, String location) {
        authService.getMainEvents(keyword, distance, category, location).enqueue(new Callback<MainEventResponse>() {
            @Override
            public void onResponse(Call<MainEventResponse> call, Response<MainEventResponse> response) {
                if (response.isSuccessful()) {
                    Gson g = new Gson();
                    getMainEventsResponseLiveData.postValue(new ObjectModel(true, response.body(), response.message()));
                } else {
                    try {
                        JSONObject errObj = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                        getMainEventsResponseLiveData.postValue(new ObjectModel(false, null, errObj.optString("message")));
                    } catch (Exception e) {
                        e.printStackTrace();
                        getMainEventsResponseLiveData.postValue(new ObjectModel(false, null, response.message()));
                    }
                }
            }

            @Override
            public void onFailure(Call<MainEventResponse> call, Throwable t) {
                getMainEventsResponseLiveData.postValue(new ObjectModel(false, null, t.getMessage()));
            }
        });
        return getMainEventsResponseLiveData;
    }
}
