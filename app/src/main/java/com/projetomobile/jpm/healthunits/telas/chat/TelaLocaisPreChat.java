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
import android.util.Log;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.projetomobile.jpm.healthunits.R;
import com.projetomobile.jpm.healthunits.valueobject.Estabelecimento;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.projetomobile.jpm.healthunits.R.id.map;
import static com.projetomobile.jpm.healthunits.telas.TelaMaps.MAP_PERMISSION_ACCESS_FINE_LOCATION;

public class TelaLocaisPreChat extends AppCompatActivity implements OnMapReadyCallback {

    public LinearLayout linearLayoutPreChat;
    public TextView txtQualLocalOcorreu;
    public Button btnAquiOndeEstou, btnOutroLocal, btnOk;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LatLng novoLocal;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_locais_pre_chat);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btnOk = (Button) findViewById(R.id.btn_ok);
        linearLayoutPreChat = (LinearLayout) findViewById(R.id.linearLayout_preChat);
        txtQualLocalOcorreu = (TextView) findViewById(R.id.txt_qual_local_ocorreu);
        btnAquiOndeEstou = (Button) findViewById(R.id.btn_aqui_onde_estou);
        btnOutroLocal = (Button) findViewById(R.id.btn_outro_local);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        chamaAquiOndeEstou();
        chamaOutroLocal();
        chamaBotaoOk();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(novoLocal, (float) 14.5));
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MAP_PERMISSION_ACCESS_FINE_LOCATION);
        } else {
            getLastLocation();
            getLocation();
        }

    }

    public void getLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    LatLng me = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(me, (float) 14.5));
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

    public void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {

            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            LatLng me = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(me, (float) 14.5));

            MarkerOptions eu = new MarkerOptions().position(me).title("Eu estava aqui quando o anrdoid me localizou pela última vez!!!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(me, (float) 14.5));

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
            GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
                @Override
                public void onSnapshotReady(Bitmap snapshot) {
                    // TODO Auto-generated method stub
                    try {
                        Intent calltelaChat = new Intent(TelaLocaisPreChat.this,TelaChat.class);
                        calltelaChat.putExtra("BITMAP", snapshot);
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
