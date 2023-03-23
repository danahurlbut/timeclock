package com.example.timeclock.services;

import com.example.timeclock.models.User;
import com.example.timeclock.models.UserGsonTypeAdapter;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
//    Future enhancement -- turn this app into an offline-first app. Use a local SQLite db as the single
//    source of truth that the app always gets its data from. Use Room to read from and write to the db,
//    use Observable pattern to update app whenever local data gets updated via the network.
    private static Retrofit retrofit = null;
    //TODO -- replace with real API URL when ready
    private static final String BASE_URL = "https://uers-api.p.rapidapi.com/";

    public static Retrofit getAPIClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(User.class, new UserGsonTypeAdapter());

        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
            Request request = chain.request().newBuilder()
                    .addHeader("X-RapidAPI-Key", "50bd0f8594mshf680df187d624dep174c9cjsnb9ad8ce073d8")
                    .addHeader("X-RapidAPI-Host", "uers-api.p.rapidapi.com")
                    .addHeader("access-control-allow-credentials", "true")
                    .addHeader("content-type", "application/json; charset=utf-8")
                    .addHeader("server", "RapidAPI-1.2.8")
                    .build();
            return chain.proceed(request);
        }).addInterceptor(interceptor).build();

        if (retrofit == null)
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                    .client(client)
                    .build();

        return retrofit;
    }
}