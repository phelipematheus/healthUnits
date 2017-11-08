package com.projetomobile.jpm.healthunits.telas.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.projetomobile.jpm.healthunits.R;

public class TelaPreChat extends AppCompatActivity {

    private Button btnAtropelamento, btnInfarte, btnConvulsao, btnPessoaBaleada, btnDesmaio, btnDorNoPeito, btnEngasgo, btnAfogamento, btnAcidentePeconhento, btnAcidenteToxico, btnQueda, btnQueimadura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_pre_chat);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btnAtropelamento = (Button) findViewById(R.id.btn_atropelamento);
        btnAtropelamento.setOnClickListener(new MyListenner());

        btnConvulsao = (Button) findViewById(R.id.btn_convulsao);
        btnConvulsao.setOnClickListener(new MyListenner());

        btnDesmaio = (Button) findViewById(R.id.btn_desmaio);
        btnDesmaio.setOnClickListener(new MyListenner());

        btnDorNoPeito = (Button) findViewById(R.id.btn_dor_peito);
        btnDorNoPeito.setOnClickListener(new MyListenner());

        btnEngasgo = (Button) findViewById(R.id.btn_engasgo);
        btnEngasgo.setOnClickListener(new MyListenner());

        btnAfogamento = (Button) findViewById(R.id.btn_afogamento);
        btnAfogamento.setOnClickListener(new MyListenner());

        btnAcidentePeconhento = (Button) findViewById(R.id.btn_acidente_peconhento);
        btnAcidentePeconhento.setOnClickListener(new MyListenner());

        btnAcidenteToxico = (Button) findViewById(R.id.btn_acidente_toxico);
        btnAcidenteToxico.setOnClickListener(new MyListenner());

        btnQueda = (Button) findViewById(R.id.btn_queda);
        btnQueda.setOnClickListener(new MyListenner());

        btnPessoaBaleada = (Button) findViewById(R.id.btn_pessoa_baleada);
        btnPessoaBaleada.setOnClickListener(new MyListenner());

        btnInfarte = (Button) findViewById(R.id.btn_infarte);
        btnInfarte.setOnClickListener(new MyListenner());

        btnQueimadura = (Button) findViewById(R.id.btn_queimadura);
        btnQueimadura.setOnClickListener(new MyListenner());

    }

    public void escolhaPreChat(Button escolha) {
        Intent callTelaDescricaoPreChat = new Intent(TelaPreChat.this, TelaDescricaoPreChat.class);
        callTelaDescricaoPreChat.putExtra("TipoAcidente", escolha.getText().toString());
        startActivity(callTelaDescricaoPreChat);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class MyListenner implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Button btnAtropelamento2 = (Button) view;
            Intent callTelaDescricaoPreChat = new Intent(TelaPreChat.this, TelaDescricaoPreChat.class);
            callTelaDescricaoPreChat.putExtra("TipoAcidente", btnAtropelamento2.getText().toString());
            startActivity(callTelaDescricaoPreChat);
        }
    }
}
