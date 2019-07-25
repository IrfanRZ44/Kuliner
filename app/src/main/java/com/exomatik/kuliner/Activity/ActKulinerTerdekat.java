package com.exomatik.kuliner.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.exomatik.kuliner.Adapter.RecyclerLihatKuliner;
import com.exomatik.kuliner.Adapter.RecyclerLihatKulinerTerdekat;
import com.exomatik.kuliner.Featured.ItemClickSupport;
import com.exomatik.kuliner.Model.ModelKuliner;
import com.exomatik.kuliner.Model.ModelKulinerTerdekat;
import com.exomatik.kuliner.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class ActKulinerTerdekat extends AppCompatActivity implements ItemClickSupport.OnItemClickListener, LocationListener {
    private RecyclerView rcKuliner;
    private TextView textNothing, textTitle;
    private ImageView back, imgCari;
    private ArrayList<ModelKulinerTerdekat> listKuliner = new ArrayList<ModelKulinerTerdekat>();
    private LocationManager locationManager;
    private double latNow, lngNow;
    private ShimmerLayout shimmerLoad;
    int jarakTerdekat = 3; //jarak kuliner terdekat dalam km

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_kuliner_terdekat);

        imgCari = (ImageView) findViewById(R.id.img_cari);
        rcKuliner = (RecyclerView) findViewById(R.id.rc_kuliner);
        textNothing = (TextView) findViewById(R.id.text_nothing);
        textTitle = (TextView) findViewById(R.id.text_title_anggota);
        back = (ImageView) findViewById(R.id.back);
        shimmerLoad = (ShimmerLayout) findViewById(R.id.shimmer_load);

        CheckPermission();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
            getLocation();
        }
        ItemClickSupport.addTo(rcKuliner).setOnItemClickListener(this);

        textTitle.setText("Kuliner Terdekat");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActKulinerTerdekat.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActKulinerTerdekat.this, MainActivity.class));
        finish();
    }

    private void getDataKuliner() {
        shimmerLoad.startShimmerAnimation();
        shimmerLoad.setVisibility(View.VISIBLE);
        textNothing.setVisibility(View.GONE);
        FirebaseDatabase.getInstance()
                .getReference("kuliner")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            boolean cek = true;

                            Iterator localIterator = dataSnapshot.getChildren().iterator();
                            listKuliner.removeAll(listKuliner);
                            listKuliner = new ArrayList<ModelKulinerTerdekat>();
                            while (localIterator.hasNext()) {
                                ModelKuliner localDataUser = (ModelKuliner) ((DataSnapshot) localIterator.next()).getValue(ModelKuliner.class);

                                String replace = localDataUser.getLocationPoint().replace("lat/lng: (", "");
                                replace = replace.replace(")", "");
                                String lat[] = replace.split(",");

                                //menyeleksi apakah jarak antara lokasi hp skrng dengan jarak lokasi kuliner di database kurang dari jarak yang ditentukan
                                //jika benar kurang dari jarak yang ditentukan maka data kuliner tersebut dimunculkan
                                if (distance(Double.parseDouble(lat[0]), Double.parseDouble(lat[1]), latNow, lngNow) < jarakTerdekat) {
                                    listKuliner.add(new ModelKulinerTerdekat(localDataUser.getNama(), localDataUser.getDesc(),
                                            localDataUser.getAlamat(), localDataUser.getLocationPoint(), localDataUser.getFoto(),
                                            localDataUser.getId(), localDataUser.getFavorit(),
                                            Double.toString(distance(Double.parseDouble(lat[0]), Double.parseDouble(lat[1]), latNow, lngNow))));
                                    cek = false;
                                }
                            }

                            if (cek) {
                                textNothing.setText("Tidak ada data kuliner terdekat di daerahmu");
                                textNothing.setVisibility(View.VISIBLE);
                            } else {
                                RecyclerLihatKulinerTerdekat adapterLihatKelas = new RecyclerLihatKulinerTerdekat(listKuliner);
                                LinearLayoutManager localLinearLayoutManager = new LinearLayoutManager(ActKulinerTerdekat.this, 1, false);
                                rcKuliner.setLayoutManager(localLinearLayoutManager);
                                rcKuliner.setNestedScrollingEnabled(false);
                                rcKuliner.setAdapter(adapterLihatKelas);
                            }
                        } else {
                            textNothing.setVisibility(View.VISIBLE);
                        }
                        shimmerLoad.stopShimmerAnimation();
                        shimmerLoad.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        shimmerLoad.stopShimmerAnimation();
                        shimmerLoad.setVisibility(View.GONE);
                        textNothing.setText("Error mengambil data");
                        textNothing.setVisibility(View.VISIBLE);
                        Toast.makeText(ActKulinerTerdekat.this, "Gagal Mengambil Data Terbaru", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        ActDetailKuliner.dataKuliner = new ModelKuliner(listKuliner.get(position).getNama(), listKuliner.get(position).getDesc()
                , listKuliner.get(position).getAlamat(), listKuliner.get(position).getLocationPoint(), listKuliner.get(position).getFoto()
                , listKuliner.get(position).getId(), listKuliner.get(position).getFavorit());
        startActivity(new Intent(ActKulinerTerdekat.this, ActDetailKuliner.class));
        finish();
    }

    /* Request updates at startup */
    @Override
    public void onResume() {
        super.onResume();
        getLocation();
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, ActKulinerTerdekat.this);
            getLokasi();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void getLokasi() {
        // GET CURRENT LOCATION
        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
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
        mFusedLocation.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // Do it all with location
                    Log.d("My Current location", "Lat : " + location.getLatitude() + " Long : " + location.getLongitude());
                    latNow = location.getLatitude();
                    lngNow = location.getLongitude();
                    getDataKuliner();
                    // Display in Toast
//                    Toast.makeText(MainActivity.this,
//                            "Lat : " + location.getLatitude() + " Long : " + location.getLongitude(),
//                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void CheckPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latNow = location.getLatitude();
        lngNow = location.getLongitude();
        getDataKuliner();
    }


    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(ActKulinerTerdekat.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(ActKulinerTerdekat.this, "Enabled new provider!" + provider,
                Toast.LENGTH_SHORT).show();
    }

    private double distance(double lat1, double lng1, double lat2, double lng2) {

//        double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output
        double earthRadius = 6371; // in miles, change to 6371 for kilometer output

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double dist = earthRadius * c;

        return dist; // output distance, in MILES
    }
}
