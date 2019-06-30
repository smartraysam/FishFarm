package com.smartdev.fishfarm.util;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {
    public static final String BASE_URL = Constants.BASE_URL;
    public static Retrofit retrofit;
    /*
    This public static method will return Retrofit client
    anywhere in the appplication
    */

 public static Retrofit getRetrofitClient() {
     Log.d(Constants.TAG, "network client started");
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) //This is the only mandatory call on Builder object
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
