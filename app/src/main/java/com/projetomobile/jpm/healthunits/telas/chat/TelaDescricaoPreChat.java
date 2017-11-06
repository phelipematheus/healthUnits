package com.projetomobile.jpm.healthunits.telas.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.projetomobile.jpm.healthunits.R;

public class TelaDescricaoPreChat extends AppCompatActivity {

    private String tipoAcidente;
    private EditText edtDescricao;
    private Button btnContinuar;

    private String descricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_pre_chat_descricao);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(getIntent().hasExtra("TipoAcidente")){
            Bundle extras = getIntent().getExtras();
            tipoAcidente = (String) extras.get("TipoAcidente");
        }

        edtDescricao = (EditText) findViewById(R.id.edt_descricao);
        btnContinuar = (Button) findViewById(R.id.btn_continuar);

        //descricao = edtDescricao.getText().toString();
        this.chamaTelaLocaisPreChat();
    }

    private void chamaTelaLocaisPreChat(){
        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callTelaLocaisPreChat = new Intent(TelaDescricaoPreChat.this,TelaLocaisPreChat.class);
                callTelaLocaisPreChat.putExtra("TipoAcidente", tipoAcidente);
                callTelaLocaisPreChat.putExtra("Descricao", String.valueOf(edtDescricao.getText()));
                startActivity(callTelaLocaisPreChat);
                finish();
            }
        });



    }
}
