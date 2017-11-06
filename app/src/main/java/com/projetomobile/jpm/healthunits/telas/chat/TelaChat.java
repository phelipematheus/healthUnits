package com.projetomobile.jpm.healthunits.telas.chat;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.projetomobile.jpm.healthunits.R;
import com.projetomobile.jpm.healthunits.adaptadores.MyAdapterChat;
import com.projetomobile.jpm.healthunits.dao.ConfiguracaoFirebase;
import com.projetomobile.jpm.healthunits.valueobject.ChatMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

import static com.projetomobile.jpm.healthunits.telas.TelaMaps.MAP_PERMISSION_ACCESS_FINE_LOCATION;
import static com.projetomobile.jpm.healthunits.telas.TelaMaps.PLAY_SERVICES_RESOLUTION_REQUEST;
import static com.projetomobile.jpm.healthunits.telas.chat.TelaLocaisPreChat.clicou;

public class TelaChat extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private String temImagem, tipoAcidente, descricao;
    private static int SIGN_IN_REQUEST_CODE = 1;
    private RelativeLayout activity_main;
    private LatLng localOcorrido;
    public static LatLng origem;

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager locationManager;

    //Add Emojicon
    EmojiconEditText emojiconEditText;
    ImageView emojiButton,submitButton;
    EmojIconActions emojIconActions;

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference mountainsRef;

    private byte[] byteArray;

    private List<ChatMessage> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_chat);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://health-units.appspot.com/");

        if(getIntent().hasExtra("TipoAcidente")){
            Bundle extras = getIntent().getExtras();
            tipoAcidente = (String) extras.get("TipoAcidente");
        }

        if(getIntent().hasExtra("Descricao")){
            Bundle extras = getIntent().getExtras();
            descricao = (String) extras.get("Descricao");
        }

        if(getIntent().hasExtra("ByteArray")){
            Bundle extras = getIntent().getExtras();
            byteArray = (byte[]) extras.get("ByteArray");
        }

        if(getIntent().hasExtra("LocalOcorrido")){
            Bundle extras = getIntent().getExtras();
            localOcorrido = (LatLng) extras.get("LocalOcorrido");
        }

        if (checkPlayServices()) {
            // Bildando o cliente da google api
            buildGoogleApiClient();
        }

        if(clicou == true){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Compartilhar?");
            builder.setMessage("Deseja compartihar o local do acidente?");
            builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {

                    String nameTime = String.valueOf(new Date().getTime());

                    mountainsRef = storageRef.child(nameTime+".jpg");

                    UploadTask uploadTask = mountainsRef.putBytes(byteArray);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(TelaChat.this,"upload done...",Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(TelaChat.this,"Deu ruim...",Toast.LENGTH_LONG).show();
                        }
                    });

                    if(FirebaseAuth.getInstance().getCurrentUser().getEmail() == null) {
                        FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage("Preciso de socorro! "+descricao+"!",
                                FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), nameTime, temImagem, localOcorrido, tipoAcidente));
                    }else {
                        FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage("Preciso de socorro! "+descricao+"!",
                                FirebaseAuth.getInstance().getCurrentUser().getEmail(), nameTime, temImagem, localOcorrido, tipoAcidente));// Base de dados do firebase recebe a mensagem e o usuário que enviou
                    }
                    clicou = false;
                }
            });
            builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TelaChat.this);
                    builder.setTitle("Mudar local?");
                    builder.setMessage("Deseja mudar o local do acidente sair?");
                    builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            clicou = false;
                            Intent telaLocaisPreChat = new Intent(TelaChat.this,TelaLocaisPreChat.class);
                            startActivity(telaLocaisPreChat);
                            finish();
                        }
                    });
                    builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            clicou = false;
                        }
                    });
                    AlertDialog alerta = builder.create();
                    alerta.show();
                }
            });
            AlertDialog alerta = builder.create();
            alerta.show();

            temImagem = "1";
        }else{
            temImagem = "2";
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        activity_main = (RelativeLayout)findViewById(R.id.activity_main);

        //Add Emoji
        emojiButton = (ImageView)findViewById(R.id.emoji_button);
        submitButton = (ImageView)findViewById(R.id.submit_button);
        emojiconEditText = (EmojiconEditText)findViewById(R.id.emojicon_edit_text);
        emojIconActions = new EmojIconActions(getApplicationContext(),activity_main,emojiButton,emojiconEditText);
        emojIconActions.ShowEmojicon();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emojiconEditText.getText().toString() != null && emojiconEditText.getText().toString().equals("") == false) {
                    if (FirebaseAuth.getInstance().getCurrentUser().getEmail() == null) {
                        FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(emojiconEditText.getText().toString(),
                                FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), null, "2",null, null));
                    } else {
                        FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(emojiconEditText.getText().toString(),
                                FirebaseAuth.getInstance().getCurrentUser().getEmail(), null, "2", null, null));// Base de dados do firebase recebe a mensagem e o usuário que enviou
                    }
                    clicou = false;
                    emojiconEditText.setText("");// Zero o campo mensagem
                    emojiconEditText.requestFocus();// Volto o cursor para o campo de mensagem
                }else {
                    Toast.makeText(TelaChat.this,"Digite alguma coisa...",Toast.LENGTH_LONG).show();
                }
            }
        });

        //Checa se não está logado e vai para página de login
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            Toast.makeText(TelaChat.this,"Não conseguiu pegar o user do firebase", Toast.LENGTH_SHORT).show();
            displayChatMessage();
        }else{
            if(FirebaseAuth.getInstance().getCurrentUser().getEmail() == null) {
                Snackbar.make(activity_main,"Welcome "+FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),Snackbar.LENGTH_SHORT).show();
            }else{
                Snackbar.make(activity_main,"Welcome "+FirebaseAuth.getInstance().getCurrentUser().getEmail(),Snackbar.LENGTH_SHORT).show();
            }
            //Load content
            displayChatMessage();
        }
    }

    private void displayChatMessage() {

        RecyclerView listOfMessage = (RecyclerView)findViewById(R.id.list_of_message);
        RecyclerView.Adapter adapter;

        listOfMessage.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);

        listOfMessage.setLayoutManager(mLayoutManager);

        DatabaseReference ref = ConfiguracaoFirebase.getFirebase();
        ref.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                items.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ChatMessage chatMessage = postSnapshot.getValue(ChatMessage.class);
                    items.add(chatMessage);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(TelaChat.this,"Não foi possivel conectar a base de dados! ",Toast.LENGTH_LONG).show();
            }


        });
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MAP_PERMISSION_ACCESS_FINE_LOCATION);
        } else {
            getLastLocation();
            getLocation();
            displayLocation();
        }
        adapter = new MyAdapterChat(TelaChat.this, items);
        listOfMessage.setAdapter(adapter);
    }


    //================MAPS====================================================================


    public void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            LatLng me = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            origem = me;
        }
    }

    public void getLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    LatLng me = new LatLng(location.getLatitude(), location.getLongitude());
                    origem = me;

                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }
            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 40000, 0, locationListener);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MAP_PERMISSION_ACCESS_FINE_LOCATION);
        } else {
            getLastLocation();
            getLocation();
            displayLocation();

        }
        //mMap.setMyLocationEnabled(true);
    }

    /* ******************Localização com a LocationServices API******************************** */

    protected synchronized void buildGoogleApiClient() {
        // Cria um cliente da google API
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Não foi possível achar a google play services", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    private void displayLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MAP_PERMISSION_ACCESS_FINE_LOCATION);
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            //Minha localização
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            LatLng me = new LatLng(latitude, longitude);
            origem = me;

            Log.e("Resposta","ESTA NO IF");
        } else {
            Log.e("RESPOSTA","ESTÁ NO ELSE");
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("", "Connection failed:  "
                + result.getErrorCode());
    }
    @Override
    public void onConnected(Bundle arg0) {
        // Once connected with google api, get the location
        displayLocation();
    }
    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
