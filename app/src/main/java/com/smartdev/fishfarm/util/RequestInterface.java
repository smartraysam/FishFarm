package com.smartdev.fishfarm.util;


import com.smartdev.fishfarm.Model.LogModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RequestInterface {
    @GET("logquery.php")
    Call<ArrayList<LogModel>>getAllLog();

    @GET("logquery.php")
    Call<ArrayList<LogModel>>getLogWithDate(@Query("from") String from, @Query("till") String till);
}
//
//    @FormUrlEncoded
//    @POST("/api/userlogin")
//
