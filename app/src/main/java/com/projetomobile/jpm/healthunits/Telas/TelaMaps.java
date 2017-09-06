package com.projetomobile.jpm.healthunits.Telas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.projetomobile.jpm.healthunits.R;
import com.projetomobile.jpm.healthunits.Service.ControllerRetrofit;

public class TelaMaps extends FragmentActivity implements OnMapReadyCallback /*, ConnectionCallbacks, OnConnectionFailedListener*/ {

    private GoogleMap mMap;
    private EditText editPesquisar;
    private Button btnAlterarFiltros;
    private ControllerRetrofit controllerRetrofit = new ControllerRetrofit();
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Button btnListar;

    private void setUpMap() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e("ERROU", "ERROU O MAPA");
            System.out.println("ERRO NO MAPA");
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, new LocationListener() {
            @Override
            public void onLocationChanged(Location loc) {
                String s = "Minha cidade atual: Brasília";
                System.out.println(s);

                //Localização dos outros
                for (int j = 0; j < controllerRetrofit.getListaEstabelecimentos().size(); j++) {
                    Float lat = controllerRetrofit.getListaEstabelecimentos().get(j).getLatitude();
                    Float longi = controllerRetrofit.getListaEstabelecimentos().get(j).getLongitude();
                    String nomeDoEstabelecimento = controllerRetrofit.getListaEstabelecimentos().get(j).getNomeFantasia();
                    mMap.addMarker(new MarkerOptions().position(new LatLng(lat, longi)).title(nomeDoEstabelecimento));
                }

                mMap.addMarker(new MarkerOptions().position(new LatLng(loc.getLatitude(), loc.getLongitude())).snippet(s).title("YOU!!!"));
                LatLng voce = new LatLng(loc.getLatitude(), loc.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(voce, 12.0f));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_mapa);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Criando todos as views da tela que tem id
        editPesquisar = (EditText) findViewById(R.id.pesquisarEdit);
        btnAlterarFiltros = (Button) findViewById(R.id.botaoAlterarFiltros);
        btnListar = (Button) findViewById(R.id.botaoListar);

        // Primeiramente precisamos checar a disponibilidade da play services
        //if (checkPlayServices()) {
        // Bildando o cliente da google api
        //    buildGoogleApiClient();
        //}

        //Início da chamada de tela caso tenha
        this.chamaSearchFilter();
        this.chamaAdapter();
    }

/*
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
    */

    private void chamaSearchFilter() {
        btnAlterarFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void chamaAdapter() {
        btnListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callTelaListaEstabelecimento = new Intent(TelaMaps.this, TelaListaEstabelecimento.class);
                startActivity(callTelaListaEstabelecimento);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //displayLocation();
        setUpMap();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
/*
    private void displayLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            //Minha localização
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            LatLng voce = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(voce).title("Oh você aqui!!!"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(voce, 12.0f));

            //Localização dos outros
            for(int j = 0; j<controllerRetrofit.getListaEstabelecimentos().size(); j++){
                Float lat = controllerRetrofit.getListaEstabelecimentos().get(j).getLatitude();
                Float longi = controllerRetrofit.getListaEstabelecimentos().get(j).getLongitude();
                String nomeDoEstabelecimento = controllerRetrofit.getListaEstabelecimentos().get(j).getNomeFantasia();
                mMap.addMarker(new MarkerOptions().position(new LatLng(lat,longi)).title(nomeDoEstabelecimento));
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

    /**
     * Google api callback methods

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
    */
}
