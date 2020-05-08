package com.usehover.testerv2.utils.network;

import android.content.pm.PackageInfo;
import android.os.AsyncTask;

import com.usehover.testerv2.ApplicationInstance;
import com.usehover.testerv2.api.RetrofitCalls;
import com.usehover.testerv2.enums.HomeEnums;
import com.usehover.testerv2.interfaces.Endpoints;
import com.usehover.testerv2.models.ApiKeyModel;
import com.usehover.testerv2.models.LoginModel;
import com.usehover.testerv2.models.TokenModel;
import com.usehover.testerv2.utils.UIHelper;
import com.usehover.testerv2.utils.Utils;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class LoginAsyncCaller extends AsyncTask<String, Void, LoginModel> {

    @Override
    protected LoginModel doInBackground(String... strings) {
        String email = strings[0];
        String password = strings[1];

        RetrofitCalls retrofitCalls = new RetrofitCalls();
        Endpoints retrofitToken = retrofitCalls.getRetrofitToken().create(Endpoints.class);
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody passwordBody = RequestBody.create(MediaType.parse("text/plain"), password);


        Call<TokenModel> callerToken = retrofitToken.getTokenFromHover(emailBody, passwordBody);
        try {
            retrofit2.Response<TokenModel> tokenModel = callerToken.execute();
            if(tokenModel.code() == 200 && tokenModel.body() !=null && tokenModel.body().getAuth_token() !=null) {
                Endpoints retrofitApi = retrofitCalls.getRetrofitApi(tokenModel.body().getAuth_token()).create(Endpoints.class);
                PackageInfo packageInfo = UIHelper.getPackageInfo(ApplicationInstance.getContext());
                String loginFailedCausedByPackage = "Something's wrong with your app settings, unable to get your package name";
                if(packageInfo !=null) {
                    String packageName = packageInfo.packageName;
                    Call<ApiKeyModel> apiCaller = retrofitApi.getApiFromHover(packageName);
                    Response<ApiKeyModel> callerApi = apiCaller.execute();
                    if(callerApi.code() == 200 && callerApi.body() !=null && callerApi.body().getApi_key() !=null) {
                        Utils.saveApiKey(callerApi.body().getApi_key());
                        String loginSuccess = "Login successful";
                        return new LoginModel(HomeEnums.SUCCESS, loginSuccess);
                    }
                } else return new LoginModel(HomeEnums.ERROR, loginFailedCausedByPackage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String loginAuthFailMessage = "Authentication failed. Wrong email or password";
        return new LoginModel(HomeEnums.ERROR, loginAuthFailMessage);
    }
}