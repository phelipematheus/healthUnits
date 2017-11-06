package com.projetomobile.jpm.healthunits.telas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.projetomobile.jpm.healthunits.R;
import com.projetomobile.jpm.healthunits.dao.ConfiguracaoFirebase;
import com.projetomobile.jpm.healthunits.telas.chat.TelaChat;
import com.projetomobile.jpm.healthunits.telas.chat.TelaPreChat;

import static com.projetomobile.jpm.healthunits.adaptadores.MyAdapterEstabelecimento.verificaSePodeFinalizarActivity;

public class TelaMenuNavegacao extends AppCompatActivity{

    public ImageView btnPrecisoDeSocorro;
    public Button btnChat, btnListaHospitais, btnMapaDosHospitais, btnSair;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_menu_navegacao);

        btnChat = (Button) findViewById(R.id.btn_chat);
        btnPrecisoDeSocorro = (ImageView) findViewById(R.id.btn_pre_chat);
        btnListaHospitais = (Button) findViewById(R.id.btn_lista_hospitais);
        btnMapaDosHospitais = (Button) findViewById(R.id.btn_mapa_hospitais);
        btnSair = (Button) findViewById(R.id.btn_sair);

        this.chamaMapaDosHospitais();
        this.chamaListaHospitais();
        this.chamaPrecisoDeSocorro();
        this.chamaChat();
        this.chamaSair();
    }

    private void chamaMapaDosHospitais(){
        btnMapaDosHospitais.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent callTelaMaps = new Intent(TelaMenuNavegacao.this,TelaMaps.class);
                startActivity(callTelaMaps);
            }
        });
    }

    private void chamaListaHospitais(){
        btnListaHospitais.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                verificaSePodeFinalizarActivity = false;
                Intent callTelaListaHospitais = new Intent(TelaMenuNavegacao.this,TelaListaEstabelecimento.class);
                startActivity(callTelaListaHospitais);
            }
        });
    }

    private void chamaPrecisoDeSocorro(){
        btnPrecisoDeSocorro.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent callTelaDescricaoPreChat = new Intent(TelaMenuNavegacao.this,TelaPreChat.class);
                startActivity(callTelaDescricaoPreChat);
            }
        });
    }

    private void chamaChat(){
        btnChat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent callTelaChat = new Intent(TelaMenuNavegacao.this,TelaChat.class);
                startActivity(callTelaChat);
            }
        });
    }

    private void chamaSair(){
        btnSair.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
                autenticacao.signOut();
                LoginManager.getInstance().logOut();
                Intent callTelaLogin = new Intent(TelaMenuNavegacao.this,TelaLogin.class);
                startActivity(callTelaLogin);
                finish();
            }
        });
    }

}
