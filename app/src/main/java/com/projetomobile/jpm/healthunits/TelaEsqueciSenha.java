package com.projetomobile.jpm.healthunits;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TelaEsqueciSenha extends AppCompatActivity {

    EditText editEmail;
    Button btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_esqueci_senha);

        //Criando todos as views da tela que tem id
        editEmail = (EditText)findViewById(R.id.emailEsqueciSenha);
        btnEnviar = (Button)findViewById(R.id.botaoEnviar);

        //Início da chamada de tela caso tenha
        this.chamaLogin();

    }

    private void chamaLogin(){
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callTelaLogin = new Intent(TelaEsqueciSenha.this,TelaLogin.class);
                startActivity(callTelaLogin);
            }
        });
    }

}
