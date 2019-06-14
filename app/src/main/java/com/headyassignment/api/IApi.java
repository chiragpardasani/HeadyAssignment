package com.headyassignment.api;

import com.headyassignment.utils.Response;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IApi {

    //String BASE_URL_LIVE = "";
    String BASE_URL_DEV = "https://stark-spire-93433.herokuapp.com/";
    String API_JSON = "json";

    @GET(API_JSON)
    Call<Response> getJson();
}
