package com.projetomobile.jpm.healthunits.telas;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.projetomobile.jpm.healthunits.R;
import com.projetomobile.jpm.healthunits.service.APIInterface;
import com.projetomobile.jpm.healthunits.service.ControllerRetrofit;
import com.projetomobile.jpm.healthunits.valueobject.Estabelecimento;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.projetomobile.jpm.healthunits.service.ControllerRetrofit.BASE_URL;

public class TelaMaps extends FragmentActivity implements OnMapReadyCallback  /*, ConnectionCallbacks, OnConnectionFailedListener*/ {

    /*
    private GoogleMap mMap;
    private EditText editPesquisar;
    private Button btnAlterarFiltros;
    private ControllerRetrofit controllerRetrofit = new ControllerRetrofit();
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    public static final int MAP_PERMISSION_ACCESS_COURSE_LOCATION = 9999;
    private Button btnListar;

    =====================Modelo antigo=====================================
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

    public static final int MAP_PERMISSION_ACCESS_FINE_LOCATION = 9999;

    private GoogleMap mMap;
    private LocationManager locationManager;

    private AutoCompleteTextView autoCompletePesquisar;
    private ImageView iconePesquisar;
    private Button btnAlterarFiltros;
    private Button btnListar;
    private ControllerRetrofit controllerRetrofit = new ControllerRetrofit();
    private List<String> listEstab = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_mapa);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Criando todos as views da tela que tem id
        autoCompletePesquisar = (AutoCompleteTextView) findViewById(R.id.pesquisarAutoComplete);
        btnAlterarFiltros = (Button) findViewById(R.id.botaoAlterarFiltros);
        btnListar = (Button) findViewById(R.id.botaoListar);
        iconePesquisar = (ImageView) findViewById(R.id.pesquisarIcon);

        //=========================Pesquisar================================================================
        iconePesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int j = 0; j<controllerRetrofit.getListaEstabelecimentos().size(); j++) {
                    if (autoCompletePesquisar.getText().toString().toUpperCase().equals(controllerRetrofit.getListaEstabelecimentos().get(j).getNomeFantasia())) {
                        Float latitude = controllerRetrofit.getListaEstabelecimentos().get(j).getLatitude();
                        Float longitude = controllerRetrofit.getListaEstabelecimentos().get(j).getLongitude();
                        LatLng local = new LatLng(latitude, longitude);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(local, 10));
                    }
                }
            }
        });

        //====================Auto Complete Pesquisar========================================================
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listEstab);

        // Dropdown layout style
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        autoCompletePesquisar.setThreshold(1);//Começa a procurar do primeiro caractere
        autoCompletePesquisar.setAdapter(adapter);
        autoCompletePesquisar.setTextColor(Color.RED);//Muda a cor do texto
        autoCompletePesquisar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String)parent.getItemAtPosition(position);
                for(int j = 0; j<controllerRetrofit.getListaEstabelecimentos().size(); j++) {
                    if (selection.equals(controllerRetrofit.getListaEstabelecimentos().get(j).getNomeFantasia())) {
                        Float latitude = controllerRetrofit.getListaEstabelecimentos().get(j).getLatitude();
                        Float longitude = controllerRetrofit.getListaEstabelecimentos().get(j).getLongitude();
                        LatLng local = new LatLng(latitude, longitude);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(local, 10));
                    }
                }

            }
        });
        //======================================================================================================

        //Início da chamada de tela caso tenha
        this.chamaSearchFilter();
        this.chamaAdapter();
    }

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

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MAP_PERMISSION_ACCESS_FINE_LOCATION);
            } else {
                getLastLocation();
                getLocation();
            }
        mMap.setMyLocationEnabled(true);
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            LatLng me = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(me).title("Eu estava aqui quando o anrdoid me localizou pela última vez!!!"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(me, 10));
        }
    }

    private void getLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    LatLng me = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(me).title("Estou Aqui!!!"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(me, 10));
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MAP_PERMISSION_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        getLastLocation();
                        getLocation();
                } else {
                    //Permissão negada
                }
                return;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();

        APIInterface apiEstabelecimento = retrofit.create(APIInterface.class);

        Call<List<Estabelecimento>> call = apiEstabelecimento.listEstabelecimento();
        call.enqueue(new Callback<List<Estabelecimento>>() {
            @Override
            public void onResponse(Call<List<Estabelecimento>> call, Response<List<Estabelecimento>> response) {
                if(response.isSuccessful()){
                    for(Estabelecimento estabelecimento : response.body()){
                        listEstab.add(estabelecimento.getNomeFantasia());
                        Float lat = estabelecimento.getLatitude();
                        Float longi = estabelecimento.getLongitude();
                        String nomeDoEstabelecimento = estabelecimento.getNomeFantasia();
                        mMap.addMarker(new MarkerOptions().position(new LatLng(lat,longi)).title(nomeDoEstabelecimento));
                    }
                    Log.e("","PASSOU!");
                }else {
                    Log.e("", "NAO PASSOU");
                }
            }

            @Override
            public void onFailure(Call<List<Estabelecimento>> call, Throwable t) {

            }
        });
    }
}
