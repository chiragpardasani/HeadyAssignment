package com.headyassignment.db.converters;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.headyassignment.db.entity.Tax;

import java.lang.reflect.Type;

public class TaxTypeConverter {

    @TypeConverter
    public static Tax stringToTax(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return new Tax();
        }

        Type listType = new TypeToken<Tax>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String TaxToString(Tax tax) {
        Gson gson = new Gson();
        return gson.toJson(tax);
    }
}
