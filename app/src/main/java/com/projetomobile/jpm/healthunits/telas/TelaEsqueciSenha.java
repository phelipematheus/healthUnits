package com.projetomobile.jpm.healthunits.telas;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.projetomobile.jpm.healthunits.R;

public class TelaEsqueciSenha extends AppCompatActivity {

    EditText editEmail;
    Button btnEnviar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_esqueci_senha);

        //Criando todos as views da tela que tem id
        editEmail = (EditText)findViewById(R.id.emailEsqueciSenha);
        btnEnviar = (Button)findViewById(R.id.botaoEnviar);

        //Início da chamada de tela caso tenha
        this.resetSenha();

    }

    private void resetSenha(){
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.sendPasswordResetEmail(editEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),"E-MAIL ENVIADO COM SUCESSO", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                finish();
            }
        });
    }

    //Tratando o botão voltar
    @Override
    public void onBackPressed() {
        finish();
    }

}
