package com.projetomobile.jpm.healthunits.Telas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.projetomobile.jpm.healthunits.DAO.ConfiguracaoFirebase;
import com.projetomobile.jpm.healthunits.Entidade.Usuario;
import com.projetomobile.jpm.healthunits.R;


public class TelaLogin extends AppCompatActivity {

    FirebaseAuth autenticacao;
    Usuario usuario;
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
    
    private void chamaCadastro(){
        txtCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callTelaCadastro = new Intent(TelaLogin.this,TelaCadastro.class);
                startActivity(callTelaCadastro);
            }
        });
    }

    private void chamaEsqueciSenha(){
        txtEsqueciSenha.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent callTelaEsqueciSenha = new Intent(TelaLogin.this,TelaEsqueciSenha.class);
                startActivity(callTelaEsqueciSenha);
            }
        });
    }

    private void chamaSearchFilter(){
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(editEmail.toString().equals("")) && !(editSenha.toString().equals("")) ){
                    usuario = new Usuario();
                    usuario.setEmail(editEmail.getText().toString());
                    usuario.setPassword(editSenha.getText().toString());
                    validarLogin();
                }else {
                    Toast.makeText(TelaLogin.this,"Preencha os campos de e-mail e senha!",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void validarLogin(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(),usuario.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent callTelaSearchFilter = new Intent(TelaLogin.this,TelaSearchFilter.class);
                    startActivity(callTelaSearchFilter);
                    Toast.makeText(TelaLogin.this,"Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(TelaLogin.this,"Usuário ou senha incorretos!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Tratando o botão voltar
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sair?");
        builder.setMessage("Deseja realmente sair?");
        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        AlertDialog alerta = builder.create();
        alerta.show();
    }
}
