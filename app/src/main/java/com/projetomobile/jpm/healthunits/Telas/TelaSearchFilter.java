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
                Intent callTelaMaps = new Intent(TelaSearchFilter.this,TelaMaps.class);
                startActivity(callTelaMaps);
                finish();
            }
        });
    }

    //Tratando o botão voltar
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sair?");
        builder.setMessage("Deseja realmente sair?");
        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        AlertDialog alerta = builder.create();
        alerta.show();
    }
}
