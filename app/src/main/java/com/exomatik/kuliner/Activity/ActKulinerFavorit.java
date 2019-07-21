package com.exomatik.kuliner.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.exomatik.kuliner.Adapter.RecyclerLihatKuliner;
import com.exomatik.kuliner.Featured.ItemClickSupport;
import com.exomatik.kuliner.Model.ModelKuliner;
import com.exomatik.kuliner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class ActKulinerFavorit extends AppCompatActivity implements ItemClickSupport.OnItemClickListener {
    private RecyclerView rcKuliner;
    private TextView textNothing, textHeader;
    private ImageView back, btnCari;
    private EditText etCari;
    private ArrayList<ModelKuliner> listKuliner = new ArrayList<ModelKuliner>();
    private ShimmerLayout shimmerLoad;
    private boolean cari = false;
    private RelativeLayout rlCari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_kuliner_favorit);

        rlCari = (RelativeLayout) findViewById(R.id.rl_et_cari);
        textHeader = (TextView) findViewById(R.id.text_title_anggota);
        rcKuliner = (RecyclerView) findViewById(R.id.rc_kuliner);
        textNothing = (TextView) findViewById(R.id.text_nothing);
        back = (ImageView) findViewById(R.id.back);
        btnCari = (ImageView) findViewById(R.id.img_cari);
        etCari = (EditText) findViewById(R.id.et_cari);
        shimmerLoad = (ShimmerLayout) findViewById(R.id.shimmer_load);

        //mengambil data dari realtime firebase
        getDataKuliner();

        ItemClickSupport.addTo(rcKuliner).setOnItemClickListener(this);

        btnCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cari){
                    String text = etCari.getText().toString();
                    getCariKuliner(text);
                }
                else {
                    textHeader.setVisibility(View.GONE);
                    rlCari.setVisibility(View.VISIBLE);
                    cari = true;
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActKulinerFavorit.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActKulinerFavorit.this, MainActivity.class));
        finish();
    }

    private void getDataKuliner() {
        shimmerLoad.startShimmerAnimation();
        //shimmer stop berarti menghentikan animasinya
        shimmerLoad.setVisibility(View.VISIBLE);
        //tampilan apapun yang di set visibility gone,, menghilangkan tampilan
        //tampilan apapun yang di set visibility visible,, menampilkan tampilan
        textNothing.setVisibility(View.GONE);
        rcKuliner.setVisibility(View.GONE);

        FirebaseDatabase.getInstance().getReference("kuliner")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance()
                .getReference("kuliner")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            boolean cek = true;

                            Iterator localIterator = dataSnapshot.getChildren().iterator();
                            listKuliner.removeAll(listKuliner);
                            listKuliner = new ArrayList<ModelKuliner>();
                            while (localIterator.hasNext()) {
                                ModelKuliner localDataUser = (ModelKuliner) ((DataSnapshot) localIterator.next()).getValue(ModelKuliner.class);

                                //menyeleksi apakah variable favorit di data kuliner bernilai 5
                                //jika benar variabel bernilai 5, maka data kuliner tersebut dimunculkan
                                if (localDataUser.getFavorit() == 5){
                                    listKuliner.add(localDataUser);
                                    cek = false;
                                }
                            }

                            if (cek){
                                textNothing.setText("Belum ada data kuliner favorit");
                                textNothing.setVisibility(View.VISIBLE);
                            }
                            else {
                                rcKuliner.setVisibility(View.VISIBLE);
                                RecyclerLihatKuliner adapterLihatKelas = new RecyclerLihatKuliner(listKuliner);
                                LinearLayoutManager localLinearLayoutManager = new LinearLayoutManager(ActKulinerFavorit.this, 1, false);
                                rcKuliner.setLayoutManager(localLinearLayoutManager);
                                rcKuliner.setNestedScrollingEnabled(false);
                                rcKuliner.setAdapter(adapterLihatKelas);
                            }
                        }
                        else {
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
                        Toast.makeText(ActKulinerFavorit.this, "Gagal Mengambil Data Terbaru", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getCariKuliner(final String cari) {
        shimmerLoad.startShimmerAnimation();
        shimmerLoad.setVisibility(View.VISIBLE);
        textNothing.setVisibility(View.GONE);
        rcKuliner.setVisibility(View.GONE);
        FirebaseDatabase.getInstance()
                .getReference("kuliner")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            boolean cek = true;

                            Iterator localIterator = dataSnapshot.getChildren().iterator();
                            listKuliner.removeAll(listKuliner);
                            listKuliner = new ArrayList<ModelKuliner>();
                            while (localIterator.hasNext()) {
                                ModelKuliner localDataUser = (ModelKuliner) ((DataSnapshot) localIterator.next()).getValue(ModelKuliner.class);
                                if (localDataUser.getFavorit() == 5){
                                    if (localDataUser.getNama().contains(cari)) {
                                        listKuliner.add(localDataUser);
                                        cek = false;
                                    }
                                }
                            }

                            if (cek){
                                textNothing.setText("Belum ada data kuliner favorit");
                                textNothing.setVisibility(View.VISIBLE);
                            }
                            else {
                                rcKuliner.setVisibility(View.VISIBLE);
                                RecyclerLihatKuliner adapterLihatKelas = new RecyclerLihatKuliner(listKuliner);
                                LinearLayoutManager localLinearLayoutManager = new LinearLayoutManager(ActKulinerFavorit.this, 1, false);
                                rcKuliner.setLayoutManager(localLinearLayoutManager);
                                rcKuliner.setNestedScrollingEnabled(false);
                                rcKuliner.setAdapter(adapterLihatKelas);
                            }
                        }
                        else {
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
                        Toast.makeText(ActKulinerFavorit.this, "Gagal Mengambil Data Terbaru", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        ActDetailKuliner.dataKuliner = listKuliner.get(position);
        startActivity(new Intent(ActKulinerFavorit.this, ActDetailKuliner.class));
        finish();
    }
}
