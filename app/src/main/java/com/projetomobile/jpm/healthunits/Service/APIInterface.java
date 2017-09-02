package com.projetomobile.jpm.healthunits.Service;

import com.projetomobile.jpm.healthunits.ValueObject.Estabelecimento;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by phelipepalmeira on 23/08/17.
 */

public interface APIInterface {

    @GET("rest/estabelecimentos")
    Call<List<Estabelecimento>> listEstabelecimento();

    @GET("rest/estabelecimentos/latitude/{latitude}/longitude/{longitude}/raio/{raio}")
    Call<List<Estabelecimento>> listEstabelecimentoRaio(@Path("latitude") String lat,@Path("longitude") String lon,@Path("raio") String raio);

}
