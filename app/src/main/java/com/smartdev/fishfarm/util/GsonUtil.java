package com.smartdev.fishfarm.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
    private static Gson gson;

    public static Gson getGsonparser() {
        if (gson == null) {
            gson = new GsonBuilder().create();
        }
        return gson;
    }
}
