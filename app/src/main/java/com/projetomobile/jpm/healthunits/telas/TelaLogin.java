package com.projetomobile.jpm.healthunits.telas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.projetomobile.jpm.healthunits.dao.ConfiguracaoFirebase;
import com.projetomobile.jpm.healthunits.entidade.Usuario;
import com.projetomobile.jpm.healthunits.R;

import static com.projetomobile.jpm.healthunits.telas.TelaMaps.MAP_PERMISSION_ACCESS_FINE_LOCATION;


public class TelaLogin extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    private FirebaseAuth autenticacao;
    private Usuario usuario;
    private EditText editEmail,editSenha;
    private Button btnEntrar;
    private TextView txtCadastro,txtEsqueciSenha;

    private SignInButton signIn;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

    private CallbackManager callbackManager;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_login);
        //==================PERMISSÕES DO APLICATIVO===================================================
        // Se não possui permissão
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // Verifica se já mostramos o alerta e o usuário negou na 1ª vez.
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // Caso o usuário tenha negado a permissão anteriormente, e não tenha marcado o check "nunca mais mostre este alerta"
                    // Podemos mostrar um alerta explicando para o usuário porque a permissão é importante.
                } else {
                    // Solicita a permissão
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MAP_PERMISSION_ACCESS_FINE_LOCATION);
                }
        }

        //Criando todos as views da tela que tem id
        txtCadastro = (TextView)findViewById(R.id.cadastreSe);
        editEmail = (EditText)findViewById(R.id.emailEdit);
        editSenha = (EditText)findViewById(R.id.senhaEdit);
        btnEntrar = (Button)findViewById(R.id.botaoEntrar);
        txtEsqueciSenha = (TextView)findViewById(R.id.cliqueAqui);
        signIn = (SignInButton) findViewById(R.id.entreGoogle);

        //===========IMPLEMENTAÇÃO DO BOTÃO GOOGLE=====================================================

        signIn.setSize(SignInButton.SIZE_STANDARD);
        signIn.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //===========IMPLEMENTAÇÃO DO BOTÃO FACEBOOK===================================================

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                ProfileTracker profileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                        Toast.makeText(TelaLogin.this,"Bem-Vindo "+currentProfile.getName()+"!",Toast.LENGTH_LONG).show();
                    }
                };
                Intent callTelaMenuNavegacao = new Intent(TelaLogin.this,TelaMenuNavegacao.class);
                startActivity(callTelaMenuNavegacao);
                finish();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(TelaLogin.this,"Não foi possível efetuar o login com o Facebook!",Toast.LENGTH_LONG).show();
            }
        });

        //=============================================================================================

        //Início da chamada de tela caso tenha
        this.chamaCadastro();
        this.chamaEsqueciSenha();
        //this.chamaSearchFilter();
        this.chamaMenuNavegacao();
    }

    private void chamaMenuNavegacao(){
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
                    Intent callTelaMenuNavegacao = new Intent(TelaLogin.this,TelaMenuNavegacao.class);
                    startActivity(callTelaMenuNavegacao);
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

    //===========IMPLEMENTAÇÃO DO BOTÃO GOOGLE=======================================================

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(TelaLogin.this,"Não foi possível se conectar com Google!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.entreGoogle:
                entrar();
                break;
            //case R.id.idSignOut:
            //    sair();
            //    break;
        }
    }

    public void entrar(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

//IMPLEMENTAR BOTAO COM ID "idSignOut" E DESCOMENTAR O QUE TIVER COMENTADO NO onClick(View v)
//DEPOIS CHAMAR ESTE MÉTODO sair() PARA ENCERRAR A SESSÃO GOOGLE
    public void sair(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("", "handleSignInResult:" + result.isSuccess());
        boolean resultado = result.isSuccess();
        Log.e("VISHHHHHHHHHHH", ""+resultado);
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            String nameAccount = account.getDisplayName();
            String emailAccount = account.getEmail();

            //nome.setText(nameAccount);
            //email.setText(emailAccount);
            updateUI(true);
            Toast.makeText(TelaLogin.this,"Bem-Vindo "+nameAccount+"!",Toast.LENGTH_SHORT).show();
        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(TelaLogin.this,"Não foi possível efetuar o login com o Google!",Toast.LENGTH_SHORT).show();
            updateUI(false);
        }
    }

    public void updateUI(boolean isLogin){
        if(isLogin){
            Intent callTelaMenuNavegacao = new Intent(TelaLogin.this,TelaMenuNavegacao.class);
            startActivity(callTelaMenuNavegacao);
            finish();
        }else{

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Facebook
        callbackManager.onActivityResult(requestCode,resultCode,data);
        //Google
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    //================================================================================================



}
