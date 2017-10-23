package com.projetomobile.jpm.healthunits.telas;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.WindowManager;

import com.projetomobile.jpm.healthunits.R;
import com.projetomobile.jpm.healthunits.adaptadores.MyAdapterEstabelecimento;
import com.projetomobile.jpm.healthunits.service.APIInterface;
import com.projetomobile.jpm.healthunits.valueobject.Estabelecimento;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.projetomobile.jpm.healthunits.adaptadores.MyAdapterEstabelecimento.verificaSePodeFinalizarActivity;
import static com.projetomobile.jpm.healthunits.service.ControllerRetrofit.BASE_URL;

public class TelaListaEstabelecimento extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_lista_estabelecimento);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        if(verificaSePodeFinalizarActivity == true){
            finish();
            verificaSePodeFinalizarActivity = false;
        }

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
                    mAdapter = new MyAdapterEstabelecimento(TelaListaEstabelecimento.this, listEst);
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
