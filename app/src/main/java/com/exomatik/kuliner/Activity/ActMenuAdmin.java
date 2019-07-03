package com.exomatik.kuliner.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class ActMenuAdmin extends AppCompatActivity implements ItemClickSupport.OnItemClickListener {
    private Button btnTambah;
    private RecyclerView rcKuliner;
    private TextView textNothing;
    private ImageView back;
    private ArrayList<ModelKuliner> listKuliner = new ArrayList<ModelKuliner>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_menu_admin);

        btnTambah = (Button) findViewById(R.id.btn_tambah);
        rcKuliner = (RecyclerView) findViewById(R.id.rc_kuliner);
        textNothing = (TextView) findViewById(R.id.text_nothing);
        back = (ImageView) findViewById(R.id.back);

        getDataKuliner();
        ItemClickSupport.addTo(rcKuliner).setOnItemClickListener(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActMenuAdmin.this, MainActivity.class));
                finish();
            }
        });

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActMenuAdmin.this, ActFormTambah.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActMenuAdmin.this, MainActivity.class));
        finish();
    }

    private void getDataKuliner() {
        FirebaseDatabase.getInstance()
                .getReference("kuliner")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Iterator localIterator = dataSnapshot.getChildren().iterator();
                            listKuliner.removeAll(listKuliner);
                            listKuliner = new ArrayList<ModelKuliner>();
                            while (localIterator.hasNext()) {
                                ModelKuliner localDataUser = (ModelKuliner) ((DataSnapshot) localIterator.next()).getValue(ModelKuliner.class);
                                listKuliner.add(localDataUser);
                            }

                            RecyclerLihatKuliner adapterLihatKelas = new RecyclerLihatKuliner(listKuliner);
                            LinearLayoutManager localLinearLayoutManager = new LinearLayoutManager(ActMenuAdmin.this, 1, false);
                            rcKuliner.setLayoutManager(localLinearLayoutManager);
                            rcKuliner.setNestedScrollingEnabled(false);
                            rcKuliner.setAdapter(adapterLihatKelas);
                        }
                        else {
                            textNothing.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(ActMenuAdmin.this, "Gagal Mengambil Data Terbaru", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        ActFormTambah.dataKuliner = listKuliner.get(position);
        startActivity(new Intent(ActMenuAdmin.this, ActFormTambah.class));
        finish();
    }
}
