package com.projetomobile.jpm.healthunits.Telas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.projetomobile.jpm.healthunits.R;
import com.projetomobile.jpm.healthunits.Service.APIInterface;
import com.projetomobile.jpm.healthunits.Service.ControllerRetrofit;
import com.projetomobile.jpm.healthunits.ValueObject.Estabelecimento;

import java.util.List;

import retrofit2.Retrofit;

public class TelaSearchFilter extends AppCompatActivity{

    EditText editPesquisarSearch;
    Button btnBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_search_filter);

        //Criando todos as views da tela que tem id
        editPesquisarSearch = (EditText)findViewById(R.id.editPesquisarSearch);
        btnBuscar = (Button)findViewById(R.id.botaoBuscar);

        //Início da chamada de tela caso tenha
        this.chamaMaps();
    }

    private void chamaMaps(){
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent callTelaMaps = new Intent(TelaSearchFilter.this,TelaMaps.class);
               // startActivity(callTelaMaps);
               // finish();
               chamadaAPI();
            }
        });
    }

    public void chamadaAPI(){
        ControllerRetrofit cRetrofit = new ControllerRetrofit();
        cRetrofit.start();
    }

}
