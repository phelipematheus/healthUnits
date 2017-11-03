package com.projetomobile.jpm.healthunits.telas.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.projetomobile.jpm.healthunits.R;

public class TelaPreChat extends AppCompatActivity {

    private Button btnAtropelamento, btnInfarte, btnConvulsao, btnPessoaBaleada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_pre_chat);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btnAtropelamento = (Button) findViewById(R.id.btn_atropelamento);
        btnInfarte = (Button) findViewById(R.id.btn_infarte);
        btnConvulsao = (Button) findViewById(R.id.btn_convulsao);
        btnPessoaBaleada = (Button) findViewById(R.id.btn_pessoa_baleada);

        chamaAtropelamento();
        chamaInfarte();
        chamaConvulsao();
        chamaPessoaBaleada();
    }

    private void chamaAtropelamento() {
        btnAtropelamento.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent callTelaPreChat = new Intent(TelaPreChat.this,TelaLocaisPreChat.class);
                callTelaPreChat.putExtra("TipoAcidente", "Atropelamento");
                startActivity(callTelaPreChat);
                finish();
            }
        });
    }

    private void chamaInfarte() {
        btnInfarte.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent callTelaPreChat = new Intent(TelaPreChat.this,TelaLocaisPreChat.class);
                callTelaPreChat.putExtra("TipoAcidente", "Infarte");
                startActivity(callTelaPreChat);
                finish();
            }
        });
    }

    private void chamaConvulsao() {
        btnConvulsao.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent callTelaPreChat = new Intent(TelaPreChat.this,TelaLocaisPreChat.class);
                callTelaPreChat.putExtra("TipoAcidente", "Convulsão");
                startActivity(callTelaPreChat);
                finish();
            }
        });
    }

    private void chamaPessoaBaleada() {
        btnPessoaBaleada.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent callTelaPreChat = new Intent(TelaPreChat.this,TelaLocaisPreChat.class);
                callTelaPreChat.putExtra("TipoAcidente", "Pessoa Baleada");
                startActivity(callTelaPreChat);
                finish();
            }
        });
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
