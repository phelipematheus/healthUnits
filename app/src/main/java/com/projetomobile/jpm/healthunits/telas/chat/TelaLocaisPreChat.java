package com.projetomobile.jpm.healthunits.telas.chat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.projetomobile.jpm.healthunits.R;

import java.io.ByteArrayOutputStream;

import static com.projetomobile.jpm.healthunits.telas.TelaMaps.MAP_PERMISSION_ACCESS_FINE_LOCATION;
import static com.projetomobile.jpm.healthunits.telas.TelaMaps.PLAY_SERVICES_RESOLUTION_REQUEST;

public class TelaLocaisPreChat extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public LinearLayout linearLayoutPreChat;
    public TextView txtQualLocalOcorreu;
    public Button btnAquiOndeEstou, btnOutroLocal, btnOk;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private SupportMapFragment mapFragment;
    private LatLng ondeEstou;
    private Marker now, localOcorrido;
    private boolean verificaLastLocation = false;
    private boolean verificaCurrentLocation = false;
    private boolean verificaMoveCameraUmaUnicaVez = false;
    private TextView txtMoverMarker;

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;

    public static boolean clicou = false;
    private boolean jaPegouLocalizacao = false;

    private String tipoAcidente, descricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_locais_pre_chat);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(getIntent().hasExtra("Descricao")){
            Bundle extras = getIntent().getExtras();
            descricao = (String) extras.get("Descricao");
        }

        if(getIntent().hasExtra("TipoAcidente")){
            Bundle extras = getIntent().getExtras();
            tipoAcidente = (String) extras.get("TipoAcidente");
        }


        btnOk = (Button) findViewById(R.id.btn_ok);
        linearLayoutPreChat = (LinearLayout) findViewById(R.id.linear_layout_locais_chat);
        txtQualLocalOcorreu = (TextView) findViewById(R.id.txt_qual_local_ocorreu);
        btnAquiOndeEstou = (Button) findViewById(R.id.btn_aqui_onde_estou);
        btnOutroLocal = (Button) findViewById(R.id.btn_outro_local);
        txtMoverMarker = (TextView) findViewById(R.id.txt_mover_marker);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapa_enquete);
        mapFragment.getMapAsync(this);

        // Primeiramente precisamos checar a disponibilidade da play services
        if (checkPlayServices()) {
            // Bildando o cliente da google api
            buildGoogleApiClient();
        }

        chamaAquiOndeEstou();
        chamaOutroLocal();
        chamaBotaoOk();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                Context context = getApplicationContext(); //or getActivity(), YourActivity.this, etc.

                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setGravity(Gravity.CENTER);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MAP_PERMISSION_ACCESS_FINE_LOCATION);
        } else {
            if(jaPegouLocalizacao == false) {
                getLastLocation();
                getLocation();
                displayLocation();
                jaPegouLocalizacao = true;
            }
        }


        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                localOcorrido = marker;
                txtMoverMarker.setVisibility(View.GONE);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), (float) 14.5));

            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

        });

    }

    public void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            LatLng me = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            if(verificaLastLocation == false) {
                ondeEstou = me;
                mMap.clear();
                //Codigo para setar um marker laranja da minha localidade
                //MarkerOptions eu = new MarkerOptions().position(me).title("Eu estava aqui quando o anrdoid me localizou pela última vez!!!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                //now = mMap.addMarker(eu);
                if(localOcorrido != null){
                    localOcorrido.remove();
                }
                localOcorrido = mMap.addMarker(new MarkerOptions().position(ondeEstou).title(tipoAcidente).draggable(true).icon(BitmapDescriptorFactory.fromResource( R.mipmap.ic_help)));
                //if(descricao == null || descricao.equals("")){
                    localOcorrido.setSnippet("Local do ocorrido");
                //}else {
                //    localOcorrido.setSnippet(descricao);
                //}
                localOcorrido.showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ondeEstou, (float) 14.5));
                verificaLastLocation = true;
            }
        }
    }

    public void getLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {

                    LatLng me = new LatLng(location.getLatitude(), location.getLongitude());
                    ondeEstou = me;
                    //if(now != null){
                    //    now.remove();
                    //}
                    //Codigo para setar um marker laranja da minha localidade
                    //now = mMap.addMarker(new MarkerOptions().position(me).title("Estou Aqui!!!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    if(verificaCurrentLocation == false) {
                        if(localOcorrido != null){
                            localOcorrido.remove();
                        }
                        localOcorrido = mMap.addMarker(new MarkerOptions().position(ondeEstou).title(tipoAcidente).draggable(true).icon(BitmapDescriptorFactory.fromResource( R.mipmap.ic_help)));
                        //if(descricao == null || descricao.equals("")){
                            localOcorrido.setSnippet("Local do ocorrido");
                        //}else {
                        //    localOcorrido.setSnippet(descricao);
                        //}
                        localOcorrido.showInfoWindow();
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ondeEstou, (float) 14.5));
                        verificaCurrentLocation = true;
                    }


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

    private void chamaOutroLocal() {
        btnOutroLocal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                linearLayoutPreChat.setVisibility(View.GONE);
                btnOk.setVisibility(View.VISIBLE);
            }
        });
    }

    private void chamaBotaoOk(){
        btnOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                clicou = true;
                CaptureScreen();

            }
        });
    }

    private void chamaAquiOndeEstou() {
        btnAquiOndeEstou.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                clicou = true;
                CaptureScreen();
            }
        });
    }

    private void CaptureScreen() {
        try{
            if (mMap != null) {
                if (localOcorrido.getPosition() != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localOcorrido.getPosition(), (float) 14.5));
                } else {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ondeEstou, (float) 14.5));
                }

                GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(Bitmap snapshot) {
                        // TODO Auto-generated method stub
                        try {
                            Intent calltelaChat = new Intent(TelaLocaisPreChat.this, TelaChat.class);

                            ByteArrayOutputStream bao = new ByteArrayOutputStream();
                            snapshot.compress(Bitmap.CompressFormat.JPEG, 40, bao);
                            snapshot.recycle();
                            byte[] byteArray = bao.toByteArray();

                            calltelaChat.putExtra("ByteArray", byteArray);
                            calltelaChat.putExtra("TipoAcidente", tipoAcidente);
                            calltelaChat.putExtra("Descricao", descricao);
                            calltelaChat.putExtra("LocalOcorrido",localOcorrido.getPosition());

                            Toast.makeText(TelaLocaisPreChat.this, "Local capturado com sucesso!", Toast.LENGTH_LONG).show();

                            startActivity(calltelaChat);
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                mMap.snapshot(callback);
            } else {
                Toast.makeText(this, "Mapa não foi inicializado", Toast.LENGTH_LONG).show();
            }
        }catch(Exception ex) {
            Toast.makeText(this, "Mapa não foi inicializado", Toast.LENGTH_LONG).show();
        }
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
            ondeEstou = me;
            //mMap.addMarker(new MarkerOptions().position(ondeEstou).title("Oh você aqui!!!"));
            if(verificaMoveCameraUmaUnicaVez == false) {
                if(localOcorrido != null){
                    localOcorrido.remove();
                }
                localOcorrido = mMap.addMarker(new MarkerOptions().position(ondeEstou).title(tipoAcidente).draggable(true).icon(BitmapDescriptorFactory.fromResource( R.mipmap.ic_help)));
                //if(descricao == null || descricao.equals("")){
                    localOcorrido.setSnippet("Local do ocorrido");
                //}else {
                //    localOcorrido.setSnippet(descricao);
                //}
                localOcorrido.showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ondeEstou, (float) 14.5));
                verificaMoveCameraUmaUnicaVez = true;
            }
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

}
