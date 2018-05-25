package com.baselibrary.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;

/**
 * <pre>
 * Author: lcl
 * Date: 2017-12-27
 * Description
 *  Gson创建的工具类
 * Version: v0.0.1
 * Others:
 * </pre>
 */
public class GsonUtils {

    public static Gson getGson(){
        Gson gson = new GsonBuilder()
                .addDeserializationExclusionStrategy(new GsonDeserializeExclusion())
                .create();
        return gson;
    }

    private static final class GsonDeserializeExclusion implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredClass() == SimpleDateFormat.class;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }

}
