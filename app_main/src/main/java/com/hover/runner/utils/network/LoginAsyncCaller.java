package com.hover.runner.utils.network;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.hover.runner.ApplicationInstance;
import com.hover.runner.R;
import com.hover.runner.api.RetrofitCalls;
import com.hover.runner.enums.HomeEnums;
import com.hover.runner.interfaces.Endpoints;
import com.hover.runner.models.ApiKeyModel;
import com.hover.runner.models.LoginModel;
import com.hover.runner.models.TokenModel;
import com.hover.runner.settings.SettingsHelper;
import com.hover.runner.utils.UIHelper;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class LoginAsyncCaller extends AsyncTask<String, Void, LoginModel> {

    @Override
    protected LoginModel doInBackground(String... strings) {
        Context c = ApplicationInstance.getContext();
        String email = strings[0];
        String password = strings[1];

        RetrofitCalls retrofitCalls = new RetrofitCalls();
        Endpoints retrofitToken = retrofitCalls.getRetrofitToken(c).create(Endpoints.class);
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody passwordBody = RequestBody.create(MediaType.parse("text/plain"), password);
        Call<TokenModel> callerToken = retrofitToken.getTokenFromHover(emailBody, passwordBody);

        try {
            retrofit2.Response<TokenModel> tokenModel = callerToken.execute();
            if (tokenModel.code() == 200 && tokenModel.body() != null && tokenModel.body().getAuth_token() != null) {
                Log.e("LOGIN", "key: " + tokenModel.body().getApiKey());
                SettingsHelper.saveOrgId(tokenModel.body().getOrgId(), c);
                SettingsHelper.saveToken(tokenModel.body().getAuth_token(), c);
                SettingsHelper.saveApiKey(tokenModel.body().getApiKey(), c);
                Endpoints retrofitApi = retrofitCalls.getRetrofitApi(tokenModel.body().getAuth_token(), c).create(Endpoints.class);
                return new LoginModel(HomeEnums.SUCCESS, c.getString(R.string.sign_in_success));
            }
        } catch (IOException e) { e.printStackTrace(); }

        return new LoginModel(HomeEnums.ERROR, c.getString(R.string.sign_in_err));
    }
}