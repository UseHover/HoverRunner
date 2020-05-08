package com.usehover.runner.interfaces;

import com.usehover.runner.models.ApiKeyModel;
import com.usehover.runner.models.TokenModel;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Endpoints {
    @Multipart
    @POST("authenticate")
    Call<TokenModel> getTokenFromHover(@Part("email") RequestBody email, @Part("password")RequestBody password);

    @GET("get_token")
    Call<ApiKeyModel> getApiFromHover(@Query("package_name") String packageName);
}
