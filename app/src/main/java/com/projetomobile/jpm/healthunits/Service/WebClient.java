package com.projetomobile.jpm.healthunits.Service;

import android.provider.MediaStore;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ppalmeira on 23/08/17.
 */

public class WebClient {

    // Se precisarmos utilizar o POST, ja foi criado !!
    /*public String post(String json) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String url = "http://mobile-aceite.tcu.gov.br/mapa-da-saude/rest/estabelecimentos";

        Request.Builder builder = new Request.Builder();

        builder.url(url);

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, json);
        builder.post(body);

        Request request = builder.build();
        Response response = client.newCall(request).execute();
        String jsonDeResposta = response.body().string();

        return jsonDeResposta;
    }*/

    // Parte que com certeza utilizadremos que é o GET da requisição da API
    public String get() throws IOException {

        String url = "http://mobile-aceite.tcu.gov.br/mapa-da-saude/rest/estabelecimentos";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();

        Response response = client.newCall(request).execute();

        String jsonDeResposta = response.body().string();

        return jsonDeResposta;
    }

}
