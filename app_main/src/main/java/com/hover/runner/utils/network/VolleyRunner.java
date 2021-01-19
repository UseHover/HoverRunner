package com.hover.runner.utils.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.hover.runner.R;
import com.hover.runner.settings.SettingsHelper;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

final public class VolleyRunner {
    private final static String TAG = "VolleySingleton";
    @SuppressLint("StaticFieldLeak") // Singleton instance is recommended by android docs
    private static VolleyRunner instance;
    private RequestQueue requestQueue;
    private final Context ctx;
    private final static long TIMEOUT_S = 300; // This is super long (5 min), but they may be downloading up to a couple MB on a very slow connection!

    private VolleyRunner(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized VolleyRunner getInstance(Context context) {
        if (instance == null)
            instance = new VolleyRunner(context);
        return instance;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext(), new VolleyRunner.CustomHurlStack());
        return requestQueue;
    }

    private <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static void uploadJsonNow(Context c, String url_end, final JSONObject json, Response.Listener<JSONObject> listener) {
        String url = c.getString(R.string.hsdk_url_builder, c.getString(R.string.api_url), url_end);
        Log.e(TAG, "url: " + url);
        VolleyRunner.getInstance(c).addToRequestQueue(new JsonObjectRequest(url, json, listener, error -> Log.e(TAG, "error" + error.getMessage())));
    }

    class CustomHurlStack extends HurlStack {
        @Override
        protected HttpURLConnection createConnection(URL url) throws IOException {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization", SettingsHelper.getToken(ctx));
            return connection;
        }
    }
}

