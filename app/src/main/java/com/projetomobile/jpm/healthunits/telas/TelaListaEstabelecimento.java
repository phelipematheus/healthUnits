package com.projetomobile.jpm.healthunits.telas;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.projetomobile.jpm.healthunits.R;
import com.projetomobile.jpm.healthunits.adaptadores.MyAdapterEstabelecimento;
import com.projetomobile.jpm.healthunits.service.APIInterface;
import com.projetomobile.jpm.healthunits.valueobject.Estabelecimento;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.projetomobile.jpm.healthunits.adaptadores.MyAdapterEstabelecimento.verificaSePodeFinalizarActivity;
import static com.projetomobile.jpm.healthunits.service.ControllerRetrofit.BASE_URL;
import static com.projetomobile.jpm.healthunits.telas.TelaMaps.MAP_PERMISSION_ACCESS_FINE_LOCATION;
import static com.projetomobile.jpm.healthunits.telas.TelaMaps.PLAY_SERVICES_RESOLUTION_REQUEST;

public class TelaListaEstabelecimento extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private LatLng origem;
    private LocationManager locationManager;
    private APIInterface apiEstabelecimento;

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;

    private List<Estabelecimento> listEst = new ArrayList<Estabelecimento>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_lista_estabelecimento);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        //        .findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);

        // Primeiramente precisamos checar a disponibilidade da play services
        if (checkPlayServices()) {
            // Bildando o cliente da google api
            buildGoogleApiClient();
        }

        if(verificaSePodeFinalizarActivity == true){
            finish();
            verificaSePodeFinalizarActivity = false;
        }

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        apiEstabelecimento = retrofit.create(APIInterface.class);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MAP_PERMISSION_ACCESS_FINE_LOCATION);
        } else {
            getLastLocation();
            getLocation();
            displayLocation();
        }

        Call<List<Estabelecimento>> call = apiEstabelecimento.listEstabelecimento();
        call.enqueue(new Callback<List<Estabelecimento>>() {
            @Override
            public void onResponse(Call<List<Estabelecimento>> call, Response<List<Estabelecimento>> response) {

                if(response.isSuccessful()){
                    for(Estabelecimento e : response.body()){
                        listEst.add(e);

                    }

                    if(origem != null) {
                        mAdapter = new MyAdapterEstabelecimento(TelaListaEstabelecimento.this, listEst,origem);
                        Log.e("", "Funcionou");
                        recyclerView.setAdapter(mAdapter);
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Estabelecimento>> call, Throwable t) {
                Log.e("","Deu merda");
            }
        });



    }

    //================MAPS====================================================================


    public void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            LatLng me = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            origem = me;
            //mMap.addMarker(new MarkerOptions().position(me).title("Eu estava aqui quando o anrdoid me localizou pela última vez!!!"));
            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(me, 10));
            Call<List<Estabelecimento>> callLatLong = apiEstabelecimento.listEstabelecimentoRaio(String.valueOf(origem.latitude), String.valueOf(origem.longitude),"200");
            callLatLong.enqueue(new Callback<List<Estabelecimento>>() {
                @Override
                public void onResponse(Call<List<Estabelecimento>> call, Response<List<Estabelecimento>> response) {
                    if(response.isSuccessful()){
                        for(Estabelecimento e : response.body()){
                            listEst.add(e);

                        }
                        //mAdapter = new MyAdapterEstabelecimento(TelaListaEstabelecimento.this, listEst);
                        Log.e("","Funcionou");
                        //recyclerView.setAdapter(mAdapter);
                    }else {
                        Log.e("", "NAO PASSOU222");
                    }
                }

                @Override
                public void onFailure(Call<List<Estabelecimento>> call, Throwable t) {
                    Log.e("", "NAO PASSOU333");
                }
            });
        }
    }

    public void getLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    LatLng me = new LatLng(location.getLatitude(), location.getLongitude());
                    origem = me;
                    //mMap.addMarker(new MarkerOptions().position(me).title("Estou Aqui!!!"));
                    //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(me, 10));
                    Call<List<Estabelecimento>> callLatLong = apiEstabelecimento.listEstabelecimentoRaio(String.valueOf(origem.latitude), String.valueOf(origem.longitude),"200");
                    callLatLong.enqueue(new Callback<List<Estabelecimento>>() {
                        @Override
                        public void onResponse(Call<List<Estabelecimento>> call, Response<List<Estabelecimento>> response) {
                            if(response.isSuccessful()){
                                for(Estabelecimento e : response.body()){
                                    listEst.add(e);

                                }
                                //mAdapter = new MyAdapterEstabelecimento(TelaListaEstabelecimento.this, listEst);
                                Log.e("","Funcionou");
                                //recyclerView.setAdapter(mAdapter);
                            }else {
                                Log.e("", "NAO PASSOU222");
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Estabelecimento>> call, Throwable t) {
                            Log.e("", "NAO PASSOU333");
                        }
                    });
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


}
