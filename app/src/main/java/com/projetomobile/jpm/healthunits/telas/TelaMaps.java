package com.projetomobile.jpm.healthunits.telas;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.http.AndroidHttpClient;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.projetomobile.jpm.healthunits.R;
import com.projetomobile.jpm.healthunits.service.APIInterface;
import com.projetomobile.jpm.healthunits.service.ControllerRetrofit;
import com.projetomobile.jpm.healthunits.valueobject.Estabelecimento;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.projetomobile.jpm.healthunits.R.id.map;
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
    private Button btnTracarRota;
    private Button btnListar;
    private ControllerRetrofit controllerRetrofit = new ControllerRetrofit();
    private List<String> listEstab = new ArrayList<String>();
    private LatLng tracaRotaLocalOrigem, tracaRotaLocalDestino;
    private List<LatLng> listaRota;
    private long distancia;
    private Polyline polyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_mapa);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        //Criando todos as views da tela que tem id
        autoCompletePesquisar = (AutoCompleteTextView) findViewById(R.id.pesquisarAutoComplete);
        btnTracarRota = (Button) findViewById(R.id.botaoTracarRota);
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
                        tracaRotaLocalDestino = local;
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
                        tracaRotaLocalDestino = local;
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(local, 10));
                    }
                }

            }
        });
        //======================================================================================================

        //Início da chamada de tela caso tenha
        //this.chamaSearchFilter();
        this.tracarRota();
        this.chamaAdapter();
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
                    tracaRotaLocalOrigem = me;
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

    /* ***************************************** ROTA ***************************************** */

    private void tracarRota() /*throws UnsupportedEncodingException*/ {
        btnTracarRota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EditText etO = (EditText) findViewById(R.id.origin);
                //EditText etD = (EditText) findViewById(R.id.destination);
                //String origin = URLEncoder.encode(etO.getText().toString(), "UTF-8");
                //String destination = URLEncoder.encode(etD.getText().toString(), "UTF-8");

                getRoute(tracaRotaLocalOrigem, tracaRotaLocalDestino);
                //getRoute(origin,destination);
            }
        });
    }

    public void getRoute(final LatLng origin, final LatLng destination){
        new Thread(){
            public void run(){

                /*String url= "http://maps.googleapis.com/maps/api/directions/json?origin="
                        + origin +"&destination="
                        + destination +"&sensor=false";*/
                String url= "http://maps.googleapis.com/maps/api/directions/json?origin="
                        + origin.latitude+","+origin.longitude+"&destination="
                        + destination.latitude+","+destination.longitude+"&sensor=false";


                HttpResponse response;
                HttpGet request;
                AndroidHttpClient client = AndroidHttpClient.newInstance("route");

                request = new HttpGet(url);
                try {
                    response = client.execute(request);
                    final String answer = EntityUtils.toString(response.getEntity());

                    runOnUiThread(new Runnable(){
                        public void run(){
                            try {
                                //Log.i("Script", answer);
                                listaRota = buildJSONRoute(answer);
                                drawRoute();
                            }
                            catch(JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    // PARSER JSON
    public List<LatLng> buildJSONRoute(String json) throws JSONException{
        JSONObject result = new JSONObject(json);
        JSONArray routes = result.getJSONArray("routes");

        distancia = routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getInt("value");

        JSONArray steps = routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
        List<LatLng> lines = new ArrayList<LatLng>();

        for(int i=0; i < steps.length(); i++) {
            Log.i("Script", "STEP: LAT: "+steps.getJSONObject(i).getJSONObject("start_location").getDouble("lat")+" | LNG: "+steps.getJSONObject(i).getJSONObject("start_location").getDouble("lng"));


            String polyline = steps.getJSONObject(i).getJSONObject("polyline").getString("points");

            for(LatLng p : decodePolyline(polyline)) {
                lines.add(p);
            }

            Log.i("Script", "STEP: LAT: "+steps.getJSONObject(i).getJSONObject("end_location").getDouble("lat")+" | LNG: "+steps.getJSONObject(i).getJSONObject("end_location").getDouble("lng"));
        }

        return(lines);
    }

    // DECODE POLYLINE
    private List<LatLng> decodePolyline(String encoded) {

        List<LatLng> listPoints = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
            Log.i("Script", "POL: LAT: "+p.latitude+" | LNG: "+p.longitude);
            listPoints.add(p);
        }
        return listPoints;
    }

    public void drawRoute(){
        PolylineOptions po;

        if(polyline == null){
            po = new PolylineOptions();

            for(int i = 0, tam = listaRota.size(); i < tam; i++){
                po.add(listaRota.get(i));
            }

            po.color(Color.BLUE).width(4);
            polyline = mMap.addPolyline(po);
        }
        else{
            polyline.setPoints(listaRota);
        }
    }

}
