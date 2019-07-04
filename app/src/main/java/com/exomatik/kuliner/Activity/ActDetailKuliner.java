package com.exomatik.kuliner.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.exomatik.kuliner.Model.ModelKuliner;
import com.exomatik.kuliner.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ActDetailKuliner extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {
    public static ModelKuliner dataKuliner;
    private ImageView back;
    private TextView textNama, textDesc, textAlamat, textKapal, textDanpal, textTelp;
    private GoogleMap mMap;
    protected GeoDataClient mGeoDataClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_detail_kuliner);

        back = (ImageView) findViewById(R.id.back);
        textNama = (TextView) findViewById(R.id.text_prov);
        textDesc = (TextView) findViewById(R.id.text_location);
        textAlamat = (TextView) findViewById(R.id.text_alamat);

        init();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataKuliner = null;
                startActivity(new Intent(ActDetailKuliner.this, MainActivity.class));
                finish();
            }
        });
    }

    private void init() {
        textNama.setText(dataKuliner.getNama());
        textDesc.setText(dataKuliner.getDesc());
        textAlamat.setText(dataKuliner.getAlamat());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(ActDetailKuliner.this);
    }

    @Override
    public void onBackPressed() {
        dataKuliner = null;
        startActivity(new Intent(ActDetailKuliner.this, MainActivity.class));
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney;

        String replace = dataKuliner.getLocationPoint().replace("lat/lng: (", "");
        replace = replace.replace(")", "");
        String lat[] = replace.split(",");
        sydney = new LatLng(Float.parseFloat(lat[0]), Float.parseFloat(lat[1]));

        mMap.addMarker(new MarkerOptions().position(sydney).title(dataKuliner.getNama()));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16.0f));

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Periksa Koneksi Anda", Toast.LENGTH_SHORT).show();
    }
}
