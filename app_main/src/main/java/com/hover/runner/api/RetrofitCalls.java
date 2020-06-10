package com.hover.runner.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCalls {
    private final String auth_for_token_stage = "https://stage.usehover.com/api/";
    private final String auth_for_token_live = "https://www.usehover.com/api/";

    private final String auth_for_key_stage = "https://stage.usehover.com/api/";
    private final String auth_for_key_live = "https://www.usehover.com/api/";

    private Gson gson ;

    public RetrofitCalls() {
        this.gson = new GsonBuilder().setLenient().create();
    }

    public Retrofit getRetrofitToken() {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(auth_for_token_stage)
                .build();
    }

    public Retrofit getRetrofitApi(String token) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Authorization", token);
            Request request = requestBuilder.build();

            return chain.proceed(request);
        });
        OkHttpClient client = httpClient.build();
        return new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(auth_for_key_stage)
                .build();
    }

}
