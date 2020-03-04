package com.app2.Classes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Map;

public class myJSONObject extends JSONObject {

    public myJSONObject() {
        super();
    }

    public myJSONObject(@NonNull Map copyFrom) {
        super(copyFrom);
    }

    public myJSONObject(@NonNull JSONTokener readFrom) throws JSONException {
        super(readFrom);
    }

    public myJSONObject(@NonNull String json) throws JSONException {
        super(json);
    }

    public myJSONObject(@NonNull JSONObject copyFrom, @NonNull String[] names) throws JSONException {
        super(copyFrom, names);
    }


    public String getMoney(String name)  throws JSONException {
        try{
            return NumberFormat.getCurrencyInstance().format(Float.parseFloat(this.getString(name).replaceAll(",",".")));
        }catch(Exception e){
            return this.getString(name);
        }

    }

    @NonNull
    @Override
    public String getString(@NonNull String name) throws JSONException {
        if(this.has(name)) {
            return super.getString(name);
        }else{
            return "";
        }
    }

    @Override
    public int getInt(@NonNull String name) throws JSONException {
        if(this.has(name)) {
            return super.getInt(name);
        }else{
            return 0;
        }
    }

    @Override
    public double getDouble(@NonNull String name) throws JSONException {
        if(this.has(name)) {
            return super.getDouble(name);
        }else{
            return 0;
        }
    }


    @Override
    public int length() {
        if(this == null){
            return 0;
        }else {
            return super.length();
        }
    }

    @NonNull
    @Override
    public JSONObject put(@NonNull String name, boolean value) throws JSONException {
        return super.put(name, value);
    }

    @NonNull
    @Override
    public JSONObject put(@NonNull String name, double value) throws JSONException {
        return super.put(name, value);
    }

    @NonNull
    @Override
    public JSONObject put(@NonNull String name, int value) throws JSONException {
        return super.put(name, value);
    }

    @NonNull
    @Override
    public JSONObject put(@NonNull String name, long value) throws JSONException {
        return super.put(name, value);
    }

    @NonNull
    @Override
    public JSONObject put(@NonNull String name, @Nullable Object value) throws JSONException {
        return super.put(name, value);
    }

    @NonNull
    @Override
    public JSONObject putOpt(@Nullable String name, @Nullable Object value) throws JSONException {
        return super.putOpt(name, value);
    }

    @NonNull
    @Override
    public JSONObject accumulate(@NonNull String name, @Nullable Object value) throws JSONException {
        return super.accumulate(name, value);
    }

    @Nullable
    @Override
    public Object remove(@Nullable String name) {
        return super.remove(name);
    }

    @Override
    public boolean isNull(@Nullable String name) {
        return super.isNull(name);
    }

    @Override
    public boolean has(@Nullable String name) {
        return super.has(name);
    }

    @NonNull
    @Override
    public Object get(@NonNull String name) throws JSONException {
        return super.get(name);
    }

    @Nullable
    @Override
    public Object opt(@Nullable String name) {
        return super.opt(name);
    }

    @Override
    public boolean getBoolean(@NonNull String name) throws JSONException {
        return super.getBoolean(name);
    }

    @Override
    public boolean optBoolean(@Nullable String name) {
        return super.optBoolean(name);
    }

    @Override
    public boolean optBoolean(@Nullable String name, boolean fallback) {
        return super.optBoolean(name, fallback);
    }

    @Override
    public double optDouble(@Nullable String name) {
        return super.optDouble(name);
    }

    @Override
    public double optDouble(@Nullable String name, double fallback) {
        return super.optDouble(name, fallback);
    }

    @Override
    public int optInt(@Nullable String name) {
        return super.optInt(name);
    }

    @Override
    public int optInt(@Nullable String name, int fallback) {
        return super.optInt(name, fallback);
    }

    @Override
    public long getLong(@NonNull String name) throws JSONException {
        return super.getLong(name);
    }

    @Override
    public long optLong(@Nullable String name) {
        return super.optLong(name);
    }

    @Override
    public long optLong(@Nullable String name, long fallback) {
        return super.optLong(name, fallback);
    }

    @NonNull
    @Override
    public String optString(@Nullable String name) {
        return super.optString(name);
    }

    @NonNull
    @Override
    public String optString(@Nullable String name, @NonNull String fallback) {
        return super.optString(name, fallback);
    }

    @NonNull
    @Override
    public JSONArray getJSONArray(@NonNull String name) throws JSONException {
        return super.getJSONArray(name);
    }

    @Nullable
    @Override
    public JSONArray optJSONArray(@Nullable String name) {
        return super.optJSONArray(name);
    }

    @NonNull
    @Override
    public JSONObject getJSONObject(@NonNull String name) throws JSONException {
        return super.getJSONObject(name);
    }

    @Nullable
    @Override
    public JSONObject optJSONObject(@Nullable String name) {
        return super.optJSONObject(name);
    }

    @Nullable
    @Override
    public JSONArray toJSONArray(@Nullable JSONArray names) throws JSONException {
        return super.toJSONArray(names);
    }

    @NonNull
    @Override
    public Iterator<String> keys() {
        return super.keys();
    }

    @Nullable
    @Override
    public JSONArray names() {
        return super.names();
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

    @NonNull
    @Override
    public String toString(int indentSpaces) throws JSONException {
        return super.toString(indentSpaces);
    }

}
