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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
import static com.projetomobile.jpm.healthunits.adaptadores.MyAdapterEstabelecimento.estabeleci;
import static com.projetomobile.jpm.healthunits.adaptadores.MyAdapterEstabelecimento.tracaOrigem;
import static com.projetomobile.jpm.healthunits.service.ControllerRetrofit.BASE_URL;

public class TelaMaps extends FragmentActivity implements OnMapReadyCallback  /*, ConnectionCallbacks, OnConnectionFailedListener*/ {


    public static final int MAP_PERMISSION_ACCESS_FINE_LOCATION = 9999;

    private GoogleMap mMap;
    private LocationManager locationManager;

    private APIInterface apiEstabelecimento;

    private AutoCompleteTextView autoCompletePesquisar;
    private ImageView iconePesquisar;
    private ImageView btnListar;
    private ControllerRetrofit controllerRetrofit = new ControllerRetrofit();
    private List<String> listEstab = new ArrayList<String>();
    private List<Estabelecimento> listEst = new ArrayList<Estabelecimento>();
    public LatLng tracaRotaLocalOrigem, tracaRotaLocalDestino, tracaRotaLocalOrigemVerifica;
    private List<LatLng> listaRota;
    private long distancia;
    private Polyline polyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_mapa);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        // TEMOS QUE CRIAR UM NOVO MAPA!!!!
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_enquete);
        mapFragment.getMapAsync(this);

        //Criando todos as views da tela que tem id
        autoCompletePesquisar = (AutoCompleteTextView) findViewById(R.id.pesquisarAutoComplete);
        btnListar = (ImageView) findViewById(R.id.botaoListar);
        iconePesquisar = (ImageView) findViewById(R.id.pesquisarIcon);

        //=========================Pesquisar================================================================
        iconePesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int j = 0; j<listEst.size(); j++) {
                    if (autoCompletePesquisar.getText().toString().toUpperCase().equals(listEst.get(j).getNomeFantasia())) {
                        Float latitude = listEst.get(j).getLatitude();
                        Float longitude = listEst.get(j).getLongitude();
                        LatLng local = new LatLng(latitude, longitude);
                        tracaRotaLocalDestino = local;
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(local, (float) 14.5));
                        try {
                            if(tracaRotaLocalOrigem != null && tracaRotaLocalDestino != null) {
                                getRoute(tracaRotaLocalOrigem, tracaRotaLocalDestino);
                                //getRoute(origin,destination);
                            }else {
                                Toast.makeText(TelaMaps.this,"Por favor escolha um destino!",Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception e){
                            Toast.makeText(TelaMaps.this,"Por favor escolha um destino!",Toast.LENGTH_LONG).show();
                        }
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
                for(int j = 0; j<listEst.size(); j++) {
                    if (selection.equals(listEst.get(j).getNomeFantasia())) {
                        Float latitude = listEst.get(j).getLatitude();
                        Float longitude = listEst.get(j).getLongitude();
                        LatLng local = new LatLng(latitude, longitude);
                        tracaRotaLocalDestino = local;
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(local, (float) 14.5));
                        try {
                            if(tracaRotaLocalOrigem != null && tracaRotaLocalDestino != null) {
                                getRoute(tracaRotaLocalOrigem, tracaRotaLocalDestino);
                                //getRoute(origin,destination);
                            }else {
                                Toast.makeText(TelaMaps.this,"Por favor escolha um destino!",Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception e){
                            Toast.makeText(TelaMaps.this,"Por favor escolha um destino!",Toast.LENGTH_LONG).show();
                        }
                    }
                }


            }
        });
        //======================================================================================================


        this.chamaListaEstabelecimento();
    }

    private void chamaListaEstabelecimento() {
        btnListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callTelaListaEstabelecimento = new Intent(TelaMaps.this, TelaListaEstabelecimento.class);
                startActivity(callTelaListaEstabelecimento);
                finish();
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
        mMap.setMyLocationEnabled(false);
        mMap.setBuildingsEnabled(true);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                try{
                    if(tracaRotaLocalOrigem != null && marker.getPosition() != null){
                        getRoute(tracaRotaLocalOrigem,marker.getPosition());
                    }
                }catch (Exception e){

                }
                return false;
            }
        });
    }

    public void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            LatLng me = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            tracaRotaLocalOrigem = me;
            MarkerOptions eu = new MarkerOptions().position(me).title("Eu estava aqui quando o anrdoid me localizou pela última vez!!!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            if(tracaRotaLocalOrigemVerifica != null){
                //mMap.addMarker(eu.visible(false));
                mMap.clear();
            }else{
                mMap.clear();
                mMap.addMarker(eu.visible(true));
            }

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(me, (float) 14.5));

            Call<List<Estabelecimento>> callLatLong = apiEstabelecimento.listEstabelecimentoRaio(String.valueOf(me.latitude), String.valueOf(me.longitude),"500");
            callLatLong.enqueue(new Callback<List<Estabelecimento>>() {
                @Override
                public void onResponse(Call<List<Estabelecimento>> call, Response<List<Estabelecimento>> response) {
                    if(response.isSuccessful()){
                        for(Estabelecimento estabelecimento : response.body()){
                            listEstab.add(estabelecimento.getNomeFantasia());
                            listEst.add(estabelecimento);
                            Float lat = estabelecimento.getLatitude();
                            Float longi = estabelecimento.getLongitude();
                            String nomeDoEstabelecimento = estabelecimento.getNomeFantasia();
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat,longi)).title(nomeDoEstabelecimento).icon(BitmapDescriptorFactory.fromResource( R.mipmap.marker)));
                        }
                        Log.e("","PASSOU222!");
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
                    tracaRotaLocalOrigem = me;
                    tracaRotaLocalOrigemVerifica = me;
                    //mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(me).title("Estou Aqui!!!").icon( BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_ORANGE )));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(me, (float) 14.5));

                    Call<List<Estabelecimento>> callLatLong = apiEstabelecimento.listEstabelecimentoRaio(String.valueOf(me.latitude), String.valueOf(me.longitude),"500");
                    callLatLong.enqueue(new Callback<List<Estabelecimento>>() {
                        @Override
                        public void onResponse(Call<List<Estabelecimento>> call, Response<List<Estabelecimento>> response) {
                            if(response.isSuccessful()){
                                for(Estabelecimento estabelecimento : response.body()){
                                    listEstab.add(estabelecimento.getNomeFantasia());
                                    listEst.add(estabelecimento);
                                    Float lat = estabelecimento.getLatitude();
                                    Float longi = estabelecimento.getLongitude();
                                    String nomeDoEstabelecimento = estabelecimento.getNomeFantasia();
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(lat,longi)).title(nomeDoEstabelecimento).icon(BitmapDescriptorFactory.fromResource( R.mipmap.marker)));
                                }
                                Log.e("","PASSOU222!");
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

        apiEstabelecimento = retrofit.create(APIInterface.class);

        Call<List<Estabelecimento>> call = apiEstabelecimento.listEstabelecimento();
        call.enqueue(new Callback<List<Estabelecimento>>() {
            @Override
            public void onResponse(Call<List<Estabelecimento>> call, Response<List<Estabelecimento>> response) {
                if(response.isSuccessful()){
                    for(Estabelecimento estabelecimento : response.body()){
                        listEstab.add(estabelecimento.getNomeFantasia());
                        listEst.add(estabelecimento);
                        Float lat = estabelecimento.getLatitude();
                        Float longi = estabelecimento.getLongitude();
                        String nomeDoEstabelecimento = estabelecimento.getNomeFantasia();
                        mMap.addMarker(new MarkerOptions().position(new LatLng(lat,longi)).title(nomeDoEstabelecimento).icon(BitmapDescriptorFactory.fromResource( R.mipmap.marker)));
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

    @Override
    protected void onResume() {
        super.onResume();
        try{
            LatLng tracaDestino = new LatLng(Double.parseDouble(String.valueOf(estabeleci.getLatitude())),Double.parseDouble(String.valueOf(estabeleci.getLongitude())));
            if(tracaOrigem != null && tracaDestino != null){
                getRoute(tracaOrigem,tracaDestino);
            }
        }catch (Exception e){

        }
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
