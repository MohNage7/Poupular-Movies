package com.mohnage7.popularmovies.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mohnage7.popularmovies.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mohnage7.popularmovies.utils.Constants.BASE_URL;


public class ApiClient {

    private static Api service;


    public static Api getClient() {
        if (service == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            // Add logging into retrofit 2.0 if in debug mode
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                httpClient.interceptors().add(logging);
            }


            httpClient.connectTimeout(10, TimeUnit.MINUTES)
                    .readTimeout(10, TimeUnit.MINUTES);

            // add API key as Query parameter
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    HttpUrl originalHttpUrl = original.url();


                    // API KEY GOES here ... deleted for uploading to github
                    HttpUrl url = originalHttpUrl.newBuilder()
                            .addQueryParameter("api_key", "")
                            .build();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .url(url)
                            .addHeader("Cache-Control", "public, max-stale=2419200");

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });


            // get remote configs
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build()).build();

            service = retrofit.create(Api.class);
        }
        return service;
    }
}
