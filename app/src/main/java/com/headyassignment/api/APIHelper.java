package com.headyassignment.api;

import com.headyassignment.utils.Response;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIHelper {

    private static final int TIMEOUT = 60;
    private static final String BASE_URL = IApi.BASE_URL_DEV;
    private static final String TAG = "ApiHelper";
    private final IApi iApi;

    public APIHelper() {
        iApi = getRetrofitInstance().create(IApi.class);
    }

    private Retrofit getRetrofitInstance() {

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    /**
     * Get Json
     */
    public void getJson(final ICallback iCallback) {
        iApi.getJson().enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response != null && response.body() != null) {
                    iCallback.onSuccess(response.body());
                } else {
                    iCallback.onFailure(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                iCallback.onFailure(null);
            }
        });
    }

    public interface ICallback {
        void onSuccess(Response body);

        void onFailure(ResponseBody responseBody);
    }
}
