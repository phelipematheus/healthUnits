package com.projetomobile.jpm.healthunits.telas.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.projetomobile.jpm.healthunits.R;

public class TelaLocaisPreChat extends AppCompatActivity {

    public TextView txtQualLocalOcorreu;
    public Button btnAquiOndeEstou, btnOutroLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_locais_pre_chat);

        txtQualLocalOcorreu = (TextView) findViewById(R.id.txt_qual_local_ocorreu);
        btnAquiOndeEstou = (Button) findViewById(R.id.btn_aqui_onde_estou);
        btnOutroLocal = (Button) findViewById(R.id.btn_outro_local);

        chamaAquiOndeEstou();
        chamaOutroLocal();
    }

    private void chamaOutroLocal() {
        btnAquiOndeEstou.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent callTelaPreChat = new Intent(TelaLocaisPreChat.this,TelaChat.class);
                startActivity(callTelaPreChat);
            }
        });
    }

    private void chamaAquiOndeEstou() {
        btnOutroLocal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent callTelaPreChat = new Intent(TelaLocaisPreChat.this,TelaChat.class);
                startActivity(callTelaPreChat);
            }
        });
    }

}
