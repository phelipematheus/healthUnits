package com.projetomobile.jpm.healthunits.Telas;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.projetomobile.jpm.healthunits.R;


public class TelaCadastro extends AppCompatActivity{

    EditText editNome,editEmail,editSenha;
    Button btnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_cadastro);

        //Criando todos as views da tela que tem id
        editNome = (EditText)findViewById(R.id.nomeCadastro);
        editEmail = (EditText)findViewById(R.id.emailCadastro);
        editSenha = (EditText)findViewById(R.id.senhaCadastro);
        btnCadastrar = (Button)findViewById(R.id.botaoCadastrar);

        //Início da chamada de tela caso tenha
        this.chamaLogin();

    }

    private void chamaLogin(){
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callTelaLogin = new Intent(TelaCadastro.this,TelaLogin.class);
                startActivity(callTelaLogin);
                finish();
            }
        });
    }

    //Tratando o botão voltar
    @Override
    public void onBackPressed() {
        Intent callTelaLogin = new Intent(TelaCadastro.this,TelaLogin.class);
        startActivity(callTelaLogin);
        finish();
    }

}
