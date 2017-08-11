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

        //Criando todos as views da tela que tem id
        txtCadastro = (TextView)findViewById(R.id.cadastreSe);
        editEmail = (EditText)findViewById(R.id.emailEdit);
        editSenha = (EditText)findViewById(R.id.senhaEdit);
        btnEntrar = (Button)findViewById(R.id.botaoEntrar);
        txtEsqueciSenha = (TextView)findViewById(R.id.cliqueAqui);

        //Início da chamada de tela caso tenha
        this.chamaCadastro();
        this.chamaEsqueciSenha();
        this.chamaSearchFilter();

    }

    public void chamaCadastro(){
        txtCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callTelaCadastro = new Intent(TelaLogin.this,TelaCadastro.class);
                startActivity(callTelaCadastro);
            }
        });
    }

    public void chamaEsqueciSenha(){
        txtEsqueciSenha.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent callTelaEsqueciSenha = new Intent(TelaLogin.this,TelaEsqueciSenha.class);
                startActivity(callTelaEsqueciSenha);
            }
        });
    }

    public void chamaSearchFilter(){
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callTelaSearchFilter = new Intent(TelaLogin.this,TelaSearchFilter.class);
                startActivity(callTelaSearchFilter);
            }
        });
    }

}
