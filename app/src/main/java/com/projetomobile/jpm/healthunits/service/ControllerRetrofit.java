package com.projetomobile.jpm.healthunits.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.projetomobile.jpm.healthunits.valueobject.Estabelecimento;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by phelipepalmeira on 02/09/17.
 */

public class ControllerRetrofit implements Callback<List<Estabelecimento>> {

    public static final String BASE_URL = "http://mobile-aceite.tcu.gov.br/mapa-da-saude/";

    private List<Estabelecimento> listaEstabelecimentos = new ArrayList<Estabelecimento>();

    public ControllerRetrofit() {
        start();
    }

    public void start() {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();

        APIInterface apiEstabelecimento = retrofit.create(APIInterface.class);

        Call<List<Estabelecimento>> call = apiEstabelecimento.listEstabelecimento();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Estabelecimento>> call, Response<List<Estabelecimento>> response) {
        if(response.isSuccessful()){
            List<Estabelecimento> e = new ArrayList<Estabelecimento>();
            for(Estabelecimento estabelecimento : response.body()){
                e.add(estabelecimento);
            }

            setListaEstabelecimentos(e);
            Log.e("","PASSOU!");
        }else {
            Log.e("", "NAO PASSOU");
        }
    }

    @Override
    public void onFailure(Call<List<Estabelecimento>> call, Throwable t) {
        Log.e("","Falou");
        Log.e("",t.getMessage());
    }

    public List<Estabelecimento> getListaEstabelecimentos() {

        return listaEstabelecimentos;
    }

    public void setListaEstabelecimentos(List<Estabelecimento> listaEstabelecimentos) {
        this.listaEstabelecimentos = listaEstabelecimentos;
    }
}
