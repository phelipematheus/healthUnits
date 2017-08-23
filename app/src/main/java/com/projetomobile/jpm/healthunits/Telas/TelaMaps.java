package com.projetomobile.jpm.healthunits.Telas;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.projetomobile.jpm.healthunits.R;

public class TelaMaps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    EditText editPesquisar;
    Button btnAlterarFiltros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Criando todos as views da tela que tem id
        editPesquisar = (EditText)findViewById(R.id.pesquisarEdit);
        btnAlterarFiltros = (Button)findViewById(R.id.botaoAlterarFiltros);

        //Início da chamada de tela caso tenha
        this.chamaSearchFilter();
    }

    private void chamaSearchFilter(){
        btnAlterarFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callTelaSearchFilter = new Intent(TelaMaps.this,TelaSearchFilter.class);
                startActivity(callTelaSearchFilter);
                finish();
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    //Tratando o botão voltar
    @Override
    public void onBackPressed() {
        Intent callTelaSearchFilter = new Intent(TelaMaps.this,TelaSearchFilter.class);
        startActivity(callTelaSearchFilter);
        finish();
    }
}