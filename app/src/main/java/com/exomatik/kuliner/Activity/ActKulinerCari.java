package com.exomatik.kuliner.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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

import io.supercharge.shimmerlayout.ShimmerLayout;

public class ActKulinerCari extends AppCompatActivity implements ItemClickSupport.OnItemClickListener {
    private RecyclerView rcKuliner;
    private TextView textNothing;
    private ImageView back, btnCari;
    private ArrayList<ModelKuliner> listKuliner = new ArrayList<ModelKuliner>();
    private EditText etCari;
    private RecyclerLihatKuliner adapter;
    private ShimmerLayout shimmerLoad;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_kuliner_cari);

        rcKuliner = (RecyclerView) findViewById(R.id.rc_kuliner);
        textNothing = (TextView) findViewById(R.id.text_nothing);
        back = (ImageView) findViewById(R.id.back);
        btnCari = (ImageView) findViewById(R.id.img_cari);
        etCari = (EditText) findViewById(R.id.et_cari);
        shimmerLoad = (ShimmerLayout) findViewById(R.id.shimmer_load);

        getDataKuliner("");
        ItemClickSupport.addTo(rcKuliner).setOnItemClickListener(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActKulinerCari.this, MainActivity.class));
                finish();
            }
        });

        etCari.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String text = etCari.getText().toString();
                    getDataKuliner(text);
                }
                return false;
            }
        });
        btnCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etCari.getText().toString();
                getDataKuliner(text);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActKulinerCari.this, MainActivity.class));
        finish();
    }

    private void getDataKuliner(final String cari) {
        progressDialog = new ProgressDialog(ActKulinerCari.this);
        progressDialog.setMessage(getResources().getString(R.string.progress_title1));
        progressDialog.setTitle(getResources().getString(R.string.progress_text1));
        progressDialog.setCancelable(false);
        progressDialog.show();
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
                            listKuliner = new ArrayList<ModelKuliner>();
                            while (localIterator.hasNext()) {
                                ModelKuliner localDataUser = (ModelKuliner) ((DataSnapshot) localIterator.next()).getValue(ModelKuliner.class);
                                if (localDataUser.getNama().contains(cari)) {
                                    Log.e("ada", localDataUser.getNama());
                                    listKuliner.add(localDataUser);
                                    cek = false;
                                }
                            }

                            if (cek) {
                                textNothing.setText("Tidak ditemukan nama kuliner tersebut");
                                textNothing.setVisibility(View.VISIBLE);
                                rcKuliner.setVisibility(View.GONE);
                            }
                            else {
                                textNothing.setVisibility(View.GONE);
                                rcKuliner.setVisibility(View.VISIBLE);
                                adapter = new RecyclerLihatKuliner(listKuliner);
                                LinearLayoutManager localLinearLayoutManager = new LinearLayoutManager(ActKulinerCari.this, 1, false);
                                rcKuliner.setLayoutManager(localLinearLayoutManager);
                                rcKuliner.setNestedScrollingEnabled(false);
                                rcKuliner.setAdapter(adapter);
                            }
                        } else {
                            textNothing.setVisibility(View.VISIBLE);
                        }
                        shimmerLoad.stopShimmerAnimation();
                        shimmerLoad.setVisibility(View.GONE);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        shimmerLoad.stopShimmerAnimation();
                        shimmerLoad.setVisibility(View.GONE);
                        textNothing.setText("Error mengambil data");
                        textNothing.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                        Toast.makeText(ActKulinerCari.this, "Gagal Mengambil Data Terbaru", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        ActDetailKuliner.dataKuliner = listKuliner.get(position);
        startActivity(new Intent(ActKulinerCari.this, ActDetailKuliner.class));
        finish();
    }
}
