package com.projetomobile.jpm.healthunits.Telas;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.projetomobile.jpm.healthunits.Adaptadores.MyAdapterEstabelecimento;
import com.projetomobile.jpm.healthunits.R;
import com.projetomobile.jpm.healthunits.Service.APIInterface;
import com.projetomobile.jpm.healthunits.ValueObject.Estabelecimento;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.projetomobile.jpm.healthunits.Service.ControllerRetrofit.BASE_URL;

public class TelaListaEstabelecimento extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_lista_estabelecimento);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        APIInterface apiEstabelecimento = retrofit.create(APIInterface.class);

        Call<List<Estabelecimento>> call = apiEstabelecimento.listEstabelecimento();
        call.enqueue(new Callback<List<Estabelecimento>>() {
            @Override
            public void onResponse(Call<List<Estabelecimento>> call, Response<List<Estabelecimento>> response) {
                List<Estabelecimento> listEst = new ArrayList<Estabelecimento>();
                if(response.isSuccessful()){
                    for(Estabelecimento e : response.body()){
                        listEst.add(e);

                    }
                    mAdapter = new MyAdapterEstabelecimento(listEst);
                    Log.e("","Funcionou");
                    recyclerView.setAdapter(mAdapter);
                }

            }

            @Override
            public void onFailure(Call<List<Estabelecimento>> call, Throwable t) {
                Log.e("","Deu merda");
            }
        });

    }
}