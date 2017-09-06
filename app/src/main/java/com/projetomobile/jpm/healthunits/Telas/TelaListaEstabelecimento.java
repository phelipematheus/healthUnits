package com.projetomobile.jpm.healthunits.Telas;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.projetomobile.jpm.healthunits.Adaptadores.MyAdapter;
import com.projetomobile.jpm.healthunits.R;
import com.projetomobile.jpm.healthunits.Service.ControllerRetrofit;
import com.projetomobile.jpm.healthunits.ValueObject.Estabelecimento;

import java.util.ArrayList;
import java.util.List;

public class TelaListaEstabelecimento extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tela_lista_estabelecimento);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setAdapter(new MyAdapter(new ControllerRetrofit().getListaEstabelecimentos()));



    }
}
