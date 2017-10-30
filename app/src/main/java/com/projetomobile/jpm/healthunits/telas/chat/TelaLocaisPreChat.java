package com.projetomobile.jpm.healthunits.telas.chat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.projetomobile.jpm.healthunits.R;

import java.io.ByteArrayOutputStream;

import static com.projetomobile.jpm.healthunits.telas.TelaMaps.MAP_PERMISSION_ACCESS_FINE_LOCATION;

public class TelaLocaisPreChat extends AppCompatActivity implements OnMapReadyCallback {

    public LinearLayout linearLayoutPreChat;
    public TextView txtQualLocalOcorreu;
    public Button btnAquiOndeEstou, btnOutroLocal, btnOk;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private SupportMapFragment mapFragment;
    private LatLng ondeEstou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_locais_pre_chat);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btnOk = (Button) findViewById(R.id.btn_ok);
        linearLayoutPreChat = (LinearLayout) findViewById(R.id.linear_layout_locais_chat);
        txtQualLocalOcorreu = (TextView) findViewById(R.id.txt_qual_local_ocorreu);
        btnAquiOndeEstou = (Button) findViewById(R.id.btn_aqui_onde_estou);
        btnOutroLocal = (Button) findViewById(R.id.btn_outro_local);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapa_enquete);
        mapFragment.getMapAsync(this);

        chamaAquiOndeEstou();
        chamaOutroLocal();
        chamaBotaoOk();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MAP_PERMISSION_ACCESS_FINE_LOCATION);
        } else {
            getLastLocation();
            getLocation();
        }

    }

    public void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            LatLng me = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            ondeEstou = me;
            mMap.clear();
            MarkerOptions eu = new MarkerOptions().position(me).title("Eu estava aqui quando o anrdoid me localizou pela última vez!!!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            mMap.addMarker(eu);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(me, (float) 14.5));

        }
    }

    public void getLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    LatLng me = new LatLng(location.getLatitude(), location.getLongitude());
                    ondeEstou = me;
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(me).title("Estou Aqui!!!").icon( BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_ORANGE )));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(me, (float) 14.5));


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
                CaptureScreen();

            }
        });
    }

    private void chamaAquiOndeEstou() {
        btnAquiOndeEstou.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                CaptureScreen();
            }
        });
    }

    private void CaptureScreen() {
        if(mMap != null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ondeEstou, (float) 14.5));
            GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
                @Override
                public void onSnapshotReady(Bitmap snapshot) {
                    // TODO Auto-generated method stub
                    try {
                        Intent calltelaChat = new Intent(TelaLocaisPreChat.this,TelaChat.class);

                        Bitmap resizedBitmap = Bitmap.createBitmap(snapshot, 0, 400, snapshot.getWidth(), 600);

                        ByteArrayOutputStream bao = new ByteArrayOutputStream();
                        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, bao);
                        resizedBitmap.recycle();
                        byte[] byteArray = bao.toByteArray();
                        String imageB64 = Base64.encodeToString(byteArray, Base64.DEFAULT);

                        calltelaChat.putExtra("ImageB64", imageB64);

                        Toast.makeText(TelaLocaisPreChat.this, "Local capturado com sucesso!", Toast.LENGTH_LONG).show();

                        startActivity(calltelaChat);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            mMap.snapshot(callback);
        }
        else{
            Toast.makeText(this, "Mapa não foi inicializado", Toast.LENGTH_LONG).show();
            return ;
        }
    }

}
