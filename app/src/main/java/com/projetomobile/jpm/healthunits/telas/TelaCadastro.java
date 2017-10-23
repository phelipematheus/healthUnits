package com.projetomobile.jpm.healthunits.telas;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.projetomobile.jpm.healthunits.R;


public class TelaCadastro extends AppCompatActivity{

    EditText editNome,editEmail,editSenha;
    Button btnCadastrar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_cadastro);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Criando todos as views da tela que tem id
        editEmail = (EditText)findViewById(R.id.emailCadastro);
        editSenha = (EditText)findViewById(R.id.senhaCadastro);
        btnCadastrar = (Button)findViewById(R.id.botaoCadastrar);

        //Início da chamada de tela caso tenha
        this.cadastrar();

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void cadastrar(){
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.createUserWithEmailAndPassword(editEmail.getText().toString(),editSenha.getText().toString());
                Toast.makeText(getApplicationContext(),"Usuario cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                finish();
                //se ja existe
                //conferir os dois campos da senha
                //conferir o padrao do email
            }
        });
    }
}
