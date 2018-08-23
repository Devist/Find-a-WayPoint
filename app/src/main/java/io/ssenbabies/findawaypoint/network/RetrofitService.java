package io.ssenbabies.findawaypoint.network;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("users/{user}/repos")
    Call<ArrayList<JsonObject>> getListRepos(@Path("user") String id, @Path("get") String bw);

    //주변 역 정보 출력
    @GET("/place/nearby-station")
    Call<JsonObject> getStationData(
            @Query("latitude") double latitude,
            @Query("longtitude") double longtitude
    );

    //주변 정보 출력
    @GET("/place/category/{categoryNumber}/{sortMethod}")
    Call<JsonObject> getPlaceData(
            @Path("categoryNumber") int categoryNumber,
            @Path("sortMethod") int sortMethod,
            @Query("latitude") double latitude,
            @Query("longtitude") double longtitude
    );
}