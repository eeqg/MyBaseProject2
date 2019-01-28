package com.example.resource.network;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import java.io.Reader;
import java.lang.reflect.Type;

public class ConvertUtil {

    private static Gson create() {
        return ConvertUtil.GsonHolder.gson;
    }

    private static class GsonHolder {
        private static Gson gson = new Gson();
    }

    public static <T> T fromJson(String json, Class<T> type) throws JsonIOException, JsonSyntaxException {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return create().fromJson(json, type);
    }

    public static <T> T fromJson(String json, Type type) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return create().fromJson(json, type);
    }

    public static <T> T fromJson(JsonReader reader, Type typeOfT) throws JsonIOException, JsonSyntaxException {
        if (reader == null || typeOfT == null) {
            return null;
        }
        return create().fromJson(reader, typeOfT);
    }

    public static <T> T fromJson(Reader json, Class<T> classOfT) throws JsonSyntaxException, JsonIOException {
        if (json == null || classOfT == null) {
            return null;
        }
        return create().fromJson(json, classOfT);
    }

    public static <T> T fromJson(Reader json, Type typeOfT) throws JsonIOException, JsonSyntaxException {
        if (json == null || typeOfT == null) {
            return null;
        }
        return create().fromJson(json, typeOfT);
    }

    public static String toJson(Object src) {
        if (src == null) {
            return null;
        }
        return create().toJson(src);
    }

    public static String toJson(Object src, Type typeOfSrc) {
        if (src == null || typeOfSrc == null) {
            return null;
        }
        return create().toJson(src, typeOfSrc);
    }

}
