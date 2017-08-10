package com.projetomobile.jpm.healthunits;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



public class TelaLogin extends AppCompatActivity {

    EditText editEmail,editSenha;
    Button btnEntrar;
    TextView txtCadastro,txtEsqueciSenha;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_login);

        txtCadastro = (TextView)findViewById(R.id.cadastreSe);
        editEmail = (EditText)findViewById(R.id.emailEdit);
        editSenha = (EditText)findViewById(R.id.senhaEdit);
        btnEntrar = (Button)findViewById(R.id.botaoEntrar);
        txtEsqueciSenha = (TextView)findViewById(R.id.cliqueAqui);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent secondActivity = new Intent(TelaLogin.this,TelaCadastro.class);
                startActivity(secondActivity);
            }
        });
    }

    public void abreCadastro(View view){

        //setContentView((R.layout.tela_cadastro));
    }

    public void abreEsqueciSenha(View view){
        //setContentView(R.layout.tela_esqueci_senha);
    }

    public void abreLogin(View view){
        //setContentView(R.layout.tela_login);
    }

    public void abreMapa(View view){
        //setContentView(R.layout.tela_mapa);
    }

    public void abreSearchFilter(View view){
        //setContentView(R.layout.tela_search_filter);
    }

}
