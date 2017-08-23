package com.projetomobile.jpm.healthunits.Entidade;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by ppalmeira on 23/08/17.
 */

public class Base {
    //Classe que conterá os códigos que pegará o Json e tratará ele

    public void fromJson(final String jsonString, final Class<?> clazz) {
        final GsonBuilder builder = buildJsonParser();
        final Gson gson = builder.create();
        //return () gson.fromJson(jsonString, clazz);
    }

    public static GsonBuilder buildJsonParser() {
        final GsonBuilder builder = new GsonBuilder();
        return builder;
    }
}
