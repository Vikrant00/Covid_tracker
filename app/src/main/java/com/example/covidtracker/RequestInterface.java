package com.example.covidtracker;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RequestInterface {

    @GET("summary")
    Call<JsonObject> getJson();

    @GET("version")
    Call<JsonObject> getVersion();

}